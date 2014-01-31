package org.pacman.draw.interaction;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObjectInterface;

public class MovableObject implements DrawableObjectInterface
{
	/** Object that will be moved */
	protected DrawableObjectInterface drawableObject;
	/** moved position */
	float x =0, y=0, z=0;

	/** disable drawing object */
	private boolean dontDraw = false;
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	/**
	 * Rotates object face in proper direction. 
	 */
	protected float currentAngleX = 0;
	protected float currentAngleY = 0;
	protected float currentAngleZ = 0;
	
	public enum MOVING_DIRECTION {UP, DOWN, LEFT, RIGHT};
	MOVING_DIRECTION currentDirection = MOVING_DIRECTION.DOWN;
	/**
	 * Set object which will be drawn
	 * 
	 * @param drawableObject the object that will be drawn and moved
	 */
	public void setDrawableObject(DrawableObjectInterface drawableObject) {
		this.drawableObject = drawableObject;
	}
	
	/**
	 * Gets drawable object to manipulate it
	 * @return
	 */
	public DrawableObjectInterface getDrawableObject()
	{
		return this.drawableObject;
	}
	
	public void countinousMoving(float value)
	{
		switch (currentDirection) {
			case UP:
				this.z -= value;
				break;
			case DOWN:
				this.z += value;
				break;
			case LEFT:
				this.x -= value;
				break;
			case RIGHT:
				this.x += value;
				break;
		}
	}
	
	public void turnDirection(MOVING_DIRECTION direction)
	{
		this.currentDirection = direction;
		switch (direction) {
			case UP:
				this.currentAngleY = 180;
				break;
			case DOWN:
				this.currentAngleY = 0;
				break;
			case LEFT:
				this.currentAngleY = 270;
				break;
			case RIGHT:
				this.currentAngleY = 90;
				break;
		}
		
	}
	
	/**
	 * Moves drawable object along x axis
	 * 
	 * @param val
	 */
	public void moveAlongXAxis(float val)
	{
		this.x += val;
	}
	
	/**
	 * Moves drawable object along y axis
	 * 
	 * @param val
	 */
	public void moveAlongYAxis(float val)
	{
		this.y += val;
	}
	
	/**
	 * Moves drawable object along z axis
	 * 
	 * @param val
	 */
	public void moveAlongZAxis(float val)
	{
		// z axis is by default pointet to viewer
		this.z += val;
	}

	/**
	 * Helps rotate object to face it in right direction
	 * @param angle
	 */
	public void rotateObjectAroundX(float angle)
	{
		this.currentAngleX = angle;
	}

	/**
	 * Helps rotate object to face it in right direction
	 * @param angle
	 */
	public void rotateObjectAroundY(float angle)
	{
		this.currentAngleY = angle;
	}

	/**
	 * Helps rotate object to face it in right direction
	 * @param angle
	 */
	public void rotateObjectAroundZ(float angle)
	{
		this.currentAngleZ = angle;
	}
	
	public float getRotateX() {
		return this.currentAngleX;
	}
	
	public float getRotateY() {
		return this.currentAngleY;
	}
	
	public float getRotateZ() {
		return this.currentAngleZ;
	}
	
	/**
	 * 
	 */
	@Override
	public void draw(GL10 gl)
	{
		if (dontDraw) {
			return;
		}
		gl.glPushMatrix();
			gl.glTranslatef(this.x, this.y, this.z);
			gl.glRotatef(this.currentAngleX, 1, 0, 0);
			gl.glRotatef(this.currentAngleY, 0, 1, 0);
			gl.glRotatef(this.currentAngleZ, 0, 0, 1);
			this.drawableObject.draw(gl);
		gl.glPopMatrix();
	}

	public void disable()
	{
		this.dontDraw = true;
	}
}
