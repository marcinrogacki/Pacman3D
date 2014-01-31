package org.pacman.draw.primitives;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.draw.DrawableObject;
import org.pacman.physics.Vector3D;

public class Cylinder extends DrawableObject
{
	protected float radius;
	protected float height;
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * 
	 * @param radius
	 * @param height
	 * @param stacks
	 */
	public float[] initVertices(float radius, float height, int stacks,
			int slices)
	{
		this.radius = radius;
		this.height = height;
		byte pointSize 		= 3;							// x, y, z = 3
		byte trianglePoints = 3;				     		// point x 3
		byte squerePoints 	= (byte) (2 * trianglePoints);	// trinagle x 2
		byte wallPoints		= (byte) (squerePoints * slices);
		int  cylinderPoints = wallPoints * stacks; 
		float vertCoords[] 	= new float[cylinderPoints * pointSize];
		//angle per square
		final float CIRCLE_SILCE_ANGLE = (float)(2*Math.PI/stacks);
		final float SLICE_HEIGHT = height/slices;
		
		int vertexTableIndex = 0;
		for (int stack=0; stack<stacks; stack++) {
			for (int slice=0; slice<slices; slice++) {
				vertexTableIndex = this.addSquare(vertCoords, vertexTableIndex,
						radius, height/2, stack, CIRCLE_SILCE_ANGLE, slice, 
						SLICE_HEIGHT);
			}
		}
		
		return vertCoords;
	}

	/**
	 * 
	 * @param vec
	 * @param vecIndex
	 * @param radius
	 * @param height
	 * @param silceAngle
	 * @param sileAngleIndex
	 * @return
	 */
	protected int addPoint(float vec[], int vecIndex, float radius, 
			float height, float silceAngle, int sileAngleIndex)
	{
		vec[vecIndex++] = 
			(float)(radius * Math.sin(-silceAngle * sileAngleIndex));
		vec[vecIndex++] = height;
		vec[vecIndex++] = 
			(float)(radius * Math.cos(silceAngle * sileAngleIndex));
		return vecIndex;
	}
	
	/**
	 * 
	 * @param vec
	 * @param vecIndex
	 * @param radius
	 * @param height
	 * @param silceAngle
	 * @param stack
	 * @return
	 */
	protected int addSquare(float vec[], int vecIndex, float radius, 
			float halfHeight, int stack, float stackAngle, int slice, 
			float silceHeight)
	{
		// left? top
		float yHeight = halfHeight - silceHeight * slice;
		float yNextHeight = halfHeight - silceHeight * (slice+1);
		vecIndex = this.addPoint(vec, vecIndex, radius, yHeight, stackAngle, 
				stack);
		// right? top
		vecIndex = this.addPoint(vec, vecIndex, radius, yHeight, stackAngle, 
				stack+1);
		// left? bottom
		vecIndex = this.addPoint(vec, vecIndex, radius, yNextHeight, stackAngle, 
				stack);
		
		// right? top
		vecIndex = this.addPoint(vec, vecIndex, radius, yHeight, stackAngle, 
				stack+1);		
		// right? bottom
		vecIndex = this.addPoint(vec, vecIndex, radius, yNextHeight, stackAngle, 
				stack+1);		
		// left? bottom
		vecIndex = this.addPoint(vec, vecIndex, radius, yNextHeight, stackAngle, 
				stack);
		
		return vecIndex;
	}
	
	/**
	 * 
	 * @param vertexVector
	 * @return
	 */
	public float[] initNormals(float[] vertexVector)
	{
		int normalVectorINdex = 0;
		int allPoint = vertexVector.length;
		float[] normalsVector = new float[allPoint];
		Vector3D point = new Vector3D(0);
		while (normalVectorINdex < allPoint) {
			point.setX(vertexVector[normalVectorINdex]);
			point.setY(vertexVector[normalVectorINdex+1]);
			point.setZ(vertexVector[normalVectorINdex+2]);
			normalVectorINdex = this.addNormalToPoint(normalsVector, point,
					normalVectorINdex);
		}
		return normalsVector;
	}
	
	protected int addNormalToPoint(float[] normalsVector, Vector3D point, 
			int normalVectorINdex)
	{
		Vector3D center = new Vector3D(0);
		// adjust center to point height - should be parallel
		center.move(point.getX(), point.getY(), point.getZ());
		// add to point - turn in right direction
//		center.move(point);
		center = center.normalize();
		// add normal
		normalsVector[normalVectorINdex++] = center.getX();
		normalsVector[normalVectorINdex++] = center.getY();
		normalsVector[normalVectorINdex++] = center.getZ();
		return normalVectorINdex;
	}
	
	protected int addNormalToPoint2(float[] normalsVector, Vector3D point, 
			int normalVectorINdex)
	{
		Vector3D center = new Vector3D(0);
		// adjust center to point height - should be parallel
		center.move(point.getX(), 0, point.getZ());
		// add to point - turn in right direction
//		center.move(point);
		center = center.normalize();
		// add normal
		normalsVector[normalVectorINdex++] = center.getX();
		normalsVector[normalVectorINdex++] = center.getY();
		normalsVector[normalVectorINdex++] = center.getZ();
		return normalVectorINdex;
	}
	
	/**
	 * 
	 * @param vertexVector
	 * @return
	 */
	public float[] initNormals2(float[] vertexVector)
	{
		int normalVectorINdex = 0;
		int allPoint = vertexVector.length;
		float[] normalsVector = new float[allPoint];
		Vector3D point = new Vector3D(0);
		while (normalVectorINdex < allPoint) {
			point.setX(vertexVector[normalVectorINdex]);
			point.setY(vertexVector[normalVectorINdex+1]);
			point.setZ(vertexVector[normalVectorINdex+2]);
			normalVectorINdex = this.addNormalToPoint2(normalsVector, point,
					normalVectorINdex);
		}
		return normalsVector;
	}
	
	/**
	 * 
	 * @param vertexVector
	 * @return
	 */
	public float[] initNormals3(float[] vertexVector)
	{
		int normalVectorINdex = 0;
		int allPoint = vertexVector.length;
		float[] normalsVector = new float[allPoint];
		Vector3D point = new Vector3D(0);
		while (normalVectorINdex < allPoint) {
			point.setX(vertexVector[normalVectorINdex]);
			point.setY(vertexVector[normalVectorINdex+1]);
			point.setZ(vertexVector[normalVectorINdex+2]);
			normalVectorINdex = this.addNormalToPoint3(normalsVector, point,
					normalVectorINdex);
		}
		return normalsVector;
	}

	
	protected int addNormalToPoint3(float[] normalsVector, Vector3D point, 
			int normalVectorINdex)
	{
		

		Vector3D center = new Vector3D(1,1,0);
		// adjust center to point height - should be parallel
		center.move(point.getX(), 0, point.getZ());
		// add to point - turn in right direction
//		center.move(point);
		center = center.normalize();
		// add normal
		normalsVector[normalVectorINdex++] = center.getX();
		normalsVector[normalVectorINdex++] = center.getY();
		normalsVector[normalVectorINdex++] = center.getZ();
		return normalVectorINdex;
	}
	
	
	@Override
	public void draw(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.verticesBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, this.normalBuffer);
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 
				(int)this.verticesBuffer.capacity()/3);
		gl.glDrawArrays(GL10.GL_NORMAL_ARRAY, 0, 
				(int)this.normalBuffer.capacity()/3);

		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
