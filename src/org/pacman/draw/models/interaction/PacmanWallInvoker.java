package org.pacman.draw.models.interaction;

import org.pacman.draw.interaction.MovableObject;
import org.pacman.draw.interaction.ObjectCollisionInvoker;

public class PacmanWallInvoker implements ObjectCollisionInvoker
{
	protected void adjustAxisX(MovableObject pacman, MovableObject wall)
	{
//		float mainObjectRadius = ((Pacman)pacman.getDrawableObject()).getRadius();
//		float wallRadius = ((Cylinder)wall.getDrawableObject()).getRadius();
//		float wallHalfHeight = ((Cylinder)wall.getDrawableObject()).getHeight()/2;
//		if () {
//			
//		}
		
	}
	
	
	@Override
	public boolean invoke(MovableObject pacman, MovableObject wall)
	{
		
		
//		float pacmanLeft = pacman.getX() - mainObjectRadius;
//		float pacmanRight = pacman.getX() + mainObjectRadius;
//		float wallLeft = pacman.getX() - wallRadius;
//		float wallRight = pacman.getX() + wallRadius;
//		
//		
//		
//		
//		float pacmanUp = pacman.getZ() - mainObjectRadius;
//		float pacmanDown = pacman.getZ() + mainObjectRadius;

		return false;
	}

}
