package org.pacman.draw.primitives;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObject;
import org.pacman.physics.Vector3D;

/**
 * 
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 */
public class Sphere extends DrawableObject
{
	/** Sphere radius */
	protected float mRadius;
	
	/** Angle between each stack (angle from Z axis to XY plane)*/
	protected float mStacksInterval;
	
	/** Angle between each slice (angle from X axis to YZ plane) */
	protected float mSlicesInterval;
	
	/** Sphere color */
	protected float mR = 1.0f, mG = 1.0f, mB = 1.0f, mAlpha = 1.0f;
	
	/**
	 *  This might cheating normal calculations. E.g. if we move center to
	 *  bottom then light will be more focused on bottom sphere. 
	 */
	protected Vector3D centerForNormals = new Vector3D(0, 0, 0);
	
	/**
	 *  This cheats normal calculations. 
	 *  
	 *  E.g. if we move center to bottom instead default (0,0,0) then light 
	 *  will be more focused on bottom sphere. This helps set properly lighting
	 *  during bonding with others object e.g. cylinder.
	 * 
	 * @param point New center for normal calculations
	 */
	public void setNewCenterForNormals(Vector3D point)
	{
		this.centerForNormals = point;
	}
	
	/**
	 * Adds a point of sphere to array.
	 * 
	 * Based on spherical coordinate system: 
	 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
	 * 
	 * @param vec   Vector where point will be added. Out parameter. 
	 * @param index Actual index of vector.
	 * @param theta Angle between Z axis and XZ plane
	 * @param phi   Angle between X axis and YZ plane
	 * @return 		Actual index of vector.
	 */
	protected int addPoint(float vec[], int index, float theta, float phi)
	{
		float radius = this.mRadius;
		// x coord
		vec[index++] = 	(float) (radius * Math.sin(theta) * Math.sin(phi));
		// y coord
		vec[index++] = (float) (radius * Math.cos(theta) );
		// z coord
		vec[index++] = (float) (radius * Math.sin(theta) * Math.cos(phi));
		
		return index;
	}
	
	/**
	 * Adds into array the points of square (2 triangles). 
	 *
	 * Squre is a tile of sphere.
	 * 
	 * @param vec 	Vector where points will be added. Out parameter.
	 * @param index Actual index of vector.
	 * @param stack Currently drawing stack
	 * @param slice Currently drawing slice
	 * @return 		Actual index of vector.
	 * @see   		#addPoint(float[], int, int, int)
	 */
	protected int addSquere(float vec[], int index, int stack, int slice)
	{
		float thetaInt = this.mStacksInterval;
		float phiInt = this.mSlicesInterval;
		float theta = stack * thetaInt;
		float phi 	= slice * phiInt;
		// draw counterclockwise
		index = addPoint(vec, index, theta, phi);
		index = addPoint(vec, index, theta + thetaInt, phi);
		index = addPoint(vec, index, theta + thetaInt, phi + phiInt);
		// draw counterclockwise
		index = addPoint(vec, index, theta, phi);
		index = addPoint(vec, index, theta + thetaInt, phi + phiInt);
		index = addPoint(vec, index, theta, phi + phiInt);
		
		return index;
	}
	
	/**
	 * Adds color to corresponding point.
	 * 
	 * @param vec 	Vector where color will be added. Out parameter.
	 * @param index Actual index of vector.
	 * @param r		Red value
	 * @param g		Green value
	 * @param b		Blue value
	 * @param a		Alpha value
	 * @return		Actual index of vector.
	 */
	protected int addColorPoint(float vec[], int index, float r, float g, 
			float b, float a)
	{
		vec[index++] = r; vec[index++] = g; vec[index++] = b; vec[index++] = a;		
		return index;
	}
	
