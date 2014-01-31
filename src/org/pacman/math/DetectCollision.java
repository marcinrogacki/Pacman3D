package org.pacman.math;

import android.graphics.PointF;

/**
 * Class ported to java fom Janusz Ganczarski lessons
 * @author Marcin Rogacki <rogacki.m@gmail.com>
 */
public class DetectCollision {
	
	/**
	 * Calcs delta
	 * @param p
	 * @param q
	 * @param r
	 * @return Calculated delta
	 */
	private float det (final PointF p, final PointF q, final PointF r){
		return p.x * (q.y - r.y) + q.x * (r.y - p.y) + r.x * (p.y - q.y);
	}
	
	/**
	 * Calcs sign
	 * 
	 * @param x
	 * @return The sign, equal 1 or -1
	 */
	private int sgn (final float x){
		return x==0 ? 0 : (x>0 ? 1 : -1);
	}
	
	/**
	 * Checks if point is in the triangle
	 * 
	 * @param point The point to check
	 * @param a	First triangle point
	 * @param b Second triangle point
	 * @param c Third triangle point
	 * @return True if is otherwise false
	 */
	public boolean inTriangle(final PointF point, final PointF a,
		final PointF b, final PointF c) 
	{
		return 	sgn(det(a,b,point)) == sgn(det(a,b,c)) 
				&& sgn(det(b,c,point)) == sgn(det(b,c,a)) 
				&& sgn(det(c,a,point)) == sgn(det(c,a,b));
	}
	
	/**
	 * Calcs if point is in the square
	 * 
	 * @param point
	 * @param leftBottom
	 * @param rightTop
	 * @return
	 */
	public boolean inSquere(final PointF point, final PointF leftTop,
		final PointF rightBottom)
	{
		return (point.x >= leftTop.x && point.y >= leftTop.y)
			   && (point.x <= rightBottom.x && point.y <= rightBottom.y);
	}
	
	/**
	 * Checks if one circle collide with other
	 * 
	 * @param centerOne
	 * @param centerTwo
	 * @param radiusOne
	 * @param radiusTwo
	 * @return
	 */
	public boolean circleTouchsCircle(final PointF centerOne, 
			final PointF centerTwo, final float radiusOne, 
			final float radiusTwo)
	{
		float pointDistance = (float) Math.sqrt(
				(centerTwo.x - centerOne.x) * (centerTwo.x - centerOne.x)	
				+ (centerTwo.y - centerOne.y) * (centerTwo.y - centerOne.y)
		);
		return pointDistance <= radiusOne + radiusTwo ? true : false;
	}
	
//	public boolean lineSegmentTouchsSquare(final PointF pointOne,
//			 final PointF pointTwo, final PointF squareCenter, 
//			 final float squareHalfWidth, final float squareHalfHeight)
//	{
//		float squareX = squareCenter.x;
//		float squareY = squareCenter.y;
//		if (this.inSquere(pointOne, leftTopSquare, rightBottomSquare)
//			|| 	this.inSquere(pointTwo, leftTopSquare, rightBottomSquare)) {
//			return true;
//		}
//		PointF extendedSquareLeftTop = new PointF(squareX + squareHalfWidth,
//				squareY + squareHalfHeight);
//		// both points are in square
//		
//		if () {
//		}
//		}
//		return false;
//	}
	
//	public boudingBox() {
//		
//	}
}
