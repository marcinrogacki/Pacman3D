package org.pacman.draw.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObject;
import org.pacman.physics.Vector3D;
import org.pacman.utilities.BufferMaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * Simple class to generate cirlce primitive in Android OpenGL ES 1.0. <br />
 * It is based on triangle fans and drawn by trigonometric functions. <br />
 * Circle class does not use PushMatrix() or PopMatrix() methods. <br />
 * Counter clockwise triangles draw direction, clockwise for whole circle <br />
 * 
 * @author Marcin Rogacki
 */
public class Circle extends DrawableObject
{
	protected FloatBuffer textureBuffer;
	protected ByteBuffer indicesBuffer;
	protected int textures[] = new int[1];
	
	/** Cirlce flat color */
	private float mR = 1.0f, mG = 1.0f, mB = 1.0f, mAlpha = 1.0f;
	
	/**
	 * Sets color of sphere.
	 * 
	 * @param r		Amount of red from 0 to 1.
	 * @param g		Amount of green from 0 to 1.
	 * @param b		Amount of blue from 0 to 1.
	 * @param alpha Amount of alpha from 0 to 1.
	 */
	public void setColor(float r, float g, float b, float alpha) {
		this.mR = r;
		this.mG = g;
		this.mB = b;
		this.mAlpha = alpha;
	}
	
	/*
	 * 
	 */
	public float[] initVertices(float xCenter, float yCenter, float radius,
			int triangles, int drawTrinagles)
	{
		//3 point per triangle, one point is a 2 dimansion coord
		int amount = triangles*9; 		
		float vertCoords[] = new float[amount];
		float texUV[] = new float[amount];
		byte indices[] = new byte[amount];
		final double angle_factor = 2*Math.PI/triangles; //angle per triangle
		
		//wzor obl punktow z: http://edu.i-lo.tarnow.pl/inf/utils/002_roz/2008_06.php
		//init coords
		
	 	//center x,y,z
		vertCoords[3] = xCenter;
		vertCoords[4] = yCenter;
		vertCoords[5] = 0; 	
		
//		int offset = 0;
		
		for(int i=0, offset=0; i<drawTrinagles; i++){ 		
			// Each loop initializes a one triangle. A triangle have 3 point on 
			// 2 coord system (x,y,z). So here is multiplication by 9.
			offset = i*9;
			
			// x1 y1 z1
			vertCoords[offset]   =  
				xCenter + (float)(radius * Math.cos(angle_factor * i));
			vertCoords[offset+1] = 
				yCenter + (float)(radius * Math.sin(-angle_factor * i));
			vertCoords[offset+2] = 0;
			
			// x2 y2 z2
			vertCoords[offset+3] = xCenter;
			vertCoords[offset+4] = yCenter;
			vertCoords[offset+5] = 0;
			
			// x3 y3 z3
			vertCoords[offset+6] = 
				xCenter + (float)(radius * Math.cos(angle_factor * (i+1)));
			vertCoords[offset+7] = 
				yCenter + (float)(radius * Math.sin(-angle_factor * (i+1)));
			vertCoords[offset+8] = 0;
			
			//tex UV
			texUV[offset]   = 0.5f + (float)(0.5f * Math.cos(angle_factor * i));
			texUV[offset+1] = 0.5f + (float)(0.5f * Math.sin(angle_factor * i));
			texUV[offset+2] = 0;
			
			texUV[offset+3] = 0.5f;
			texUV[offset+4] = 0.5f;
			texUV[offset+5] = 0;
			
			texUV[offset+6] = 0.5f + (float)(0.5f*Math.cos(angle_factor*(i+1)));
			texUV[offset+7] = 0.5f + (float)(0.5f*Math.sin(angle_factor*(i+1)));
			texUV[offset+8] = 0;
			
			//number indices is equal number of points, 
			//So we multiply the current triangle number by 3
			offset = i*3; 
			indices[offset]		= (byte)offset; 
			indices[offset+1]	= (byte)(offset+1); 
			indices[offset+2]	= (byte)(offset+2);
		}
		
		this.textureBuffer = BufferMaker.createFloat(texUV);
		this.indicesBuffer = BufferMaker.createByte(indices);
		return vertCoords;
	}
	
	/**
	 * Inits normals based on coords
	 * 
	 * @param vertCoords
	 * @return the vector of calculated normals
	 */
	public float[] initNormals(final float vertCoords[]) {
		int allPoint = vertCoords.length;
		float[] normalVector = new float[allPoint];
		Vector3D point = new Vector3D(0);
		Vector3D basicNormal = new Vector3D(0);
		int i = 0;
		while (i < allPoint) {
			basicNormal.setX(0);
			basicNormal.setY(0);
			basicNormal.setZ(1);
			point.setX(vertCoords[i]);
			point.setY(vertCoords[i+1]);
			point.setZ(vertCoords[i+2]);
			// move basic normal to point
			basicNormal.move(point);
			basicNormal = basicNormal.normalize();
			normalVector[i++] = basicNormal.getX();
			normalVector[i++] = basicNormal.getY();
			normalVector[i++] = basicNormal.getZ();
		}
		
		return normalVector;
	}
	
	/**
	 * 
	 */
	public void draw(GL10 gl){
		// enable
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glFrontFace(GL10.GL_CCW);
		
		// pointers
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.verticesBuffer);
		if (null != this.normalBuffer) {
			gl.glNormalPointer(GL10.GL_FLOAT, 0, this.normalBuffer);
		}
		
		// draw
		gl.glColor4f(this.mR, this.mG, this.mB, this.mAlpha);
		gl.glDrawElements(GL10.GL_TRIANGLES, indicesBuffer.capacity(), 
				GL10.GL_UNSIGNED_BYTE, indicesBuffer);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		// disable
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	/**
	 * 
	 * @param gl
	 * @param context
	 * @param androidTexId
	 */
	public void loadBitmap(GL10 gl, Context context, int androidTexId){
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(androidTexId);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}//try-finally		
		// Tell OpenGL to generate textures.
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
	}
}