	/**
	 * Adds color to corresponding square.
	 * 
	 * @param vec 	Vector where color will be added. Out parameter.
	 * @param index Actual index of vector.
	 * @param r		Red value
	 * @param g		Green value
	 * @param b		Blue value
	 * @param a		Alpha value
	 * @return		Actual index of vector
	 * @see 		#addColorPoint(float[], int, float, float, float, float)
	 */
	protected int addSquereColor(float vec[], int index, float r, float g, 
			float b, float a) 
	{		
		// triangle 1
		index = this.addColorPoint(vec, index, r, g, b, a);
		index = this.addColorPoint(vec, index, r, g, b, a);
		index = this.addColorPoint(vec, index, r, g, b, a);
		
		// triangle 2
		index = this.addColorPoint(vec, index, r, g, b, a);
		index = this.addColorPoint(vec, index, r, g, b, a);
		index = this.addColorPoint(vec, index, r, g, b, a);
		return index;
	}
	
	protected int addNormalToPoint(float vec[], int index, Vector3D point) {
		// Point is like a vector from (0,0,0) to point. When we move
		// this vector by sphere radius, then start position of this point will
		// from sphere mesh (point). After normalization this moved vector will
		// be a normal started from real point and directed from center of
		// model
		Vector3D center = point;//this.centerForNormals;
//		center.move(point);
//		center = point.add(center);
		center.normalize();
		vec[index++] = center.getX();
		vec[index++] = center.getY();
		vec[index++] = center.getZ();
		return index;
	}
	
	/**
	 * Initializes a sphere.
	 * 
	 * @param  radius Radius of sphere
	 * @param  stacks Number of stacks
	 * @param  slices Number of slices
	 * @param  drawStacks Number of stacks to be drawn
	 * @param  drawSlices Number of slices to be drawn
	 * @return 
	 */
	public float[] initVertices(float radius, byte stacks, byte slices, 
			byte drawStacks, byte drawSlices
	) {
		this.mStacksInterval = (float) (Math.PI / (float)slices);
		this.mSlicesInterval = (float) (2.0 * Math.PI / (float)stacks);
		this.mRadius = radius;
		
		byte pointSize 		= 3;							// x, y, z = 3
		byte trianglePoints = 3;				     		// point x 3
		byte squerePoints 	= (byte) (2 * trianglePoints);	// trinagle x 2
		int  spherePoints 	= squerePoints * drawStacks * drawSlices; 
		float vertCoords[] 	= new float[spherePoints * pointSize];
		
		int vertIndex = 0;
		for(int stack=0; stack<drawStacks; ++stack){
			for(int slice=0; slice<drawSlices; ++slice){
				vertIndex = this.addSquere(vertCoords, vertIndex, stack, slice);
			}
		}
		return vertCoords;
	}
	
	/**
	 * Moves initiated sphere from default point (0,0,0) to given
	 * @param point
	 * @return
	 */
	public void moveCenter(float[] verticlesVector, Vector3D point)
	{
		int size = verticlesVector.length;
		// x cords
		for (int i=0; i<size; i = i + 3) {
			verticlesVector[i] += point.getX();
		}
		// y cords
		for (int i=1; i<size; i = i + 3) {
			verticlesVector[i] += point.getZ();
		}
		// z cords
		for (int i=2; i<size; i = i + 3) {
			verticlesVector[i] += point.getY();
		}
	}
	
	/**
	 * 
	 * @param vertexVector
	 * @return
	 */
	public float[] initNormals(float[] vertexVector)
	{
		int normalIndex = 0;
		int allPoint = vertexVector.length;
		float normalVector[] = new float[allPoint];
		Vector3D point = new Vector3D(0);
		while (normalIndex < allPoint) {
			point.setX(vertexVector[normalIndex]);
			point.setY(vertexVector[normalIndex+1]);
			point.setZ(vertexVector[normalIndex+2]);
			normalIndex = this.addNormalToPoint(normalVector, normalIndex, 
					point);
		}
		
		return normalVector;
	}
	
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
	 * Draws initialized sphere.
	 * 
	 * @param  gl 	handle to OpenGL 1.0
	 * @return void
	 */
	@Override
	public void draw(GL10 gl){
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glFrontFace(GL10.GL_CCW);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
		if (null != normalBuffer) {
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		}

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, (int)verticesBuffer.capacity()/3);
		if (null != normalBuffer) {
			gl.glDrawArrays(GL10.GL_NORMAL_ARRAY, 0, (int)normalBuffer.capacity()/3);
		}

		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public float getRadius()
	{
		return this.mRadius;
	}
}
