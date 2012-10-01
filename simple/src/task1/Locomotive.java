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

	private Vector3f direction;
	private float speed;
	
	public Locomotive(Vector3f direction, float speed) {
		this.direction = direction;
		this.speed = speed;
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
	}
	
	public void act() {
		for (Shape w: shapes)
			((Actable)w).act();
	}

	class LocomotiveBody extends Shape implements Actable {

		private Matrix4f shift = new Matrix4f();
		
		public LocomotiveBody() {
			super(new Cylinder(2, .5f, 60));
			Matrix4f bodyTransf = new Matrix4f();
			//bodyTransf.rotX(MathFloat.PI/2);
			bodyTransf.setIdentity();
			bodyTransf.setTranslation(new Vector3f(.5f, .5f, .5f));
			setTransformation(bodyTransf);
			direction.normalize();
			direction.scale(0.01f*speed);
			shift.setTranslation(direction);

		}
		@Override
		public void act() {
			Matrix4f t = getTransformation();
			t.add(shift);
			setTransformation(t);
		}
		
	}
}
