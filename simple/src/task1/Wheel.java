package task1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.Shape;
import jrtr.VertexData;

public class Wheel extends Shape {

	Matrix4f rotate = new Matrix4f();
	Matrix4f shift = new Matrix4f();
	
	public Wheel(int radius, Vector3f direction, float speed) {
		super(new Torus(radius, 1, 5, 80));
		Matrix4f blah = new Matrix4f();
		blah.rotX(MathFloat.PI/2);
		setTransformation(blah);
		rotate.rotY(0.01f);
		direction.normalize();
		direction.scale(0.01f);
		shift.setTranslation(direction);
	}

	
	public void roll() {
		Matrix4f blah = getTransformation();
		blah.mulTransposeLeft(shift, rotate); //could be source of an error! (First time this happens...)
		shift.mul(1.1f);
		blah.mul(shift);
		setTransformation(blah);
	}
}
