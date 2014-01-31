package org.pacman.draw.effects;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.utilities.BufferMaker;

public class Light {
	private FloatBuffer mAmbientBuffer;
	private FloatBuffer mDiffuseBuffer;
	private FloatBuffer mSpecularBuffer;
	private FloatBuffer mPositionBuffer;
	
	public void initAmbient(float red, float green, float blue, float alpha) {
		float v4f[] = {red, green, blue, alpha};
		this.mAmbientBuffer = BufferMaker.createFloat(v4f);
	}
	
	public void initDiffuse(float red, float green, float blue, float alpha) {
		float v4f[] = {red, green, blue, alpha};
		this.mDiffuseBuffer = BufferMaker.createFloat(v4f);
	}
	
	public void initSpecular(float red, float green, float blue, float alpha) {
		float v4f[] = {red, green, blue, alpha};
		this.mSpecularBuffer = BufferMaker.createFloat(v4f);
	}

	public void initPosition(float x, float y, float z, float w) {
		float v4f[] = {x, y, z, w};
		this.mPositionBuffer = BufferMaker.createFloat(v4f);
	}
	
	public void setLight(GL10 gl, final int GL_LIGHTi) {
		if (this.mAmbientBuffer != null) {
			gl.glLightfv(GL_LIGHTi, GL10.GL_AMBIENT, this.mAmbientBuffer);
		}
		if (this.mDiffuseBuffer != null) {
			gl.glLightfv(GL_LIGHTi, GL10.GL_DIFFUSE, this.mDiffuseBuffer);
		}
		if (this.mSpecularBuffer != null) {
			gl.glLightfv(GL_LIGHTi, GL10.GL_SPECULAR, this.mSpecularBuffer);
		}
		gl.glLightfv(GL_LIGHTi, GL10.GL_POSITION, this.mPositionBuffer);	
	}
}
