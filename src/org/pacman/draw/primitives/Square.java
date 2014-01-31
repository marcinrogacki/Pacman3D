package org.pacman.draw.primitives;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObjectInterface;
import org.pacman.utilities.BufferMaker;

/**
 * Simple class to generate square primitive in Android OpenGL ES 1.0. <br />
 * It is based on two triangles and can be used to draw FreeLook element. <br />
 * Square class does not use PushMatrix() or PopMatrix() methods. <br />
 * 
 * @author Marcin Rogacki
 */
public class Square implements DrawableObjectInterface {
	private FloatBuffer mCordsBuffer;
	private ByteBuffer mIndicesBuffer;	
	/**
	 * Initialize points and indicates of square in OpenGL coordinates system. <br />
	 * Indices corners for texturing are respectively: <br />
	 * left down, right down, left up, //first triangle <br />
	 * left up, right down, right up   //second triangle <br />
	 * 
	 * @param x1 left down corner
	 * @param y1 left down corner
	 * @param x2 right up corner
	 * @param y2 right up corner
	 * @param ratio the ratio of width to height display. 
	 * Zero gives the original aspect ratio display
	 * @return void
	 */
	public void init(float x1, float x2, float y1, float y2, float ratio){
		if(ratio == 0)
			ratio = 1;
		float squareCoords[] = {
				x1*ratio, y1,
	            x2*ratio, y1,
	            x1*ratio, y2,
	            x2*ratio, y2
	    }; 
		byte indices[] = { 
				0, 1, 2, 2, 1, 3 
		};		
		
		this.mCordsBuffer = BufferMaker.createFloat(squareCoords);
		this.mIndicesBuffer = BufferMaker.createByte(indices);
	}//init()	
	/**
	 * Draws initialized square.
	 * 
	 * @param gl context GL10 class.
	 * @return void
	 */
	public void draw(GL10 gl){
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mCordsBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, mIndicesBuffer.capacity(), GL10.GL_UNSIGNED_BYTE, mIndicesBuffer);		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}//draw()
}//Square class
