package org.pacman.draw.models.interaction;

import org.pacman.draw.interaction.MovableObject;
import org.pacman.draw.interaction.ObjectCollisionChecker;
import org.pacman.draw.interaction.ObjectCollisionInvoker;
import org.pacman.draw.models.Pacman;
import org.pacman.draw.primitives.Sphere;
import org.pacman.math.DetectCollision;

import android.graphics.PointF;

public class PacmanPointsChecker extends ObjectCollisionChecker
{
	protected float pacmanRadius;
	protected float pointRadius;
	
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
		this.pacmanRadius = ((Pacman)pacman.getDrawableObject()).getRadius();
	}
	
	@Override
	public void addObjectToCheck(MovableObject object) {
		this.pointRadius = ((Sphere)object.getDrawableObject()).getRadius()/2;
		super.addObjectToCheck(object);
	}
	
	@Override
	public void detect()
	{
		PointF pacmanCenter = new PointF(this.mainObject.getX(), 
				this.mainObject.getZ());
		PointF pointCenter = new PointF();
		for (MovableObject point : this.otherObjects)
		{
			pointCenter.set(point.getX(), point.getZ());
			if (this.colisionDetecter.circleTouchsCircle(pacmanCenter,
					 pointCenter, this.pacmanRadius, this.pointRadius))
			{
				point.disable();
			}
		}

	}

}
