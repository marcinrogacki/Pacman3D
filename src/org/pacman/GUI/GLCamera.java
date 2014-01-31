package org.pacman.GUI;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.physics.Vector3D;

import android.opengl.GLU;

/**
 * 
 * @author Marcin Rogacki
 *
 */
public class GLCamera {
	public Vector3D 	mPos = new Vector3D(0),		//camera position
						mDir = new Vector3D(0),		//camera look at   
						mUp = new Vector3D(0),		//camera up vector
						mRight = new Vector3D(0);	//camera right vector
//	float mLen = 0;
	private final float RADIAN_FACTORY = 1.57079632679f;
	private float mAngleX = 0f;
	private float mAngleY = 0f;
	
	
	/**
	 * 
	 */
	public GLCamera(){
		mPos.setZ(6f);
		mUp.setY(1f);		
		mRight.setX(1f);
	}
	
	/**
	 * Sets up the up vector of camera.
	 */
	private void setUpVector() {
		Vector3D forward = mPos.normalize();
		Vector3D right = this.mUp.crossProduct(forward);
		this.mUp = forward.crossProduct(right);
	}
	/**
	 * 
	 * @param vecCenter
	 * @param vecToShift
	 * @param angleX
	 * @param angleY
	 * @return
	 */
	private void setShift(float angleX, float angleY) {
		// information of Spherical coordinate system:
		// http://en.wikipedia.org/wiki/Spherical_coordinate_system
		// http://informatyka.wroc.pl/node/698?page=0,2
		float camDist = this.mPos.distance(this.mDir);
		double z = camDist * Math.sin(angleX + RADIAN_FACTORY) 
				 * Math.cos(angleY); 
		double x = camDist * Math.sin(angleX + RADIAN_FACTORY) 
				 * Math.sin(angleY);
		double y = camDist * Math.cos(angleX + RADIAN_FACTORY);
		
		/**
		 * #calcShift - rotation at the center of the coordinate system
		 */
		Vector3D calcShift = new Vector3D((float)x, (float)y, (float)z);
		// add difference between center of coordinate system and camera look 
		// center. The calcShift was calculated with distance of center cam and
		// posision cam)
		this.mPos = calcShift.add(this.mDir);
	}
	
	/**
	 * 
	 * @param xShift
	 * @param yShift
	 * @param sensitive
	 */
	public void setOrbitCam(float xShift, float yShift, float sensitive){		
		xShift *= sensitive;
		yShift *= sensitive;
		
		//Shift along y axis on display is a rotation around the axis x in OpenGL
		mAngleX += (float) Math.toRadians(yShift); 
		//Shift along x axis on display is a rotation around the axis y in OpenGL
		mAngleY += (float) Math.toRadians(xShift); 
		
		this.setShift(mAngleX, mAngleY);		
		this.setUpVector();
	}
	
	/**
	 * 
	 * @param zoom
	 */
	public void setZoom(float zoom){
		mPos = mPos.multiply(zoom);
	}
	
	/**
	 * 
	 * @param move
	 */
	public void setWalk(float move){	
		//code from: http://www.opengl.org/sdk/samples/CodeColony/camera.php
		Vector3D distance = mPos.subtract(mDir);
		distance = distance.multiply(-move);
		mPos = mPos.add(distance);
		mDir = mDir.add(distance);
	}
	
	/**
	 * 
	 * @param strafe
	 */
	public void setStrafe(float strafe){
		//code from: http://www.opengl.org/sdk/samples/CodeColony/camera.php
		Vector3D distance = mRight.multiply(strafe);
		mPos = mPos.add(distance);
		mDir = mDir.add(distance);
	}
	
	/**
	 * 
	 * @param upOrDown
	 */
	public void setFly(float upOrDown){
		//code from: http://www.opengl.org/sdk/samples/CodeColony/camera.php
		Vector3D distance = mUp.multiply(upOrDown);
		mPos = mPos.add(distance);
		mDir = mDir.add(distance); 
	}
	
	/**
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl){
		GLU.gluLookAt(gl, mPos.getX(), mPos.getY(), mPos.getZ(), 
					  mDir.getX(), mDir.getY(), mDir.getZ(), 
					  mUp.getX(), mUp.getY(), mUp.getZ());	
	}
}
