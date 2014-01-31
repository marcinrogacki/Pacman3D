package org.pacman.draw.interaction;

import java.util.ArrayList;

import org.pacman.math.DetectCollision;

/**
 * Defines structure and interface to detects if specified objects were collided
 * 
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 *
 */
public abstract class ObjectCollisionChecker
{
	/** Your hero of the class */
	protected MovableObject mainObject;
	/** 
	 * Opponents
	 * Objects that will be iterated and checked if disturbs your hero
	 */
	protected ArrayList<MovableObject> otherObjects;
	/** What should be done to hero or other object if they will meet */
	protected ObjectCollisionInvoker actionAfterCollision;
	/** Usefull for detecting collisions */
	protected DetectCollision colisionDetecter;
	
	/**
	 * Block default
	 */
	protected ObjectCollisionChecker() {}
	
	/**
	 * Sets important data
	 * 
	 * @param mainObject
	 * @param actionAfterCollision
	 * @param colisionDetecter
	 */
	public void init(MovableObject mainObject, 
			ObjectCollisionInvoker actionAfterCollision,
			DetectCollision colisionDetecter)
	{
		this.otherObjects = new ArrayList<MovableObject>();
		this.mainObject = mainObject;
		this.actionAfterCollision = actionAfterCollision;
		this.colisionDetecter = colisionDetecter;
	}
	
	/**
	 * Adds object to check if it collide with main specified in constructor
	 * @param object
	 */
	public void addObjectToCheck(MovableObject object)
	{
		this.otherObjects.add(object);
	}
	
	/**
	 * Checks all objects if they collide with main and if do invokes an action
	 * 
	 * This method should iterate added objects, checks if they collide with 
	 * main by custom collision detecter and invokes action after collide event
	 */
	public abstract void detect();
}
