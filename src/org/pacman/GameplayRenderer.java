package org.pacman;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.pacman.GUI.FollowRtsCamera;
import org.pacman.GUI.FreeLook;
import org.pacman.draw.effects.Light;
import org.pacman.draw.interaction.MovableObject;
import org.pacman.draw.interaction.ObjectCollisionChecker;
import org.pacman.draw.models.Ghost;
import org.pacman.draw.models.OpenGLAxes;
import org.pacman.draw.models.Pacman;
import org.pacman.draw.models.interaction.GhostWallChecker;
import org.pacman.draw.models.interaction.PacmanPointsChecker;
import org.pacman.draw.models.interaction.PacmanWallChecker;
import org.pacman.draw.primitives.Cylinder;
import org.pacman.draw.primitives.Sphere;
import org.pacman.math.DetectCollision;
import org.pacman.physics.Vector3D;
import org.pacman.utilities.GLStateChanger;


import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.view.WindowManager;

/**
 * Renderer for testing models
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 */
public class GameplayRenderer implements Renderer
{
	/** 
	 * On fly state changer. Injected in constructor. 
	 * Allows manage GL states outside glDraw where gl context is avaliable.
	 */
	protected GLStateChanger glStateChanger;
	protected int 		mDispWidth, mDispHeight;
	protected float 	mAngle;
	protected Context 	mContext;
	protected FreeLook 	mGUI;
	// light
	protected Light 	mLight;
	protected Light 	mLight2;
	// camera
//	protected MovableObject camera;
	protected FollowRtsCamera camera;
	// objects
	protected MovableObject pacman;
	protected MovableObject redGhost;
	protected MovableObject orangeGhost;
	protected MovableObject blueGhost;
	protected MovableObject pinkGhost;
	protected MovableObject[] walls = new MovableObject[51];
	protected MovableObject[] points = new MovableObject[170];
	// detect collision
	protected ObjectCollisionChecker pacmanWallChecker;
	protected ObjectCollisionChecker pacmanPointsChecker;
	
	protected ObjectCollisionChecker ghostRedWallChecker;
	protected ObjectCollisionChecker ghostOrangeWallChecker;
	protected ObjectCollisionChecker ghostBlueWallChecker;
	protected ObjectCollisionChecker ghostPinkWallChecker;
	// helper
	protected OpenGLAxes axes;
	
	public void initPoints()
	{
		float coordsX[] = {
			0, 1.5f, 3f, 4.5f, 6f, 7.5f, 9f, 10.5f, 12, 13.5f, 
			1.5f, 13.5f,
			1.5f, 3f, 4.5f, 6f, 9f, 10.5f, 12, 13.5f, 
			6f, 9f, 12,  
			1.5f, 3f, 4.5f, 6f, 7.5f, 9f, 12, 13.5f, 
			1.5f, 9f, 13.5f, 
			1.5f, 3f, 4.5f, 6f, 7.5f, 9f, 10.5f, 12, 13.5f, 
			9f, 9f, 9f, 9f, 9f, 9f, 9f, // special vertical
			1.5f, 3f, 4.5f, 6f, 9f, 10.5f, 12, 13.5f, 
			6f, 9f, 13.5f, 
			0, 1.5f, 3f, 4.5f, 6f, 7.5f, 9f, 10.5f, 12, 13.5f, 
			1.5f, 9f, 13.5f, 
			1.5f, 9f, 13.5f, 
			1.5f, 3f, 4.5f, 6f, 7.5f, 9f, 10.5f, 12, 13.5f, 
		};
		float coordsZ[] = {
			13.5f, 13.5f, 13.5f, 13.5f, 13.5f, 13.5f, 13.5f, 13.5f, 13.5f, 13.5f,
			12f, 12f,  
			10.5f, 10.5f, 10.5f, 10.5f, 10.5f, 10.5f, 10.5f, 10.5f,
			9, 9, 9,
			7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 
			6, 6, 6,
			4.5f, 4.5f, 4.5f, 4.5f, 4.5f, 4.5f, 4.5f, 4.5f, 4.5f,
			3, 1.5f, 0, -1.5f, -3, -4.5f, -6, // special horizontal 
			-7.5f, -7.5f, -7.5f, -7.5f, -7.5f, -7.5f, -7.5f, -7.5f, 
			-9, -9, -9, 
			-10.5f, -10.5f, -10.5f, -10.5f, -10.5f, -10.5f, -10.5f, -10.5f, -10.5f, -10.5f, 
			-12, -12, -12, 
			-13.5f, -13.5f, -13.5f,
//			-14.5f, -14.5f, -14.5f, -14.5f, -14.5f, -14.5f, -14.5f, -14.5f, -14.5f, -14.5f, 
			-15, -15, -15, -15, -15, -15, -15, -15, -15, -15, 
		};
		// init drawable sphere
		Sphere sphere = new Sphere();
		float verticles[];
		byte details = 4;
		verticles = sphere.initVertices(0.5f, details, details, details, details);
		sphere.initVerticlesBuffer(verticles);
		verticles = sphere.initNormals(verticles);
		sphere.initNormalBuffer(verticles);
		
		int pointsCounter = 0;
		int length = coordsX.length;
		for (int i=0; i < length; i++) {
			this.points[pointsCounter] = new MovableObject();
			this.points[pointsCounter].setDrawableObject(sphere);
			this.points[pointsCounter].moveAlongXAxis(coordsX[i]);
			this.points[pointsCounter].moveAlongZAxis(coordsZ[i]);
			pointsCounter++;
			if (0.01f > coordsX[i] && -0.01f < coordsX[i]) {
				continue;
			}
			this.points[pointsCounter] = new MovableObject();
			this.points[pointsCounter].setDrawableObject(sphere);
			this.points[pointsCounter].moveAlongXAxis(-coordsX[i]);
			this.points[pointsCounter].moveAlongZAxis(coordsZ[i]);
			pointsCounter++;
		}
	}
	
