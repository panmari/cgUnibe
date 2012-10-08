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
	private float nearPlane, farPlane, aspectRatio, VerticalFieldOfView;
	
	public Frustum(float nearPlane, float farPlane, float aspectRatio, float VerticalFieldOfView) {
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.aspectRatio = aspectRatio;
		update();
	}
	
	/**
	 * Copied from skript
	 */
	private void update() {
		projectionMatrix = new Matrix4f();
		projectionMatrix.setM00(1/(aspectRatio*tan(VerticalFieldOfView/2)));
		projectionMatrix.setM11(1/(tan(VerticalFieldOfView/2)));
		projectionMatrix.setM11(1/(tan(VerticalFieldOfView/2)));
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
	}

	public float getFarPlane() {
		return farPlane;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public float getVerticalFieldOfView() {
		return VerticalFieldOfView;
	}

	public void setVerticalFieldOfView(float verticalFieldOfView) {
		VerticalFieldOfView = verticalFieldOfView;
	}
}
