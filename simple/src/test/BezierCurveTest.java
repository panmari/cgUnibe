package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.vecmath.Point2f;
import javax.vecmath.Point4f;

import org.junit.Test;

import task6.BezierCurve;
import task6.RotationBody;

public class BezierCurveTest {

	private final float epsilon = 0.0001f;
	@Test
	public void straightLineShouldWork() {
		Point2f[] controlPoints = new Point2f[4];
		controlPoints[0] = new Point2f(0,0);
		controlPoints[1] = new Point2f(0,1);
		controlPoints[2] = new Point2f(0,2);
		controlPoints[3] = new Point2f(0,3);
		BezierCurve straight_line = new BezierCurve(1, controlPoints, 100);
		
		for (float t = 0; t <= 3; t+=0.01f) {
			assertEquals(0, straight_line.pointFor(t).x, epsilon);
			assertEquals(t, straight_line.pointFor(t).y, epsilon);
		}
	}
	
	@Test
	public void straightLongLineShouldWork() {
		Point2f[] controlPoints = new Point2f[7];
		controlPoints[0] = new Point2f(0,0);
		controlPoints[1] = new Point2f(0,1);
		controlPoints[2] = new Point2f(0,2);
		controlPoints[3] = new Point2f(0,3);
		controlPoints[4] = new Point2f(0,4);
		controlPoints[5] = new Point2f(0,5);
		controlPoints[6] = new Point2f(0,6);
		BezierCurve straight_line = new BezierCurve(2, controlPoints, 100);
		
		for (float t = 3f; t <= 6; t+=0.01f) {
			assertEquals(0, straight_line.pointFor(t).x, epsilon);
			assertEquals(t, straight_line.pointFor(t).y, epsilon);
		}
	}
	
	@Test
	public void rotationBodyTest() {
		Point2f[] controlPoints = new Point2f[7];
		controlPoints[0] = new Point2f(0,0);
		controlPoints[1] = new Point2f(0,1);
		controlPoints[2] = new Point2f(0,2);
		controlPoints[3] = new Point2f(0,3);
		controlPoints[4] = new Point2f(0,4);
		controlPoints[5] = new Point2f(0,5);
		controlPoints[6] = new Point2f(0,6);
		BezierCurve straight_line = new BezierCurve(2, controlPoints, 2);
		
		RotationBody b = new RotationBody(straight_line, 6);
		//assertEquals(2*2*3*3, b.getIndices().length);
	}

	@Test
	public void endPointShouldBeOnControlPoint() {
		Point2f[] controlPoints = new Point2f[4];
		controlPoints[0] = new Point2f(1,0);
		controlPoints[1] = new Point2f(2,1);
		controlPoints[2] = new Point2f(2,2);
		controlPoints[3] = new Point2f(0,3);
		BezierCurve eggCurve = new BezierCurve(1, controlPoints, 5);
		Point4f[] curve = eggCurve.getCurveMesh();
		System.out.println(Arrays.toString(curve));
		assertEquals(0, curve[4].x, epsilon);
		assertEquals(3, curve[4].y, epsilon);
	}

}
