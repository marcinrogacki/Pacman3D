package org.pacman;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.pacman.GUI.FreeLook;
import org.pacman.GUI.GLCamera;
import org.pacman.draw.DrawableObjectInterface;
import org.pacman.draw.effects.Light;
import org.pacman.draw.models.Chest;
import org.pacman.draw.models.Ghost;
import org.pacman.draw.models.Pacman;
import org.pacman.draw.primitives.Circle;
import org.pacman.draw.primitives.Cylinder;
import org.pacman.draw.primitives.Sphere;
import org.pacman.utilities.GLStateChanger;


import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.view.WindowManager;

/**
 * Renderer for testing models
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 */
public class DeploymentRenderer implements Renderer
{
	/** 
	 * On fly state changer. Injected in constructor. 
	 * Allows manage GL states outside glDraw where gl context is avaliable.
	 */
	protected GLStateChanger glStateChanger;
	protected int 		mDispWidth, mDispHeight;
	protected float 	mAngle;
	protected Context 	mContext;
	protected GLCamera 	mGLCamera;
	protected FreeLook 	mGUI;
	// light
	protected Light 	mLight;
	/** @var mModels Aray of models to render */
	final int ALL_OBJECTS = 6;
	private DrawableObjectInterface mModels[] = new DrawableObjectInterface[this.ALL_OBJECTS];
	final int ARRAY_MODEL_CHEST 	= 0;
	final int ARRAY_MODEL_SPHERE 	= 1;
	final int ARRAY_MODEL_PACMAN 	= 2;
	final int ARRAY_MODEL_CIRCLE 	= 3;
	final int ARRAY_MODEL_CYLINDER 	= 4;
	final int ARRAY_MODEL_GHOST 	= 5;
	/** @var mCurrentModel Current rendered model */
	DrawableObjectInterface mCurrentModel;
	private int mCurrentModelCounter = this.ARRAY_MODEL_GHOST;
	