	/**
	 * 
	 */
	protected void initWalls()
	{
		float[] verticles;
		Cylinder drawableWall;
		int wallCounter = 0;
		int wallDetails = 5;
		
		// height, botom/top, left/right
		float horizontalWalls[] = {
			31f,  15, 0,			// bottom 1
			10f,  12f,  7.5f,		// botom  2 L
			10f,  12f, -7.5f,		// botom  2 L
			9f, 9f,  0f,			// botom  3 T
			2f, 9f,  13.5f,			// botom  3 SHORT
			2f, 9f,  -13.5f,		// botom  3 SHORT
			5f,  6f, 5f,			// botom  4 LINE
			5f,  6f, -5f,			// botom  4 LINE
			2.5f,  6f, 11.5f,		// botom  4 L
			2.5f,  6f, -11.5f,		// botom  4 L
			9f,  3f,  0f,			// botom  5 T2
			9f,  0f,  0f,			// botom  5 BOX
			9f,  -3f,  0f,			// botom  5 BOX
			5.5f,  3f,  12.5f,			// botom  5 cut
			5.5f,  3f,  -12.5f,			// botom  5 cut
			9f,  -9f,  0f,				// botom  5 T3
			4f,  -6f, 4.5f,				// botom  4 TT
			4f,  -6f, -4.5f,			// botom  4 TT
			5.7f,  -6f,  12.5f,			// botom  5 cut UP
			5.7f,  -6f,  -12.5f,		// botom  5 cut UP
			3f,  -9f, 11.5f,		// botom  4 short 2
			3f,  -9f, -11.5f,		// botom  4 short 2
			3f,  -12f, 11.5f,		// botom  4 double 1_1
			3f,  -13f, 11.5f,		// botom  4 double 1_1
			3f,  -12f, -11.5f,		// botom  4 double 1_2
			3f,  -13f, -11.5f,		// botom  4 double 1_2
			//
			4.5f,  -12f, 5f,		// botom  4 double 2_1
			4.5f,  -13f, 5f,		// botom  4 double 2_1
			4.5f,  -12f, -5f,		// botom  4 double 2_2
			4.5f,  -13f, -5f,		// botom  4 double 2_2
			31f, -16, 0, 			// bottom x (top)
		};
		int length = horizontalWalls.length;
		for (int i=0; i < length; i = i + 3, wallCounter++) {
			drawableWall = new Cylinder();
			verticles = drawableWall.initVertices(0.5f, horizontalWalls[i], wallDetails, 1);
			drawableWall.initVerticlesBuffer(verticles);
			verticles = drawableWall.initNormals2(verticles);		
			drawableWall.initNormalBuffer(verticles);
			
			this.walls[wallCounter] = new MovableObject();
	        this.walls[wallCounter].setDrawableObject(drawableWall);
	        this.walls[wallCounter].rotateObjectAroundZ(90);
	        this.walls[wallCounter].moveAlongZAxis(horizontalWalls[i+1]);
	        this.walls[wallCounter].moveAlongXAxis(horizontalWalls[i+2]);
	        this.walls[wallCounter].moveAlongYAxis(-0.3f);
		}
		horizontalWalls = null;

		// lenght, left/right, top/bottom
		float verticalWalls[] = {
			12f, 15f,  9f, 	// cut 
			12f, -15f, 9f,    // cut
			3,  0, 11,      // T
			3.5f,  7, 10f,  // reverse T
			3.5f, -7, 10f,  // reverse T
			4f,  10f, 7.5f,  // L
			4f, -10f, 7.5f,  // L
			3,  0, 5, 		 // T2
			2.5f,  4, -1.5f, 	 // box
			2.5f,  -4, -1.5f,   // box
			4f,  7, 1.5f,  	// SHORT BY BOX
			4f, -7, 1.5f,  	// SHORT BY BOX
			6f,  7, -6f,  	// LONG BY BOX TT
			6f, -7, -6f,  	// LONG BY BOX TT
			3,  0, -7, 		 // T3
			9f,  10f, -1.5f,  // cut LONG
			9f, -10f, -1.5f,  // cut LONG	
			4f,  0, -13.5f, 		 // touuge
			
			9.7f, 15.4f,  -11f, 	// cut  3
			9.7f, -15.4f, -11f,    // cut 3		
		};
		length = verticalWalls.length;
		for (int i=0; i < length; i = i + 3, wallCounter++) {
			drawableWall = new Cylinder();
			verticles = drawableWall.initVertices(0.5f, verticalWalls[i], wallDetails, 1);
			drawableWall.initVerticlesBuffer(verticles);
			verticles = drawableWall.initNormals3(verticles);		
			drawableWall.initNormalBuffer(verticles);
			
			this.walls[wallCounter] = new MovableObject();
	        this.walls[wallCounter].setDrawableObject(drawableWall);
	        this.walls[wallCounter].rotateObjectAroundX(90);
	        this.walls[wallCounter].moveAlongXAxis(verticalWalls[i+1]);
	        this.walls[wallCounter].moveAlongZAxis(verticalWalls[i+2]);
	        this.walls[wallCounter].moveAlongYAxis(-0.3f);
		}
	}
	
