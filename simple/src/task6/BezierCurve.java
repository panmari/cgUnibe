package task6;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector4f;

import com.jogamp.opengl.math.FloatUtil;

public class BezierCurve {

	private Point4f[] curveMesh;
	private final Matrix4f bernstein = new Matrix4f(-1, 3, -3, 1,
	                                                 3, -6, 3, 0,
	                                                 -3, 3, 0, 0,
	                                                 1, 0, 0, 0 );
	private Matrix4f[] segmentMatrices;
	private Vector4f[] tangents;
	
	
	/**
	 * 
	 * @param n Number of bezier segments
	 * @param controlPoints array of control points in x-y-plane
	 * @param evalN number of evaluation points
	 */
	public BezierCurve(int n, Point2f[] controlPoints, int evalN) {
		if ((n-1)*3+4 != controlPoints.length)
			throw new IllegalArgumentException("controlPoints has wrong length: " + controlPoints.length);
		this.segmentMatrices = new Matrix4f[n];
		curveMesh = new Point4f[evalN];
		tangents = new Vector4f[evalN];
		for (int i = 0; i < controlPoints.length - 1; i += 3) {
			Matrix4f segmentMatrix = new Matrix4f();
			for (int column = 0; column < 4; column++) {
				segmentMatrix.setColumn(column, controlPoints[i + column].x, controlPoints[i + column].y, 0, 0);
			}
			segmentMatrix.mul(bernstein);
			segmentMatrices[i/3] = segmentMatrix;
		}
		
		float evalStep = n*3/(float)(evalN - 1);
		for (int t = 0; t < evalN; t++ )
			curveMesh[t] = pointFor(t*evalStep);
		for (int t = 0; t < evalN; t++ ) {
			Vector4f tangent = new Vector4f(curveMesh[(t + 1) % evalN]);
			tangent.sub(curveMesh[t]);
			tangent.normalize();
			tangent.negate();
			tangents[t] = tangent;
		}

	}
	
	/**
	 * t goes from 0 to n - 1
	 * @param t
	 * @return
	 */
	public Point4f pointFor(float t) {
		int segmentBegin = (int) t/3;
		if (t % 3 == 0 && t != 0)
			segmentBegin -= 1;
		float x = (t - segmentBegin*3) / 3f;
		Point4f resultingPoint = new Point4f(FloatUtil.pow(x, 3),
										FloatUtil.pow(x, 2),
										x,
										1);
		segmentMatrices[segmentBegin].transform(resultingPoint);
		return resultingPoint;
	}
	
	public Point4f[] getCurveMesh() {
		return curveMesh;
	}

	public Vector4f[] getTangents() {
		return tangents;
	}
}
