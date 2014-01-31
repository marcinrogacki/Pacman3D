package org.pacman.draw.models;

import javax.microedition.khronos.opengles.GL10;

import org.pacman.R;
import org.pacman.draw.DrawableObject;
import org.pacman.draw.primitives.Circle;
import org.pacman.draw.primitives.Cylinder;
import org.pacman.draw.primitives.Sphere;
import org.pacman.physics.Vector3D;

import android.content.Context;

public class Ghost extends DrawableObject {
	/** ghost model parts */
	protected Cylinder body;
	protected Sphere head;
	protected Circle eye;
	/** How tall is ghost */
	protected float bodyHeight;
	/** How fat is a ghost */
	protected float bodyRadius;
	
	public float getRadius()
	{
		return this.bodyRadius;
	}
	
	public void initTextures(GL10 gl, Context ctx)
	{
		this.eye.loadBitmap(gl, ctx, R.drawable.eye);
	}
	
	public void init(float radius, float height, float eyeRadius)
	{
		// init
		this.body = new Cylinder();
		this.head = new Sphere();
		this.eye = new Circle();
		this.bodyHeight = height;
		this.bodyRadius = radius;
		// var
		byte pieces = 20;
		float[] verticlesVector;
		float[] normalsVector;
		// head - half sphere
		verticlesVector = this.head.initVertices(radius, pieces, pieces, 
				(byte)(pieces/2), pieces);
		this.head.moveCenter(verticlesVector, new Vector3D(0, 0, height/2f));
//		normalsVector = this.head.initNormals(verticlesVector);
//		this.head.initNormalBuffer(normalsVector);
//		this.head.initVerticlesBuffer(verticlesVector);
		// body
		verticlesVector = this.body.mergeVectors(
			this.body.initVertices(radius, height, pieces, pieces),
			verticlesVector
		);
		normalsVector = this.body.initNormals(verticlesVector);
		this.body.initNormalBuffer(normalsVector);
		this.body.initVerticlesBuffer(verticlesVector);
		// eye
		byte eyeTriangles = 15;
		verticlesVector = this.eye.initVertices(0, 0, eyeRadius, eyeTriangles, 
				eyeTriangles);
		normalsVector = this.eye.initNormals(verticlesVector);
		this.eye.initNormalBuffer(normalsVector);
		this.eye.initVerticlesBuffer(verticlesVector);
 	}
	
	@Override
	public void draw(GL10 gl)
	{
		gl.glPushMatrix();
			this.body.draw(gl);
		gl.glPopMatrix();

		gl.glColor4f(1, 1, 1, 1);
		gl.glPushMatrix();
		gl.glRotatef(-20, 0, 1, 0);
			gl.glTranslatef(0, this.bodyHeight/2.5f, this.bodyRadius);
			this.eye.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			gl.glRotatef(20, 0, 1, 0);
			gl.glTranslatef(0, this.bodyHeight/2.5f, this.bodyRadius);
			this.eye.draw(gl);
		gl.glPopMatrix();
	}
}
