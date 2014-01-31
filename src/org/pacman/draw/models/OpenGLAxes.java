package org.pacman.draw.models;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObjectInterface;
import org.pacman.draw.primitives.Sphere;
import org.pacman.utilities.BufferMaker;

public class OpenGLAxes implements DrawableObjectInterface
{
	protected FloatBuffer xAxisBuffer;
	protected FloatBuffer yAxisBuffer;
	protected FloatBuffer zAxisBuffer;
	protected Sphere sphere;
	protected float size;
	
	public void init(float size)
	{
		this.size = size;
		this.sphere = new Sphere();
		byte elm = 4;
		this.sphere.initVerticlesBuffer(
			this.sphere.initVertices(0.4f, elm, elm, elm, elm)
		);
		
		float xVerticles[] = {-1*size, 0, 0, 1*size, 0, 0};
		float yVerticles[] = { 0, -1*size, 0, 0, 1*size, 0};
		float zVerticles[] = { 0, 0, -1*size, 0, 0, 1*size};
		
		this.xAxisBuffer = BufferMaker.createFloat(xVerticles);
		this.yAxisBuffer = BufferMaker.createFloat(yVerticles);
		this.zAxisBuffer = BufferMaker.createFloat(zVerticles);
	}
	
	@Override
	public void draw(GL10 gl)
	{
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.xAxisBuffer);
		gl.glColor4f(1, 0, 0, 1);
		gl.glPushMatrix();
		gl.glDrawArrays(GL10.GL_LINES, 0, (int)this.xAxisBuffer.capacity()/3);
		gl.glTranslatef(this.size, 0, 0);
		this.sphere.draw(gl);
		gl.glPopMatrix();
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.yAxisBuffer);
		gl.glColor4f(0, 1, 0, 1);
		gl.glPushMatrix();
		gl.glDrawArrays(GL10.GL_LINES, 0, (int)this.yAxisBuffer.capacity()/3);
		gl.glTranslatef(0, this.size, 0);
		this.sphere.draw(gl);
		gl.glPopMatrix();
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.zAxisBuffer);
		gl.glColor4f(0, 0, 1, 1);
		gl.glPushMatrix();
		gl.glDrawArrays(GL10.GL_LINES, 0, (int)this.zAxisBuffer.capacity()/3);
		gl.glTranslatef(0, 0, this.size);
		this.sphere.draw(gl);
		gl.glPopMatrix();

		gl.glColor4f(1, 1, 1, 1);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_CULL_FACE);
		
	}

}
