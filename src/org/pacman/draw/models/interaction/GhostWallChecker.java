package org.pacman.draw.models.interaction;

import java.util.Random;

import org.pacman.draw.interaction.MovableObject;
import org.pacman.draw.interaction.ObjectCollisionChecker;
import org.pacman.draw.interaction.ObjectCollisionInvoker;
import org.pacman.draw.models.Ghost;
import org.pacman.draw.primitives.Cylinder;
import org.pacman.math.DetectCollision;

import android.graphics.PointF;

public class GhostWallChecker extends ObjectCollisionChecker
{
	protected float mainObjectRadius;
	MovableObject.MOVING_DIRECTION previusDirection;
	MovableObject.MOVING_DIRECTION currentDirection;
    Random random;
    float changedCoutinousMoveX;
    float changedCoutinousMoveZ;
	
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
		this.mainObjectRadius = ((Ghost)pacman.getDrawableObject()).getRadius();
		this.random = new Random();
	}
	
	public void changeDirection()
	{
		this.previusDirection = this.currentDirection;
		
		if (this.currentDirection == MovableObject.MOVING_DIRECTION.UP
			|| this.currentDirection == MovableObject.MOVING_DIRECTION.DOWN) {
			// change
			if (this.random.nextBoolean()) {
				this.currentDirection = MovableObject.MOVING_DIRECTION.LEFT;
			} else {
				this.currentDirection = MovableObject.MOVING_DIRECTION.RIGHT;
			}
		} else {
			if (this.random.nextBoolean()) {
				this.currentDirection = MovableObject.MOVING_DIRECTION.UP;
			} else {
				this.currentDirection = MovableObject.MOVING_DIRECTION.DOWN;
			}
		}
		this.mainObject.turnDirection(this.currentDirection);
		changedCoutinousMoveX = this.mainObject.getX();
		changedCoutinousMoveZ = this.mainObject.getZ();
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
		return this.colisionDetecter.inSquere(pacmanLeft, wallLeftTop,
				wallRightBottom);
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
		return this.colisionDetecter.inSquere(pacmanRight, wallLeftTop,
				wallRightBottom);
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
		return this.colisionDetecter.inSquere(pacmanUp, wallLeftTop,
				wallRightBottom);
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
		return this.colisionDetecter.inSquere(pacmanDown, wallLeftTop,
				wallRightBottom);
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
			
			if (this.meetUp(pacmanUp, wallLeftTop, wallRightBottom)) {
				this.mainObject.setZ(wallRightBottom.y + this.mainObjectRadius);
			}
			if (this.meetDown(pacmanDown, wallLeftTop, wallRightBottom)) {
				this.mainObject.setZ(wallLeftTop.y - this.mainObjectRadius);
			}
			if (this.meetLeft(pacmanLeft, wallLeftTop, wallRightBottom)) {
				this.mainObject.setX(wallRightBottom.x + this.mainObjectRadius);
			}
			if (this.meetRight(pacmanRight, wallLeftTop, wallRightBottom)) {
				this.mainObject.setX(wallLeftTop.x - this.mainObjectRadius);
			}
		}

		if (System.currentTimeMillis()%1100 < 100) {
			if (0 == this.random.nextInt(20)) {
				this.changeDirection();
//				boolean notValid = true;
//				float predict = 0.5f; 
//				while (notValid) {
//					if (this.currentDirection == MovableObject.MOVING_DIRECTION.UP) {
//						pacmanUp.y -= predict;
//						notValid = this.meetUp((pacmanUp) , wallLeftTop, wallRightBottom);
//					} else if (this.currentDirection == MovableObject.MOVING_DIRECTION.DOWN) {
//						pacmanUp.y += predict;
//						notValid = this.meetDown(pacmanDown, wallLeftTop, wallRightBottom);
//					} else if (this.currentDirection == MovableObject.MOVING_DIRECTION.LEFT) {
//						pacmanUp.x -= predict;
//						notValid = this.meetLeft(pacmanLeft, wallLeftTop, wallRightBottom);
//					} else if (this.currentDirection == MovableObject.MOVING_DIRECTION.RIGHT) {
//						pacmanUp.x += predict;
//						notValid = this.meetRight(pacmanRight, wallLeftTop, wallRightBottom);
//					}
//				}
			}
		}
		if (System.currentTimeMillis()%200 < 100) {
			if ((changedCoutinousMoveX - this.mainObject.getX()) < 0.5f
			 	&& (changedCoutinousMoveZ - this.mainObject.getZ()) < 0.5f) {
				this.changeDirection();
			}
		}
	}

}
