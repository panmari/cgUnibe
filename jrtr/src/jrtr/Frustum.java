package jrtr;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.BoundingSphere;

/**
 * Stores the specification of a viewing frustum, or a viewing
 * volume. The viewing frustum is represented by a 4x4 projection
 * matrix. You will extend this class to construct the projection 
 * matrix from intuitive parameters.
 * <p>
 * A scene manager (see {@link SceneManagerInterface}, {@link SimpleSceneManager}) 
 * stores a frustum.
 */
public class Frustum {

	private Vector3f[] planeNormals = new Vector3f[6];
	private Point3f[] planePoints = new Point3f[6];
	private Matrix4f projectionMatrix;
	private float nearPlane, farPlane, aspectRatio, verticalFieldOfView;
	/**
	 * 
	 * @param nearPlane
	 * @param farPlane
	 * @param aspectRatio 
	 * @param VerticalFieldOfView in radian!
	 */
	public Frustum(float nearPlane, float farPlane, float aspectRatio, float verticalFieldOfView) {
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.aspectRatio = aspectRatio;
		this.verticalFieldOfView = verticalFieldOfView;
		update();
	}
	
	/**
	 * Copied from script
	 */
	private void update() {
		projectionMatrix = new Matrix4f(); //throw away old matrix (may contain nasty stuff)
		projectionMatrix.setM00(1/(aspectRatio*tan(verticalFieldOfView/2)));
		projectionMatrix.setM11(1/(tan(verticalFieldOfView/2)));
		projectionMatrix.setM11(1/(tan(verticalFieldOfView/2)));
		projectionMatrix.setM22((nearPlane + farPlane)/(nearPlane - farPlane));
		projectionMatrix.setM32(-1);
		projectionMatrix.setM23(2 * nearPlane * farPlane/(nearPlane - farPlane));
		
		// "tip" of pyramid
		planeNormals[0] = new Vector3f(0,0,1);
		planePoints[0] = new Point3f(0,0, -nearPlane);
		// bottom of pyramid
		planeNormals[1] = new Vector3f(0,0,-1);
		planePoints[1] = new Point3f(0,0, -farPlane);
		
		//upside:
		Vector3f verticalNormal = new Vector3f(0,1,0);
		Matrix4f rot = new Matrix4f();
		rot.rotX(verticalFieldOfView/2);
		rot.transform(verticalNormal);
		planeNormals[2] = new Vector3f(verticalNormal);
		planePoints[2] = new Point3f(0,0,0);
		
		//downside:
		verticalNormal = new Vector3f(0,-1,0);
		rot.rotX(-verticalFieldOfView/2);
		rot.transform(verticalNormal);
		planeNormals[3] = verticalNormal;
		planePoints[3] = new Point3f(0,0,0);
		
		//to the right
		Vector3f horizontalNormal = new Vector3f(1,0,0);
		float horizontalFieldOfView = (float) Math.atan(tan(verticalFieldOfView/2)*nearPlane*aspectRatio/nearPlane)*2;
		rot.rotY(-horizontalFieldOfView/2);
		rot.transform(horizontalNormal);
		planeNormals[4] = new Vector3f(horizontalNormal);
		planePoints[4] = new Point3f(0,0,0);
		
		//to the left
		horizontalNormal = new Vector3f(-1,0,0);
		rot.rotY(horizontalFieldOfView/2);
		rot.transform(horizontalNormal);
		planeNormals[5] = horizontalNormal;
		planePoints[5] = new Point3f(0,0,0);		
	}

	private float tan(float f) {
		return MathFloat.sin(f)/MathFloat.cos(f);
	}
	/**
	 * Construct a default viewing frustum. The frustum is given by a 
	 * default 4x4 projection matrix.
	 */
	public Frustum()
	{
		this(1, 100, 1, MathFloat.PI/3);
	}
	
	/**
	 * Return the 4x4 projection matrix, which is used for example by 
	 * the renderer.
	 * 
	 * @return the 4x4 projection matrix
	 */
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		update();
	}

	public float getFarPlane() {
		return farPlane;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		update();
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		update();
	}

	public float getVerticalFieldOfView() {
		return verticalFieldOfView;
	}

	public void setVerticalFieldOfView(float verticalFieldOfView) {
		this.verticalFieldOfView = verticalFieldOfView;
		update();
	}
	
	/**
	 * Conservative way to determine if a bounding sphere is inside
	 * @param bs
	 * @return
	 */
	public boolean isOutside(BoundingSphere bs) {
		for (int i = 0; i < 6; i++)
			if (isOutsideOf(planeNormals[i], planePoints[i], bs))
				return true;
		return false;
	}
	
	/**
	 * Tests a point if it lies outside of a given plane.
	 * @param normal
	 * @param onPlane
	 * @param bs
	 * @return
	 */
	public boolean isOutsideOf(Vector3f normal, Point3f onPlane, BoundingSphere bs) {
		Vector3f dist = new Vector3f();
		dist.sub(onPlane, bs.center);
		float t = normal.dot(dist)/normal.dot(normal);
		return -t > bs.radius;
	}
}
