package org.pacman.GUI;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObjectInterface;
import org.pacman.draw.interaction.MovableObject;
import org.pacman.physics.Vector3D;

import android.opengl.GLU;

/**
 * 
 * @author Marcin Rogacki
 *
 */
public class FollowRtsCamera implements DrawableObjectInterface
{

	public Vector3D 	mPos = new Vector3D(0),		//camera position
						mDir = new Vector3D(0),		//camera look at
						mUp = new Vector3D(0);   	//camera up vector
	protected float rotateAroundYAngle = 0f;
	MovableObject followedModel;
	
	public FollowRtsCamera()
	{
		this.mPos.setZ(6);
		this.mUp.setY(1);
	}
	
	/**
	 * 
	 * @param followedModel
	 */
	public void setFollowedModel(MovableObject followedModel) {
		this.followedModel = followedModel;
	}

	/**
	 * 
	 * @param vector
	 */
	public void setPosition(Vector3D vector)
	{
		mPos = vector;
	}
	
	public void setZoom(float zoom)
	{
		mPos = mPos.multiply(zoom);
	}
	
	/**
	 * Will normlize
	 * @param vector
	 */
	public void setUpVector(Vector3D vector) 
	{
		vector = vector.normalize();
		this.mUp = vector;
	}
	
	/**
	 * @param gl
	 */
	public void draw(GL10 gl){
		GLU.gluLookAt(
			gl,
			this.mPos.getX() + this.followedModel.getX(), 
			this.mPos.getY() + this.followedModel.getY(), 
			this.mPos.getZ() + this.followedModel.getZ(), 
			mDir.getX() + this.followedModel.getX(),
			mDir.getY() + this.followedModel.getY(),
			mDir.getZ() + this.followedModel.getZ(), 
			mUp.getX(), mUp.getY(), mUp.getZ()
		);	
	}
}
