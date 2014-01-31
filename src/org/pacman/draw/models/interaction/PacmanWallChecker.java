package org.pacman.draw.models.interaction;

import org.pacman.draw.interaction.MovableObject;
import org.pacman.draw.interaction.ObjectCollisionChecker;
import org.pacman.draw.interaction.ObjectCollisionInvoker;
import org.pacman.draw.models.Pacman;
import org.pacman.draw.primitives.Cylinder;
import org.pacman.math.DetectCollision;

import android.graphics.PointF;

public class PacmanWallChecker extends ObjectCollisionChecker
{
	protected float mainObjectRadius;
	
	/**
	 * Sets important data
	 * 
	 * @param mainObject
	 * @param actionAfterCollision
	 * @param colisionDetecter
	 */
	public void init(MovableObject pacman, 
			ObjectCollisionInvoker actionAfterCollision,
			DetectCollision colisionDetecter)
	{
		super.init(pacman, actionAfterCollision, colisionDetecter);
		this.mainObjectRadius = ((Pacman)pacman.getDrawableObject()).getRadius();
	}
	
	/**
	 * 
	 * @param pacmanLeft
	 * @param wallLeftTop
	 * @param wallRightBottom
	 * @return
	 */
	protected boolean meetLeft(PointF pacmanLeft, PointF wallLeftTop,
			PointF wallRightBottom)
	{
		if (this.colisionDetecter.inSquere(pacmanLeft, wallLeftTop,
				wallRightBottom)) {
			this.mainObject.setX(wallRightBottom.x + this.mainObjectRadius);
		}
		return false;
	}
	
	/**
	 * 
	 * @param pacmanLeft
	 * @param wallLeftTop
	 * @param wallRightBottom
	 * @return
	 */
	protected boolean meetRight(PointF pacmanRight, PointF wallLeftTop,
			PointF wallRightBottom)
	{
		if (this.colisionDetecter.inSquere(pacmanRight, wallLeftTop,
				wallRightBottom)) {
			this.mainObject.setX(wallLeftTop.x - this.mainObjectRadius);
		}
		return false;
	}
	
	/**
	 * 
	 * @param pacmanLeft
	 * @param wallLeftTop
	 * @param wallRightBottom
	 * @return
	 */
	protected boolean meetUp(PointF pacmanUp, PointF wallLeftTop,
			PointF wallRightBottom)
	{
		if (this.colisionDetecter.inSquere(pacmanUp, wallLeftTop,
				wallRightBottom)) {
			this.mainObject.setZ(wallRightBottom.y + this.mainObjectRadius);
		}
		return false;
	}
	
	/**
	 * 
	 * @param pacmanLeft
	 * @param wallLeftTop
	 * @param wallRightBottom
	 * @return
	 */
	protected boolean meetDown(PointF pacmanDown, PointF wallLeftTop,
			PointF wallRightBottom)
	{
		if (this.colisionDetecter.inSquere(pacmanDown, wallLeftTop,
				wallRightBottom)) {
			this.mainObject.setZ(wallLeftTop.y - this.mainObjectRadius);
		}
		return false;
	}
	
	@Override
	public void detect()
	{
		PointF pacmanUp = new PointF(
				this.mainObject.getX(), 
				this.mainObject.getZ() - this.mainObjectRadius);
		PointF pacmanDown = new PointF(
				this.mainObject.getX(), 
				this.mainObject.getZ() + this.mainObjectRadius);
		PointF pacmanLeft = new PointF(
				this.mainObject.getX() - this.mainObjectRadius, 
				this.mainObject.getZ());
		PointF pacmanRight = new PointF(
				this.mainObject.getX() + this.mainObjectRadius, 
				this.mainObject.getZ());

		PointF wallLeftTop = new PointF();
		PointF wallRightBottom = new PointF();
		float wallHalfHeight = 0;
		float wallHalfWidth = 0;
		
		for (MovableObject wall : this.otherObjects)
		{
			wallHalfHeight = ((Cylinder)wall.getDrawableObject()).getHeight()/2;
			wallHalfWidth = ((Cylinder)wall.getDrawableObject()).getRadius();
			if (wall.getRotateZ() != 0) {
				wallLeftTop.x = wall.getX() - wallHalfHeight;
				wallLeftTop.y = wall.getZ() - wallHalfWidth;
				wallRightBottom.x = wall.getX() + wallHalfHeight;
				wallRightBottom.y = wall.getZ() + wallHalfWidth;
			} else {
				wallLeftTop.x = wall.getX() - wallHalfWidth;
				wallLeftTop.y = wall.getZ() - wallHalfHeight;
				wallRightBottom.x = wall.getX() + wallHalfWidth;
				wallRightBottom.y = wall.getZ() + wallHalfHeight;
			}
			
			this.meetUp(pacmanUp, wallLeftTop, wallRightBottom);
			this.meetDown(pacmanDown, wallLeftTop, wallRightBottom);
			this.meetLeft(pacmanLeft, wallLeftTop, wallRightBottom);
			this.meetRight(pacmanRight, wallLeftTop, wallRightBottom);
		}

	}

}
