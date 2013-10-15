package task1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.jogamp.opengl.math.FloatUtil;
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

	private Wheel[] wheels = new Wheel[4];
	private Matrix4f shift_per_time = new Matrix4f();
	private Vector3f direction;
	private Matrix4f rotateStepLeft = new Matrix4f();
	private Matrix4f rotateStepRight = new Matrix4f();
	private float speed;
	
	public Locomotive(Vector3f initialDirection, float initialSpeed) {
		this.rotateStepLeft.rotY(FloatUtil.PI/40);
		rotateStepRight.invert(rotateStepLeft);
		for (int i = 0; i < 4; i++) {
			Wheel w = new Wheel(.4f, initialSpeed);
			wheels[i] = w;
			Matrix4f m = w.getTransformation();
			m.setIdentity();
			m.setTranslation(wheelCoordinates[i]);
			w.setInit(m);
			shapes.add(w);
		}
		shapes.add(new LocomotiveBody());
		setDirection(initialDirection, initialSpeed);
	}
	
	public void act() {
		for (MovingShape s: shapes) {
			((Actable)s).act();
			s.addShift(shift_per_time);
		}
	}
	
	public void setSpeed(float speed) {
		if (speed > 0)
			setDirection(direction, speed);
	}
	
	public void setDirection(Vector3f direction) {
		setDirection(direction, speed);
	}
	
	public void setDirection(Vector3f direction, float speed) {
		if (direction.y != 0)
			throw new IllegalArgumentException("Can only move in xz-plane!");
		if (direction.length() == 0)
			throw new IllegalArgumentException("Can not be null vector!");
		this.direction = direction;
		this.speed = speed;
		direction.normalize();
		direction.scale(0.01f*speed);
		shift_per_time.setTranslation(direction);
		Matrix4f dirMat = new Matrix4f();
		float angle = new Vector3f(1, 0, 0).angle(direction);
		if (direction.z > 0)
			dirMat.rotY(-angle);
		else dirMat.rotY(angle);
		for (MovingShape s: shapes) {
			Matrix4f m = s.getInit();
			m.mul(dirMat, m);
			m.add(s.getShift());
			s.setTransformation(m);
		}
		for (Wheel w: wheels)
			w.setSpeed(speed);
	}

	public void rotateLeft() {
		rotateStepLeft.transform(direction);
		setDirection(direction, speed);
	}

	public void rotateRight() {
		rotateStepRight.transform(direction);
		setDirection(direction, speed);
	}
	
	class LocomotiveBody extends MovingShape implements Actable {
		
		public LocomotiveBody() {
			super(new Cylinder(2, .5f, 60));
			Matrix4f m = getTransformation();
			m.rotY(-FloatUtil.PI/2);
			Matrix4f helperMatrix = new Matrix4f();
			helperMatrix.setIdentity();
			helperMatrix.setTranslation(new Vector3f(0, .5f, 0));
			m.mul(m, helperMatrix);
			init = new Matrix4f(m);
		}
		
		@Override
		public void act() {

		}
	}

	public void accelerate() {
		setSpeed(speed + .5f);
	}

	public void decelerate() {
		setSpeed(speed - .5f);
	}
}