	/**
	 * Fetchs contex and display size
	 * @param ctx
	 */
	public GameplayRenderer(Context ctx, GLStateChanger glStateChanger){
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
		this.camera = new FollowRtsCamera();
		this.camera.setPosition(new Vector3D(0, 6, 10));
//		this.camera.setPosition(new Vector3D(0, 6f, 10f));
//		this.camera.setOrbitCam(0, -90, 0.4f);
		// GUI
		float displayRatio = (float)this.mDispWidth/this.mDispHeight;
		this.mGUI = new FreeLook(mContext.getResources(), gl, displayRatio);
		// light
		this.initLight(gl);
		// models
		this.initModels(gl);
		this.initWalls();
		this.initPoints();
//		this.axes.init(4);
		// helper model
		this.axes = new OpenGLAxes();
		// detect collision
		this.initPacmanWallsDetecCollision();
		this.initPacmanPointsDetecCollision();
		this.initGhostsWallsDetecCollision();
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
	 * 
	 * @param gl
	 */
	protected void initModels(GL10 gl)
	{
		// pacman
		Pacman pacman = this.createPacman(gl, 1.0f);
		this.pacman = new MovableObject();
		this.pacman.setDrawableObject(pacman);
		this.pacman.setZ(7.5f);
//		this.pacman.setZ(-10);
		this.camera.setFollowedModel(this.pacman);
		// Ghosts
		Ghost ghost = this.createGhost(gl, 1.0f, 1.0f, 0.2f);
		// red ghost
		this.redGhost = new MovableObject();
		this.redGhost.setDrawableObject(ghost);
		this.redGhost.moveAlongZAxis(-5.0f);
		// red ghost
		this.orangeGhost = new MovableObject();
		this.orangeGhost.setDrawableObject(ghost);
		this.orangeGhost.moveAlongZAxis(-7.0f);
		this.orangeGhost.moveAlongXAxis(-3.0f);
		// red ghost
		this.blueGhost = new MovableObject();
		this.blueGhost.setDrawableObject(ghost);
		this.blueGhost.moveAlongZAxis(-6.0f);
		this.blueGhost.moveAlongXAxis(4.0f);
		// red ghost
		this.pinkGhost = new MovableObject();
		this.pinkGhost.setDrawableObject(ghost);
		this.pinkGhost.moveAlongZAxis(-4.0f);
		this.pinkGhost.moveAlongXAxis(-7.0f);
	}
	
	protected void initPacmanWallsDetecCollision()
	{
		this.pacmanWallChecker = new PacmanWallChecker();
		this.pacmanWallChecker.init(this.pacman, null, new DetectCollision());
		for (MovableObject wall: walls) {
			this.pacmanWallChecker.addObjectToCheck(wall);
		}
	}
	
	protected void initGhostsWallsDetecCollision()
	{
		this.ghostRedWallChecker = new GhostWallChecker();
		this.ghostRedWallChecker.init(this.redGhost, null, new DetectCollision());
		for (MovableObject wall: walls) {
			this.ghostRedWallChecker.addObjectToCheck(wall);
		}
		
		this.ghostOrangeWallChecker = new GhostWallChecker();
		this.ghostOrangeWallChecker.init(this.orangeGhost, null, new DetectCollision());
		for (MovableObject wall: walls) {
			this.ghostOrangeWallChecker.addObjectToCheck(wall);
		}
		
		this.ghostBlueWallChecker = new GhostWallChecker();
		this.ghostBlueWallChecker.init(this.blueGhost, null, new DetectCollision());
		for (MovableObject wall: walls) {
			this.ghostBlueWallChecker.addObjectToCheck(wall);
		}
		
		this.ghostPinkWallChecker = new GhostWallChecker();
		this.ghostPinkWallChecker.init(this.pinkGhost, null, new DetectCollision());
		for (MovableObject wall: walls) {
			this.ghostPinkWallChecker.addObjectToCheck(wall);
		}
	}
	
	protected void initPacmanPointsDetecCollision()
	{
		this.pacmanPointsChecker = new PacmanPointsChecker();
		this.pacmanPointsChecker.init(this.pacman, null, new DetectCollision());
		for (MovableObject point: points) {
			this.pacmanPointsChecker.addObjectToCheck(point);
		}
	}
	
	/**
	 * Creates pacman model
	 * 
	 * @param gl Gl context
	 * @param radius Size of the pacman
	 * @return
	 */
	protected Pacman createPacman(GL10 gl, float radius) 
	{
		Pacman pacman = new Pacman();
		pacman.init(radius);
		pacman.initTextures(gl, this.mContext);
		return pacman;
	}
	
	/**
	 * 
	 * @param gl
	 * @param radius
	 * @param height
	 * @param eyeRadius
	 * @return
	 */
	protected Ghost createGhost(GL10 gl, float radius, float height,
			float eyeRadius)
	{
		Ghost ghost = new Ghost();
		ghost.init(radius, height, eyeRadius);
		ghost.initTextures(gl, this.mContext);
		return ghost;
	}
	
	/**
	 * Inits ambient and diffuse light
	 * @param gl
	 */
	protected void initLight(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glShadeModel(GL10.GL_SMOOTH);
		this.mLight = new Light();
		this.mLight.initAmbient(-0.2f, -0.2f, -0.2f, 1.0f);
		this.mLight.initDiffuse(1.f, 1.0f, 1.0f, 1f);
		this.mLight.initSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		this.mLight.initPosition(0.0f, 3f, 20f, 0.0f);
		this.mLight.setLight(gl, GL10.GL_LIGHT0);
		
		this.mLight2 = new Light();
		this.mLight2.initAmbient(-0.1f, -0.1f, -0.1f, 1.0f);
		this.mLight2.initDiffuse(1f, 1f, 1f, 1f);
		this.mLight2.initSpecular(1f, 1f, 1f, 1f);
		this.mLight2.initPosition(0f, 2f, 13.5f, 0.0f);
		this.mLight2.setLight(gl, GL10.GL_LIGHT1);
//		gl.glDisable(GL10.GL_LIGHT1);
	}
	
	/**
	 * 
	 */
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// clear
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0f, 0f, 0f, 1.0f);
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
		
