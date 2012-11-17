package graphSceneManager;

import javax.vecmath.Matrix4f;

public class TransformGroup extends Group {
	Matrix4f transformation;
	
	public TransformGroup(Matrix4f transformation) {
		this.transformation = transformation;
	}
	public Matrix4f getTransformation() {
		return this.transformation;
	}
}
