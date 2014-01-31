package org.pacman;

import org.pacman.math.DetectCollision;
import org.pacman.physics.Vector3D;
import org.pacman.utilities.GLStateChanger;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameplayGLView extends GLSurfaceView
{
	/** 
	 * On fly state changer. Allows manage GL states outside glDraw where gl 
	 * context is avaliable.
	 */
	protected GLStateChanger glStateChanger = new GLStateChanger();
	private GameplayRenderer gameplayRender;
	/** @var Helps calc HUD detect collision (pressed HUD) */
	DetectCollision dc = new DetectCollision();
	protected boolean choperCam = false;
	/**
	 * 
	 * @param context
	 */
	public GameplayGLView(Context context) {
		super(context);
		this.glStateChanger.setLighting(true);
		this.setFocusableInTouchMode(true);
		this.setEGLConfigChooser(true);
		gameplayRender = new GameplayRenderer(context, this.glStateChanger);
		this.setRenderer(gameplayRender);
	}
	/**
	 * 
	 * @param point
	 * @param dispWidth
	 * @param dispHeight
	 * @return
	 */
	private boolean changeCam(PointF point, float dispWidth, 
			float dispHeight)
	{
		PointF a = new PointF(dispWidth*0.6f, dispHeight*0.6f);
		PointF b = new PointF(dispWidth, dispHeight);
		return dc.inSquere(point, a, b);
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
	private boolean movePacmanDown(final PointF point, float dispWidth,
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
	private boolean movePacmanUp(final PointF point, float dispWidth,
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
	private boolean movePacmanRight(PointF point, float dispWidth, float dispHeight)
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
	private boolean movePacmanLeft(final PointF point, float dispWidth,
			float dispHeight)
	{
		PointF a = new PointF(0f, dispHeight);
		PointF b = new PointF(0, dispHeight*0.7f);
		PointF c = new PointF(dispWidth*0.15f, dispHeight*0.85f);
		return dc.inTriangle(point, a, b, c);
	}
	
//	/**
//	 * 
//	 * @param point
//	 * @param dispWidth
//	 * @param dispHeight
//	 * @return
//	 */
//	private boolean rotateCamLeft(PointF point, float dispWidth, 
//			float dispHeight)
//	{
//		PointF leftTop = new PointF(dispWidth*0.7f, dispHeight*0.8f);
//		PointF rightBottom = new PointF(dispWidth*0.85f,dispHeight);
//		return dc.inSquere(point, leftTop, rightBottom);
//	}
//	
//	/**
//	 * 
//	 * @param point
//	 * @param dispWidth
//	 * @param dispHeight
//	 * @return
//	 */
//	private boolean rotateCamRight(PointF point, float dispWidth, 
//			float dispHeight)
//	{
//		PointF leftTop = new PointF(dispWidth*0.85f, dispHeight*0.8f);
//		PointF rightBottom = new PointF(dispWidth,dispHeight);
//		return dc.inSquere(point, leftTop, rightBottom);
//	}
	
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
    		this.gameplayRender.zoomCam(0.98f);
        	return true;
        }
        if (this.checkZoomOutControl(point, dispWidth, dispHeight)) {
    		this.gameplayRender.zoomCam(1.02f);
        	return true;
        }
        
//        // rotate cam around y
//		if (this.rotateCamRight(point, dispWidth, dispHeight)) {
//			this.gameplayRender.rotateCamAroundY(0.1f);
//			return true;
//		}
//		if (this.rotateCamLeft(point, dispWidth, dispHeight)) {
//			this.gameplayRender.rotateCamAroundY(-0.1f);
//			return true;
//		}
		
        // MOVMENT CONTROL
		if (this.movePacmanUp(point, dispWidth, dispHeight)) {
    		gameplayRender.moveAlongZAxis(0.2f);
    		gameplayRender.moveCamAlongZAxis(0.2f);
			return true;
		}
		if (this.movePacmanDown(point, dispWidth, dispHeight)) {
    		gameplayRender.moveAlongZAxis(-0.2f);
    		gameplayRender.moveCamAlongZAxis(-0.2f);
			return true;
		}
		if (this.movePacmanRight(point, dispWidth, dispHeight)) {
			gameplayRender.moveAlongXAxis(0.2f);
    		gameplayRender.moveCamAlongXAxis(0.2f);
			return true;
		}
		if (this.movePacmanLeft(point, dispWidth, dispHeight)) {
			gameplayRender.moveAlongXAxis(-0.2f);
    		gameplayRender.moveCamAlongXAxis(-0.2f);
			return true;
		}
		
		switch (e.getAction()) {
		
			// model switcher
        	case MotionEvent.ACTION_DOWN:
        		if (this.changeCam(point, dispWidth, dispHeight)) {
        			if (choperCam) {
        				choperCam = false;
        				this.gameplayRender.camSetPosition(new Vector3D(0, 20, 1));
        			} else {
        				choperCam = true;
        				this.gameplayRender.camSetPosition(new Vector3D(0, 6f, 10f));
        			}
        			return true;
        		}
	    		break;
        }
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
