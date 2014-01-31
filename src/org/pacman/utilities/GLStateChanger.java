package org.pacman.utilities;

import javax.microedition.khronos.opengles.GL10;

public class GLStateChanger {
	/** 
	 * Indicates if any state was changed. 
	 * If true force to check GL states and re-init them
	 */
	protected boolean stateChanged = false;
	/** enable GL_LIGHTING */
	protected boolean lighting = false;
	
	/**
	 * Checks if some state was changed. If it was - introduces the changes
	 * @param gl
	 */
	public void checkStates(GL10 gl) {
		// nothing has changed
		if (false == this.stateChanged) {
			return;
		}
		
		// re-init all states
		this.applyChange(gl, this.lighting, GL10.GL_LIGHTING);
		
		// set that changes were applied
		this.stateChanged = false;
	}
	
	/**
	 * Enable or disable given state
	 * 
	 * @param gl
	 * @param enable
	 * @param state  e.g. GL10.GL_LIGHTING
	 */
	protected void applyChange(GL10 gl, boolean enable, int state)
	{
		if (enable) {
			gl.glEnable(state);
		} else {
			gl.glDisable(state);
		}
	}
	
	/**
	 * @param lighting the lighting to set
	 */
	public void setLighting(boolean lighting) {
		this.lighting = lighting;
		this.stateChanged = true;
	}
	
	/**
	 * @return the lighting
	 */
	public boolean isLighting() {
		return lighting;
	}
}
