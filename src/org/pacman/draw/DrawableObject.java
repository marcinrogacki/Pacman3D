package org.pacman.draw;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.utilities.BufferMaker;

/**
 * Defines common interface for OpenGL models
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 */
public abstract class DrawableObject implements DrawableObjectInterface
{
	/** Buffer for points. This buffer is used to drawing model */
	protected FloatBuffer verticesBuffer;
	
	/** Buffer for teture UV coords */
	protected FloatBuffer texturesUVBuffer;
	
	/** Normals */
	protected FloatBuffer normalBuffer;

	/** Flat coloring */
	protected float mR = 1.0f, mG = 1.0f, mB = 1.0f, mAlpha = 1.0f;
	
	/**
	 * Sets color of sphere.
	 * 
	 * @param r		Amount of red from 0 to 1.
	 * @param g		Amount of green from 0 to 1.
	 * @param b		Amount of blue from 0 to 1.
	 * @param alpha Amount of alpha from 0 to 1.
	 */
	public void setColor(float r, float g, float b, float alpha) {
		this.mR = r;
		this.mG = g;
		this.mB = b;
		this.mAlpha = alpha;
	}
	
	/**
	 * Creates buffer from vector
	 * @param verticlesCoords
	 */
	public void initVerticlesBuffer(float[] verticlesCoords)
	{
		this.verticesBuffer = BufferMaker.createFloat(verticlesCoords);
	}
	
	/**
	 * Creates buffer from vector
	 * @param verticlesCoords
	 */
	public void initTexturesBuffer(float[] texturesUV)
	{
		this.texturesUVBuffer = BufferMaker.createFloat(texturesUV);
	}
	
	/**
	 * Creates buffer from vector
	 * @param normalCoords
	 */
	public void initNormalBuffer(float[] normalCoords)
	{
		this.normalBuffer = BufferMaker.createFloat(normalCoords);
	}
	
	public float[] mergeVectors(float[] source, float[] additional)
	{
		float[] merged = new float[source.length + additional.length];
		System.arraycopy(source, 0, merged, 0, source.length);
		System.arraycopy(additional, 0, merged, source.length, additional.length);
		return merged;
	}
	
	/**
	 * Draws an ready object
	 * 
	 * Should be called after init {@see DrawableObjectInterface.init()}
	 * @param gl Android OpenGL object?
	 */
	public abstract void draw(GL10 gl);
}
