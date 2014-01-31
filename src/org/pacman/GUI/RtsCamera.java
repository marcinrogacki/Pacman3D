package org.pacman.GUI;

import org.pacman.physics.Vector3D;

/**
 * 
 * @author Marcin Rogacki
 *
 */
public class RtsCamera extends GLCamera
{
	protected float rotateAroundYAngle = 0f;
	
	/**
	 * 
	 * @param move
	 */
	@Override
	public void setWalk(float move)
	{
		Vector3D moved = new Vector3D(0, 0, -move);
		mPos = mPos.add(moved);
		mDir = mDir.add(moved);
	}
	
	/**
	 * Warrnig! This wont work properly!
	 * @param angle
	 */
	public void rotateAroundY(float angle)
	{
		this.rotateAroundYAngle += angle;
		float radius = this.mPos.distance(this.mDir);
		float x = radius * (float)Math.cos(this.rotateAroundYAngle);
		float z = radius * (float)Math.sin(-this.rotateAroundYAngle);
//		this.mPos.move(new Vector3D(x, 0, z));
		this.mPos.setX(x);
		this.mPos.setY(z);
	}
}
