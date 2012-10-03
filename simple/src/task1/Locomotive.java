package task1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.Shape;

public class Locomotive extends AssembledShape implements Actable {
	
	/**
	 * Could be easily built out of cylinders & torus
	 * (Even only cylinders)
	 */
	
	private Vector3f[] wheelCoordinates = {	new Vector3f(0, 0, 0),
											new Vector3f(1, 0, 0),
											new Vector3f(0, 0, 1),
											new Vector3f(1, 0, 1)};

	private Matrix4f shift = new Matrix4f();
	private Vector3f direction;
	private float speed;
	
	public Locomotive(Vector3f direction, float speed) {
		this.direction = direction;
		this.speed = speed;
		direction.normalize();
		direction.scale(0.01f*speed);
		shift.setTranslation(direction);
		for (int i = 0; i < 4; i++) {
			Wheel w = new Wheel(.5f, direction, speed);
			Matrix4f t = new Matrix4f();
			t.setTranslation(wheelCoordinates[i]);
			Matrix4f init = w.getTransformation();
			init.add(t);
			w.setTransformation(init);
			shapes.add(w);
		}
		shapes.add(new LocomotiveBody());
		setDirection(direction);
	}
	
	public void act() {
		for (Shape s: shapes) {
			((Actable)s).act();
			s.getTransformation().add(shift);
		}
	}
	
	public void setDirection(Vector3f direction) {
		if (direction.y != 0)
			throw new IllegalArgumentException("Can only move in xz-plane!");
		if (direction.length() == 0)
			throw new IllegalArgumentException("Can not be null vector!");
		Matrix4f dirMat = new Matrix4f();
		float angle = new Vector3f(1, 0, 0).angle(direction);
		if (direction.z > 0)
			dirMat.rotY(-angle);
		else dirMat.rotY(angle);
		for (Shape s: shapes) {
			s.getTransformation().mul(dirMat);
		}
	}

	class LocomotiveBody extends Shape implements Actable {
		
		public LocomotiveBody() {
			super(new Cylinder(2, .5f, 60));
			Matrix4f bodyTranslation = new Matrix4f();
			bodyTranslation.rotY(-MathFloat.PI/2);
			bodyTranslation.setTranslation(new Vector3f(.5f, .5f, .5f));
			setTransformation(bodyTranslation);
		}
		
		@Override
		public void act() {

		}
		
	}
}
