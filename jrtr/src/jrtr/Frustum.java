package jrtr;

import javax.vecmath.Matrix4f;

import jogamp.graph.math.MathFloat;

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
		projectionMatrix = new Matrix4f();
		float f[] = {2.f, 0.f, 0.f, 0.f, 
					 0.f, 2.f, 0.f, 0.f,
				     0.f, 0.f, -1.02f, -2.02f,
				     0.f, 0.f, -1.f, 0.f};
		projectionMatrix.set(f);
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
}
