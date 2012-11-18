package graphSceneManager;

import javax.vecmath.Matrix4f;

public class TransformGroup extends Group {
	Matrix4f transformation;
	
	public TransformGroup() {
		this.transformation = new Matrix4f();
		this.transformation.setIdentity();
	}
	public Matrix4f getTransformation() {
		return this.transformation;
	}
	public void setTransformation(Matrix4f m) {
		this.transformation = m;
	}
}
