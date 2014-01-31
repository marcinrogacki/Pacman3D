package org.pacman.draw.primitives;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObjectInterface;
import org.pacman.utilities.BufferMaker;

public class Triangle implements DrawableObjectInterface {
	private FloatBuffer mBuffer;
	public Triangle(){
		float triangleCoords[] = {
	            // X, Y, Z
	            -0.5f, -0.25f, 0,
	             0.5f, -0.25f, 0,
	             0.0f,  0.559016994f, 0
	        }; 
		
		this.mBuffer = BufferMaker.createFloat(triangleCoords);
	}
	
	public void draw(GL10 gl){
		gl.glPushMatrix();		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, (int)mBuffer.capacity()/3);
		gl.glPopMatrix();
	}
}
