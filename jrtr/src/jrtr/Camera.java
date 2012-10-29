package jrtr;

import javax.vecmath.*;

/**
 * Stores the specification of a virtual camera. You will extend
 * this class to construct a 4x4 camera matrix, i.e., the world-to-
 * camera transform from intuitive parameters. 
 * 
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a camera.
 */
public class Camera {

	private Matrix4f cameraMatrix;
	
	private Point3f centerOfProjection;
	private Point3f lookAtPoint;
	private Vector3f upVector;

	private Vector3f x, y, z;

	public Camera(Point3f centerOfProjection, Point3f lookAtPoint, Vector3f upVector) {
		cameraMatrix = new Matrix4f();
		this.centerOfProjection = centerOfProjection;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		update();
	}
	/**
	 * Construct a camera with a default camera matrix. The camera
	 * matrix corresponds to the world-to-camera transform. This default
	 * matrix places the camera at (0,0,10) in world space, facing towards
	 * the origin (0,0,0) of world space, i.e., towards the negative z-axis.
	 */
	public Camera()
	{
		this(new Point3f(2,5,10), new Point3f(0,0,0), new Vector3f(0,1,0));
	}
	
	/**
	 * Return the camera matrix, i.e., the world-to-camera transform. For example, 
	 * this is used by the renderer.
	 * 
	 * @return the 4x4 world-to-camera transform matrix
	 */
	public Matrix4f getCameraMatrix()
	{
		return cameraMatrix;
	}
	
	public void setCenterOfProjection(Point3f centerOfProjection) {
		this.centerOfProjection = centerOfProjection;
		update();
	}
	
	public void update() {
		z = new Vector3f(centerOfProjection);
		Vector4f trans = new Vector4f(centerOfProjection);
		z.sub(lookAtPoint); // new direction of the z-axis!
		z.normalize();
		
		trans.setW(1);
		x = new Vector3f();
		x.cross(upVector, z);
		x.normalize();
		
		y = new Vector3f(); // new direction of the y-axis!
		y.cross(z, x);
		y.normalize();
		
		cameraMatrix.setColumn(0, new Vector4f(x));
		cameraMatrix.setColumn(1, new Vector4f(y));
		cameraMatrix.setColumn(2, new Vector4f(z));
		cameraMatrix.setColumn(3, trans);
		cameraMatrix.invert();
	}
	
	public Point3f getCenterOfProjection() {
		return centerOfProjection;
	}
	public void setLookAtPoint(Point3f lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
		update();
	}
	public Point3f getLookAtPoint() {
		return lookAtPoint;
	}
	public void setUpVector(Vector3f upVector) {
		this.upVector = upVector;
		update();
	}
	public Vector3f getUpVector() {
		return upVector;
	}
	
	public Vector3f getCameraXAxis() {
		return x;
	}
	
	public Vector3f getCameraYAxis() {
		return y;
	}
	
	public Vector3f getCameraZAxis() {
		return z;
	}
}
