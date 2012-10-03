package task1;

import javax.vecmath.Matrix4f;

import jrtr.Shape;
import jrtr.VertexData;

public abstract class MovingShape extends Shape {

	Matrix4f init;
	
	public MovingShape(VertexData vertexData) {
		super(vertexData);
	}
	
	public Matrix4f getInit() {
		return new Matrix4f(init);
	}


	public void setInit(Matrix4f m) {
		init = new Matrix4f(m);
	}
}
