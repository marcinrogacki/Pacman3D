package org.pacman.GUI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.primitives.Square;



import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * @author Marcin Rogacki
 *
 */
public class SquareGUI extends Square
{
	private FloatBuffer mTexBuffer;
	private int[] mTextures;
	/**
	 * 
	 */	
	final public void initSimpleTextured(GL10 gl, float left, float right,
			 float bottom, float top, float ratio, int texAmount, 
			 Bitmap bitmap[])
	{
		super.init(left, right, bottom, top, ratio);
		float texCoords[] = {
			0.0f, 1.0f,
			1.0f, 1.0f,
		    0.0f, 0.0f,
		    1.0f, 0.0f
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(texCoords.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mTexBuffer = vbb.asFloatBuffer();
		mTexBuffer.put(texCoords);
		mTexBuffer.position(0);
		
		//Load textures
		mTextures =  new int[texAmount];
		gl.glGenTextures(texAmount, mTextures, 0);		
		for(int i=0; i<texAmount; ++i){
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[i]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap[i], 0);
//			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap[i], GL10.GL_UNSIGNED_BYTE, 0);
		}	
	}	
	/**
	 * Draw square with chosen texture.
	 * 
	 * @param gl context GL10 class 
	 * @param texNum texture number to be displayed (counting starts at zero)
	 */
	final public void drawSimpleTextured(GL10 gl, int texNum){		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);				
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextures[texNum]);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
		super.draw(gl);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
}
