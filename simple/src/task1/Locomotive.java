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
	
	private Vector3f[] wheelCoordinates = {	new Vector3f(.5f, 0, .5f),
											new Vector3f(-.5f, 0, .5f),
											new Vector3f(.5f, 0, -.5f),
											new Vector3f(-.5f, 0, -.5f)};

	private Matrix4f shift = new Matrix4f();
	private Vector3f direction;
	private float speed;
	
	public Locomotive(Vector3f direction, float speed) {
		this.direction = direction;
		this.speed = speed;
		direction.normalize();
		direction.scale(0.01f*speed);
		for (int i = 0; i < 4; i++) {
			Wheel w = new Wheel(.4f, direction, speed);
			Matrix4f m = w.getTransformation();
			m.setIdentity();
			m.setTranslation(wheelCoordinates[i]);
			shapes.add(w);
		}
		shapes.add(new LocomotiveBody());
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
		//shift.setTranslation(direction);
		Matrix4f dirMat = new Matrix4f();
		float angle = new Vector3f(1, 0, 0).angle(direction);
		if (direction.z > 0)
			dirMat.rotY(-angle);
		else dirMat.rotY(angle);
		for (Shape s: shapes) {
			Matrix4f m = s.getTransformation();
			m.mul(dirMat, m);
		}
	}


	class LocomotiveBody extends Shape implements Actable {
		
		public LocomotiveBody() {
			super(new Cylinder(2, .5f, 60));
			Matrix4f m = getTransformation();
			m.rotY(-MathFloat.PI/2);
			Matrix4f helperMatrix = new Matrix4f();
			helperMatrix.setIdentity();
			helperMatrix.setTranslation(new Vector3f(0, .5f, 0));
			m.mul(m, helperMatrix);
		}
		
		@Override
		public void act() {

		}
		
	}
}
