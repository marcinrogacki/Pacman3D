package org.pacman;

import org.pacman.math.DetectCollision;
import org.pacman.utilities.GLStateChanger;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class DeploymentGLView extends GLSurfaceView {
	/** 
	 * On fly state changer. Allows manage GL states outside glDraw where gl 
	 * context is avaliable.
	 */
	protected GLStateChanger glStateChanger = new GLStateChanger();
	private DeploymentRenderer mRender;
	private float mPreviousX;
	private float mPreviousY;
	/** @var Helps calc HUD detect collision (pressed HUD) */
	DetectCollision dc = new DetectCollision();
	
	/**
	 * 
	 * @param context
	 */
	public DeploymentGLView(Context context) {
		super(context);
//		this.glStateChanger.setLighting(false);
		this.setFocusableInTouchMode(true);
		this.setEGLConfigChooser(true);
		mRender = new DeploymentRenderer(context, this.glStateChanger);
		this.setRenderer(mRender);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean checkZoomOutControl(final PointF point, float dispWidth, 
			float dispHeight)
	{
		PointF leftTop = new PointF(0, 0);
		PointF rightBottom = new PointF(dispWidth*0.2f, dispHeight*0.2f);
		return dc.inSquere(point, leftTop, rightBottom);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean checkZoomInControl(final PointF point, float dispWidth, 
			float dispHeight)
	{
		PointF leftTop = new PointF(dispWidth*0.2f, 0);
		PointF rightBottom = new PointF(dispWidth*0.4f,dispHeight*0.2f);
		return dc.inSquere(point, leftTop, rightBottom);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean fly(final PointF point, float dispWidth, float dispHeight)
	{
		PointF leftTop = new PointF(dispWidth*0.8f, 0);
		PointF rightBottom = new PointF(dispWidth, dispHeight*0.2f);
		return dc.inSquere(point, leftTop, rightBottom);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean land(final PointF point, float dispWidth, float dispHeight)
	{
		PointF leftTop = new PointF(dispWidth*0.8f, dispHeight*0.2f);
		PointF rightBottom = new PointF(dispWidth, dispHeight*0.4f);
		return dc.inSquere(point, leftTop, rightBottom);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean moveBackward(final PointF point, float dispWidth,
			float dispHeight)
	{
		PointF a = new PointF(0f, dispHeight);
		PointF b = new PointF(dispWidth*0.3f, dispHeight);
		PointF c = new PointF(dispWidth*0.15f, dispHeight*0.85f);
		return dc.inTriangle(point, a, b, c);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean moveForward(final PointF point, float dispWidth,
			float dispHeight)
	{
		PointF a = new PointF(0f, dispHeight*0.7f);
		PointF b = new PointF(dispWidth*0.3f, dispHeight*0.7f);
		PointF c = new PointF(dispWidth*0.15f, dispHeight*0.85f);
		return dc.inTriangle(point, a, b, c);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean strafeRight(PointF point, float dispWidth, float dispHeight)
	{
		PointF a = new PointF(dispWidth*0.4f, dispHeight);
		PointF b = new PointF(dispWidth*0.4f, dispHeight*0.7f);
		PointF c = new PointF(dispWidth*0.15f, dispHeight*0.85f);
		return dc.inTriangle(point, a, b, c);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean nextModel(PointF point, float dispWidth, float dispHeight)
	{
		PointF a = new PointF(dispWidth*0.7f, dispHeight);
		PointF b = new PointF(dispWidth, dispHeight);
		PointF c = new PointF(dispWidth*0.7f, dispHeight*0.7f);
		return dc.inTriangle(point, a, b, c);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean previuosModel(PointF point, float dispWidth, 
			float dispHeight)
	{
		PointF a = new PointF(dispWidth*0.7f, dispHeight*0.7f);
		PointF b = new PointF(dispWidth, dispHeight);
		PointF c = new PointF(dispWidth, dispHeight*0.7f);
		return dc.inTriangle(point, a, b, c);
	}
	
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean strafeLeft(final PointF point, float dispWidth,
			float dispHeight)
	{
		PointF a = new PointF(0f, dispHeight);
		PointF b = new PointF(0, dispHeight*0.7f);
		PointF c = new PointF(dispWidth*0.15f, dispHeight*0.85f);
		return dc.inTriangle(point, a, b, c);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent e) {
        PointF point = new PointF(e.getX(), e.getY());
        float dispWidth = this.getWidth();
        float dispHeight = this.getHeight();
        
        // ZOOM CONTROL
        if (this.checkZoomInControl(point, dispWidth, dispHeight)) {
    		this.mRender.zoomCam(0.98f);
        	return true;
        }
        if (this.checkZoomOutControl(point, dispWidth, dispHeight)) {
    		this.mRender.zoomCam(1.02f);
        	return true;
        }
        
        // FLY CONTROL
        if (this.fly(point, dispWidth, dispHeight)) {
			mRender.fly(0.05f);
        	return true;
        }
		if(this.land(point, dispWidth, dispHeight)){
			mRender.fly(-0.05f);
			return true;
		}
		
        // MOVMENT CONTROL
		if (this.moveForward(point, dispWidth, dispHeight)) {
    		mRender.walkCam(0.05f);
			return true;
		}
		if (this.moveBackward(point, dispWidth, dispHeight)) {
    		mRender.walkCam(-0.05f);
			return true;
		}
		if (this.strafeRight(point, dispWidth, dispHeight)) {
			mRender.strafe(0.05f);
			return true;
		}
		if (this.strafeLeft(point, dispWidth, dispHeight)) {
			mRender.strafe(-0.05f);
			return true;
		}

		switch (e.getAction()) {
		
			// model switcher
        	case MotionEvent.ACTION_DOWN:
	    		if (this.nextModel(point, dispWidth, dispHeight)) {
	    			mRender.nextModel();
	    			return true;
	    		}
	    		if (this.previuosModel(point, dispWidth, dispHeight)) {
	    			mRender.previousModel();
	    			return true;
	    		}
	    		break;
        	
        	// CAMERA POSITION
            case MotionEvent.ACTION_MOVE: 
                float dx = point.x - mPreviousX;
                float dy = point.y - mPreviousY;
                mRender.setOrbitCam(dx, dy, 0.3f);
                break;
        }

        mPreviousX = point.x;
        mPreviousY = point.y;
        return true;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU ) {
	    	boolean previosState = this.glStateChanger.isLighting();
	    	this.glStateChanger.setLighting(!previosState);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
