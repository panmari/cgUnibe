package task1;

import javax.vecmath.Matrix4f;

import jrtr.Shape;
import jrtr.VertexData;

public abstract class MovingShape extends Shape {

	Matrix4f init;
	Matrix4f shift = new Matrix4f();
	
	public MovingShape(VertexData vertexData) {
		super(vertexData);
	}
	
	public Matrix4f getInit() {
		return new Matrix4f(init);
	}
	
	public Matrix4f getShift() {
		return new Matrix4f(shift);
	}


	public void setInit(Matrix4f m) {
		init = new Matrix4f(m);
	}

	public void addShift(Matrix4f shift_per_time) {
		shift.add(shift_per_time);
		getTransformation().add(shift_per_time);
	}
}
