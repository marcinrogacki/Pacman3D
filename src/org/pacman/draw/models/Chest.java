package org.pacman.draw.models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;


public class Chest extends DrawableObject
{
	private ByteBuffer indicesBuffer;
	int[] textures = new int[1];

	/**
	 * 
	 */
	public float [] initVertices(){
		float vCoords[] = {
	            // X, Y, Z
				-1.0f, -1.0f, 1.0f, //v0 //front
				1.0f, -1.0f, 1.0f, 	//v1
				-1.0f, 1.0f, 1.0f, 	//v2
				1.0f, 1.0f, 1.0f, 	//v3
	
				1.0f, -1.0f, 1.0f, 	//4 //right
				1.0f, -1.0f, -1.0f, //5
				1.0f, 1.0f, 1.0f, 	//6
				1.0f, 1.0f, -1.0f,	//7
				
				-1.0f, -1.0f, -1.0f, //8 //left
				-1.0f, -1.0f, 1.0f,	 //9
				-1.0f,  1.0f, -1.0f, //10
				-1.0f, 1.0f, 1.0f,	 //11
				
				-1.0f, 1.0f, 1.0f,	 //12 //up
				1.0f, 1.0f, 1.0f,	 //13
				-1.0f, 1.0f, -1.0f,	 //14
				1.0f, 1.0f, -1.0f,	 //15
				
				1.0f, -1.0f, -1.0f,  //16 //back
				-1.0f, -1.0f, -1.0f, //17
				1.0f, 1.0f,  -1.0f,  //18
				-1.0f, 1.0f, -1.0f,	 //19
				
				1.0f, -1.0f, 1.0f, 	//v20  //down
				-1.0f, -1.0f, 1.0f, //v21
				1.0f, -1.0f, -1.0f, //v22
				-1.0f, -1.0f, -1.0f, //v23	 
	    };
		
		byte indices[] = {
				// Faces definition
				0, 1, 2, 2, 1, 3, 		// front
				4, 5, 6, 6, 5, 7, 		// right
				8, 9, 10, 10, 9, 11, 	// left
				12, 13, 14, 14, 13, 15, // up
				16, 17, 18, 18, 17, 19, // back
				20, 21, 22, 22, 21, 23, //down
										};
		
		indicesBuffer = ByteBuffer.allocateDirect(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		
		return vCoords;
	}
	
	/**
	 * 
	 * @return
	 */
	public float[] initTextureUV() 
	{	
		float tCoords[] = {
				0.5f, 1.0f, //front
                1.0f, 1.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
                
                0.0f, 1.0f, //right
                0.5f, 1.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                
                0.0f, 1.0f, //left
                0.5f, 1.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                
                0.5f, 0.5f, //up
                1.0f, 0.5f,
                0.5f, 0.0f,
                1.0f, 0.0f,
                
                0.0f, 0.5f, //back
                0.5f, 0.5f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                
                0.0f, 0.5f, //down
                0.5f, 0.5f,
                0.0f, 0.0f,
                0.5f, 0.0f    
        };
		return tCoords;
	}
	
	/**
	 * @param gl
	 */
	public void draw(GL10 gl){		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glCullFace(GL10.GL_BACK);
		gl.glFrontFace(GL10.GL_CCW);
			
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texturesUVBuffer);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.verticesBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indicesBuffer.capacity(), 
				GL10.GL_UNSIGNED_BYTE, indicesBuffer);
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	/**
	 * 
	 * @param gl
	 * @param context
	 * @param textureResId
	 */
	public void loadBitmap(GL10 gl, Context context, final int textureResId){
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(textureResId);
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