		this.camera.draw(gl);
		gl.glEnable(GL10.GL_LIGHT0);
		// pacman
		this.pacman.draw(gl);
		// red ghost
		gl.glColor4f(1, 0, 0, 1);
		this.redGhost.countinousMoving(0.8f);
		this.redGhost.draw(gl);
		// orange ghost
		gl.glColor4f(1, 0.42f, 0f, 1);
		this.orangeGhost.countinousMoving(0.8f);
		this.orangeGhost.draw(gl);
		// blue ghost
		gl.glColor4f(0, 0, 1, 1);
		this.blueGhost.countinousMoving(0.8f);
		this.blueGhost.draw(gl);
		// pink ghost
		gl.glColor4f(1, 0.42f, 0.42f, 1);
		this.pinkGhost.countinousMoving(0.8f);
		this.pinkGhost.draw(gl);
		

		gl.glColor4f(0.34f, 0.34f, 1f, 1);
		for (MovableObject wall : this.walls) {
			wall.draw(gl);
		}
		gl.glDisable(GL10.GL_LIGHT0);
		
		gl.glEnable(GL10.GL_LIGHT1);
		gl.glColor4f(1f, 1f, 0.34f, 1f);
		for (MovableObject point : this.points) {
			point.draw(gl);
		}
		gl.glDisable(GL10.GL_LIGHT1);
		
