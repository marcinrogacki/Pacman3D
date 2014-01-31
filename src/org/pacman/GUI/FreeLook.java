package org.pacman.GUI;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.R;
import org.pacman.draw.primitives.Circle;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;

public class FreeLook {
	private SquareGUI mTopBar = new SquareGUI();
	private SquareGUI mArrows = new SquareGUI();
	private SquareGUI mFly 	  = new SquareGUI();
	private Circle mBtnA	  = new Circle();
	private Circle mBtnB	  = new Circle();
	private float mRatio;
	/*
	 * 
	 */
	public FreeLook(Resources res, GL10 gl, float ratio){
		mRatio = ratio;
		Bitmap bmTopBar[] = { loadBitmap(res, R.drawable.btn_plus_minus) };
		mTopBar.initSimpleTextured(gl, -1f, -0.3f, 0.6f, 1f, mRatio, 1, bmTopBar);
		
		bmTopBar[0] = loadBitmap(res, R.drawable.btn_arrows);
		mArrows.initSimpleTextured(gl, -1f, -0.4f, -1f, -0.4f, mRatio, 1, bmTopBar);
		
		bmTopBar[0] = loadBitmap(res, R.drawable.btn_up_down);
		mFly.initSimpleTextured(gl, 0.6f, 1f, 0.3f, 1f, mRatio, 1, bmTopBar);
		
		float[] vec = mBtnA.initVertices(mRatio-0.2f, -0.6f, 0.15f, 
				16, 16);
		mBtnA.initVerticlesBuffer(vec);
		vec = mBtnB.initVertices(mRatio-0.5f, -0.8f, 0.15f, 16, 16);
		mBtnB.initVerticlesBuffer(vec);
		
		bmTopBar[0].recycle();
	}	
	/*
	 * 
	 */
	public void draw(GL10 gl){
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-mRatio, mRatio, -1, 1, 1f, 0f);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 1f, 0, 0, 0, 0, 1, 0);		
		
		//draw components
		
//		mTopBar.drawSimpleTextured(gl, 0);		
		mArrows.drawSimpleTextured(gl, 0);
//		mFly.drawSimpleTextured(gl, 0);		
//		mBtnA.draw(gl);		
//		mBtnB.draw(gl);
	}
	
	private Bitmap loadBitmap(Resources res, int androidTexId){
		//Get the texture from the Android resource directory
		InputStream is = res.openRawResource(androidTexId);
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
		return bitmap;
	}
}