	/**
	 * Fetchs contex and display size
	 * @param ctx
	 */
	public DeploymentRenderer(Context ctx, GLStateChanger glStateChanger){
		super();
		this.mContext = ctx;
		this.glStateChanger = glStateChanger;
		// get display size
		WindowManager windowManager =
			(WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		mDispWidth = windowManager.getDefaultDisplay().getWidth();
		mDispHeight = windowManager.getDefaultDisplay().getHeight();
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// camera
		this.mGLCamera = new GLCamera();
		// GUI
		float displayRatio = (float)this.mDispWidth/this.mDispHeight;
		this.mGUI = new FreeLook(mContext.getResources(), gl, displayRatio);
		// light
		this.initLight(gl);
		// models
		this.initModels(gl);
		// render ordering
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// material
		float specular[] = {0.3f, 0.3f, 0.3f, 1.0f};
		float shininess[] = { 10.0f };
		gl.glMaterialfv(
			GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular, 0
		);
		gl.glMaterialfv(
			GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininess, 0
		);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
	} // onSurfaceChanged()
	
	/**
	 * Inits models and put them into array mModels[]
	 * @param gl
	 */
	private void initModels(GL10 gl) {
		float[] verticlesVector;
		float[] normalVector;
		float[] texturesUVVector;
		// chest
		Chest chest = new Chest();
		chest.loadBitmap(gl, this.mContext, R.drawable.box_texture);
		verticlesVector = chest.initVertices();
		texturesUVVector = chest.initTextureUV();
		chest.initTexturesBuffer(texturesUVVector);
		chest.initVerticlesBuffer(verticlesVector);
		this.mModels[this.ARRAY_MODEL_CHEST] = chest;
		// sphere
		Sphere sphere = new Sphere();
		sphere.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		verticlesVector = sphere.initVertices(1f, (byte)20, (byte)20, (byte)20,
				(byte)20);
		normalVector = sphere.initNormals(verticlesVector);
		sphere.initNormalBuffer(normalVector);
		sphere.initVerticlesBuffer(verticlesVector);
		this.mModels[this.ARRAY_MODEL_SPHERE] = sphere;
		// packman	
		Pacman pacman = new Pacman();
		pacman.init(1.0f);
		pacman.initTextures(gl, this.mContext);
		this.mModels[this.ARRAY_MODEL_PACMAN] = pacman;
		// circle
		Circle circle = new Circle();
		circle.setColor(0, 0, 1, 1);
		verticlesVector = circle.initVertices(0f, 0f, 1.0f, 20, 20);
		normalVector = circle.initNormals(verticlesVector);
		circle.initNormalBuffer(verticlesVector);
		circle.initVerticlesBuffer(verticlesVector);
		this.mModels[this.ARRAY_MODEL_CIRCLE] = circle;
		// cylinder
		Cylinder cylinder = new Cylinder();
		cylinder.setColor(1, 0, 0, 2);
		verticlesVector = cylinder.initVertices(1f, 10f, 20, 20);
		normalVector = cylinder.initNormals2(verticlesVector);
		cylinder.initNormalBuffer(normalVector);
		cylinder.initVerticlesBuffer(verticlesVector);
		this.mModels[this.ARRAY_MODEL_CYLINDER] = cylinder;
		// ghost
		Ghost ghost = new Ghost();
		ghost.setColor(0.8f, 0, 0, 1);
		ghost.init(1, 1, 0.2f);
		ghost.initTextures(gl, this.mContext);
		this.mModels[this.ARRAY_MODEL_GHOST] = ghost;
		// set initial rendered model
		this.mCurrentModel = this.mModels[this.mCurrentModelCounter];
	}
	
	/**
	 * Inits ambient and diffuse light
	 * @param gl
	 */
	protected void initLight(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glShadeModel(GL10.GL_SMOOTH);
		this.mLight = new Light();
		this.mLight.initAmbient(-0.6f, -0.6f, -0.6f, 1.0f);
		this.mLight.initDiffuse(1.0f, 1.0f, 1.0f, 1f);
//		this.mLight.initSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		this.mLight.initPosition(0.0f, 0f, 1f, 0.0f);
		this.mLight.setLight(gl, GL10.GL_LIGHT0);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// clear
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(.5f, 0.5f, .5f, 1.0f);
		this.glStateChanger.checkStates(gl);
		
		// wtf? edit: I know! A back from ortho perspective caused by GUI -
		// - stupid solution.
		float ratio = (float) mDispWidth / mDispHeight;		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1.0f, 1.0f, 2.0f, 100f);  
		// camera
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		mGLCamera.draw(gl);
		
		// scene/model drawing
		gl.glPushMatrix();
			mAngle += 0.8f;
			gl.glRotatef(mAngle, 0, 1f, 0);
			mCurrentModel.draw(gl);
		gl.glPopMatrix();
//		gl.glPushMatrix();
//			mAngle += 0.8f;
//			gl.glTranslatef(0, 2, 0);
//			gl.glRotatef(mAngle, 0, 1f, 0);
//			mModels[ARRAY_MODEL_SPHERE].draw(gl);
//		gl.glPopMatrix();
		
		// GUI
		gl.glDisable(GL10.GL_LIGHTING);
		mGUI.draw(gl);
		gl.glEnable(GL10.GL_LIGHTING);
	}//onDrawFrame()
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, mDispWidth, mDispHeight);
		
		float ratio = (float) mDispWidth / mDispHeight;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1.0f, 1.0f, 2.0f, 20f);  // apply the projection matrix
	}//onSurfaceChanged()
	
	/*
	 * 
	 */
	public void setOrbitCam(float xShift, float yShift, float sensitive){
		mGLCamera.setOrbitCam(xShift, yShift, sensitive);		
	}
	
	public void zoomCam(float zoom){
		mGLCamera.setZoom(zoom);
	}
	
	public void walkCam(float forward){
		mGLCamera.setWalk(forward);
	}
	
	public void strafe(float strafe){
		mGLCamera.setStrafe(strafe);
	}
	
	public void fly(float fly){
		mGLCamera.setFly(fly);
	}
	
	/**
	 * Switch to nest test model
	 */
	public void nextModel() {
		if (++this.mCurrentModelCounter >= this.ALL_OBJECTS) {
			this.mCurrentModelCounter = 0;
		}
		this.mCurrentModel = this.mModels[this.mCurrentModelCounter];
	}
	
	/**
	 * Switchs to previuos test model
	 */
	public void previousModel() {
		if (--this.mCurrentModelCounter < 0) {
			this.mCurrentModelCounter = this.ALL_OBJECTS - 1;
		}
		this.mCurrentModel = this.mModels[this.mCurrentModelCounter];
	}
	
	/**
	 * Enable or disable lighting
	 * 
	 * @param enable
	 */
	public void enableLighting(boolean enable)
	{
		this.glStateChanger.setLighting(enable);
	}
}
