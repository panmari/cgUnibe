package task2;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Camera;

public class CustomCamera extends Camera {

	private Point3f centerOfProjection;
	private Point3f lookUpPoint;
	private Vector3f upVector;

	public CustomCamera(Point3f centerOfProjection, Point3f lookUpPoint, Vector3f upVector) {
		this.centerOfProjection = centerOfProjection;
		this.lookUpPoint = lookUpPoint;
		this.upVector = upVector;
	}
}
