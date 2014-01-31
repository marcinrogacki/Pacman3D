package org.pacman.draw.models;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import org.pacman.R;

import org.pacman.draw.DrawableObjectInterface;
import org.pacman.draw.primitives.Circle;
import org.pacman.draw.primitives.Sphere;

public class Pacman implements DrawableObjectInterface {
	private float mMouthAngle 		= 20f;
	float mMoveMouth 				= 6f;
	private Sphere mHalfBody		= new Sphere();
	private Sphere mEye 			= new Sphere();
	protected Circle mTopMouth  	= new Circle();
	/** bottom mouth have an tongue texture */
	protected Circle mBottomMouth  	= new Circle();
	/** */
	float radius;
	
	/**
	 * 
	 * @return
	 */
	public float getRadius()
	{
		return this.radius;
	}
	
	public void init(float radius)
	{
		this.radius = radius;
		float[] verticlesVector;
		float[] normalsVector;
		// body
		byte bodySegments = 10;
		verticlesVector = this.mHalfBody.initVertices(radius, bodySegments, bodySegments,
					   (byte)(bodySegments / 2), bodySegments);
		normalsVector = this.mHalfBody.initNormals(verticlesVector);
		this.mHalfBody.initNormalBuffer(normalsVector);
		this.mHalfBody.initVerticlesBuffer(verticlesVector);
		// bottom mouth
		verticlesVector = this.mBottomMouth.initVertices(0, 0, radius, bodySegments, 
				bodySegments/2);
		normalsVector = this.mBottomMouth.initNormals(verticlesVector);
		this.mBottomMouth.initNormalBuffer(normalsVector);
		this.mBottomMouth.initVerticlesBuffer(verticlesVector);
		// top mouth
		this.mTopMouth.setColor(1, 0, 0, 1);
		verticlesVector = this.mTopMouth.initVertices(0, 0, radius, bodySegments, 
				bodySegments/2);
		normalsVector = this.mTopMouth.initNormals(verticlesVector);
		this.mTopMouth.initNormalBuffer(normalsVector);
		this.mTopMouth.initVerticlesBuffer(verticlesVector);
		// eye
		byte eyeSegments = 6;
		this.mEye.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		verticlesVector = this.mEye.initVertices(radius/10f, eyeSegments, eyeSegments,
				       (byte)(eyeSegments / 2), eyeSegments);
		normalsVector = this.mEye.initNormals(verticlesVector);
		this.mEye.initNormalBuffer(normalsVector);
		this.mEye.initVerticlesBuffer(verticlesVector);
	}
	
	public void initTextures(GL10 gl, Context ctx)
	{
		this.mBottomMouth.loadBitmap(gl, ctx, R.drawable.tongue);
	}
	
	public void draw(GL10 gl)
	{
		if (mMouthAngle > 35f || mMouthAngle < 6f) {
			mMoveMouth *= -1;
		}
		
		mMouthAngle += mMoveMouth;
		// body
		gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
		gl.glPushMatrix();
			gl.glRotatef(-mMouthAngle, 0.0f, 0.0f, 1.0f);
			this.mHalfBody.draw(gl);
			// need to rotate bottom
			gl.glRotatef(180f + 2 * mMouthAngle, 0.0f, 0.0f, 1.0f);
			this.mHalfBody.draw(gl);
		gl.glPopMatrix();
		
		// mouth
		gl.glPushMatrix();
			gl.glRotatef(90, 0f, 1f, 0f);
			gl.glRotatef(90+mMouthAngle, 1f, 0f, 0f);
			this.mTopMouth.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			gl.glRotatef(-90, 0f, 1f, 0f);
			gl.glRotatef(-90+mMouthAngle, 1f, 0f, 0f);
			this.mBottomMouth.draw(gl);
		gl.glPopMatrix();
		
		// display left eye
		gl.glColor4f(0, 0, 0, 1);
		gl.glPushMatrix();
			gl.glRotatef(40f, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(30f, 1.0f, 0.0f, 0.0f);
			gl.glTranslatef(0.0f, 1.0f, 0.0f);
			this.mEye.draw(gl);
		gl.glPopMatrix();
		
		// display right eye
		gl.glPushMatrix();
			gl.glRotatef(40f, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(-30f, 1.0f, 0.0f, 0.0f);
			gl.glTranslatef(0.0f, 1.0f, 0.0f);
			this.mEye.draw(gl);
		gl.glPopMatrix();
		

		gl.glColor4f(1, 1, 1, 1);
	}
}
