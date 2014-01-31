package org.pacman.draw;

import javax.microedition.khronos.opengles.GL10;
/**
 * Defines common interface for OpenGL models * 
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 */
public interface DrawableObjectInterface {	
	/**
	 * Draws an ready object
	 * 
	 * Should be called after init {@see DrawableObjectInterface.init()}
	 * @param gl Android OpenGL object?
	 */
	public void draw(GL10 gl);
}
