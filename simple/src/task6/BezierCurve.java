package task6;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point4f;

import jogamp.graph.math.MathFloat;

public class BezierCurve {

	private Point2f[] curveMesh;
	private Point2f[] controlPoints;
	private final Matrix4f bernstein = new Matrix4f(-1, 3, -3, 1,
	                                                 3, -6, 3, 0,
	                                                 -3, 3, 0, 0,
	                                                 1, 0, 0, 0 );
	private Matrix4f[] segmentMatrices;
	
	
	/**
	 * 
	 * @param n Number of bezier segments
	 * @param controlPoints array of control points in x-y-plane
	 * @param evalN number of evaluation points
	 */
	public BezierCurve(int n, Point2f[] controlPoints, int evalN) {
		if ((n-1)*3+4 != controlPoints.length)
			throw new IllegalArgumentException("controlPoints has wrong length: " + controlPoints.length);
		this.controlPoints = controlPoints;
		this.segmentMatrices = new Matrix4f[n];
		curveMesh = new Point2f[evalN];
		for (int i = 0; i < controlPoints.length; i += 3) {
			Matrix4f segmentMatrix = new Matrix4f();
			for (int column = 0; column < 4; column++) {
				segmentMatrix.setColumn(column, controlPoints[i + column].x, controlPoints[i + column].y, 0, 0);
			}
			segmentMatrices[i % 3] = segmentMatrix;
		}
	}
	
	/**
	 * t goes from 0 to n - 1
	 * @param t
	 * @return
	 */
	public Point4f pointForT(float t) {
		int remainder = ((int) t) % 3;
		int segmentBegin = ((int) t) - remainder;
		float x = t % 3;
		Point4f resultingPoint = new Point4f(MathFloat.pow(x, 3),
										MathFloat.pow(x, 2),
										x,
										0);
		segmentMatrices[segmentBegin].transform(resultingPoint);
		return resultingPoint;
	}
}