		this.pacmanWallChecker.detect();
		this.pacmanPointsChecker.detect();
		this.ghostRedWallChecker.detect();
		this.ghostOrangeWallChecker.detect();
		this.ghostBlueWallChecker.detect();
		this.ghostPinkWallChecker.detect();
		
		gl.glColor4f(1f, 1f, 1f, 1);
		// GUI
		gl.glDisable(GL10.GL_LIGHTING);
//		this.axes.draw(gl);
		this.mGUI.draw(gl);
		this.glStateChanger.setLighting(this.glStateChanger.isLighting());
	} //onDrawFrame()
	
	/**
	 * 
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		gl.glViewport(0, 0, mDispWidth, mDispHeight);
		
		float ratio = (float) mDispWidth / mDispHeight;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1.0f, 1.0f, 2.0f, 40f);  // apply the projection matrix
	}//onSurfaceChanged()
	
	/*
	 * 
	 */
	public void rotateCamAroundY(float angle){
//		GLCamera cam = (GLCamera)this.camera.getDrawableObject();
//		cam.rotateAroundY(angle);
//		this.camera.rotateAroundY(angle);
	}
	
	public void zoomCam(float zoom){
//		GLCamera cam = (GLCamera)this.camera.getDrawableObject();
//		cam.setZoom(zoom);
		this.camera.setZoom(zoom);
	}
	
	public void fly(float fly){
//		GLCamera cam = (GLCamera)this.camera.getDrawableObject();
//		cam.setFly(fly);
	}
	
	
	
	/**
	 * 
	 * @param value
	 */
	public void moveAlongXAxis(float value){
		this.pacman.moveAlongXAxis(value);
		if (value < 0) {
			this.pacman.rotateObjectAroundY(0);
		} else {
			this.pacman.rotateObjectAroundY(180);
		}
	}
	
	/**
	 * 
	 * @param value
	 */
	public void moveAlongZAxis(float value){
		this.pacman.moveAlongZAxis(-value);
		if (value < 0) {
			this.pacman.rotateObjectAroundY(90);
		} else {
			this.pacman.rotateObjectAroundY(270);
		}
	}
	
	/**
	 * 
	 * @param value
	 */
	public void moveCamAlongXAxis(float value){
//		this.camera.moveAlongXAxis(-value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void moveCamAlongZAxis(float value){
//		this.camera.moveAlongZAxis(value);
	}
	
	public void camSetPosition(Vector3D vector) {
		this.camera.setPosition(vector);
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
