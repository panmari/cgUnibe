package task1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.Shape;
import jrtr.VertexData;

public class Wheel extends Shape {

	Matrix4f rotate = new Matrix4f();
	Matrix4f shift = new Matrix4f();
	
	/**
	 * A wheel class, isn't that great?
	 * @param radius
	 * @param direction
	 * @param speed
	 */
	public Wheel(float radius, Vector3f direction, float speed) {
		super(new Torus(radius, .5f, 80, 20));
		if (direction.y != 0)
			throw new IllegalArgumentException("Can only move in xz-plane!");
		if (direction.length() == 0)
			throw new IllegalArgumentException("can only move in xz-plane!");
		Matrix4f initMat = new Matrix4f();
		float angle = new Vector3f(1, 0, 0).angle(direction);
		if (direction.z > 0)
			initMat.rotY(-angle);
		else initMat.rotY(angle);
		setTransformation(initMat);
		
		direction.normalize();
		direction.scale(0.01f*speed);
		shift.setTranslation(direction);
		rotate.rotZ(direction.length()/radius);
		rotate.invert();
	}

	
	public void roll() {
		Matrix4f blah = getTransformation();
		blah.mul(rotate);
		blah.add(shift);

		setTransformation(blah);
	}
}
