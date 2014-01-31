package org.pacman.physics;

public class Point3Df {
	private float 	mX = 0, 
					mY = 0, 
					mZ = 0;

	/**
	 * Sets the x coord
	 * 
	 * @param x The x to set
	 */
	public void setX(float x) {
		this.mX = x;
	}

	/**
	 * Gets the x coord.
	 * 
	 * @return The x coord.
	 */
	public float getX() {
		return this.mX;
	}
	
	/**
	 * Sets the y coord
	 * 
	 * @param y The y to set
	 */
	public void setY(float y) {
		this.mY = y;
	}

	/**
	 * Gets the y coord.
	 * 
	 * @return The y coord.
	 */
	public float getY() {
		return this.mY;
	}
	
	/**
	 * Sets the z coord
	 * 
	 * @param z The z to set
	 */
	public void setZ(float z) {
		this.mZ = z;
	}

	/**
	 * Gets the z coord.
	 * 
	 * @return The z coord.
	 */
	public float getZ() {
		return this.mZ;
	}
	
	/**
	 * Sets all coords.
	 * 
	 * @param x The x value.
	 * @param y The y value.
	 * @param z The z value.
	 */
	public void setAll(float x, float y, float z){
		this.mX = x;
		this.mY = y;
		this.mZ = z;
	}
}
