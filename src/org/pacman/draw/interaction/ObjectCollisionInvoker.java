package org.pacman.draw.interaction;

public interface ObjectCollisionInvoker
{
	public boolean invoke(MovableObject mainObject, 
			MovableObject affectedObject);
}
