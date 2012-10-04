package task1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class Wheel extends MovingShape implements Actable {

	private Matrix4f rotate = new Matrix4f();
	private float radius;
	/**
	 * A wheel class, isn't that great?
	 * Does not move on its own, only rotate
	 * @param radius
	 * @param direction
	 * @param speed
	 */
	public Wheel(float radius, float speed) {
		super(new Torus(radius, radius/4, 60, 5));
		this.radius = radius;
		rotate.rotZ(0.01f*speed/radius);
		rotate.invert();
	}
	
	public void setSpeed(float speed) {
		rotate.rotZ(0.01f*speed/radius);
		rotate.invert();
	}
	
	public void setDirection(Vector3f direction) {
		Matrix4f initMat = new Matrix4f();
		float angle = new Vector3f(1, 0, 0).angle(direction);
		if (direction.z > 0)
			initMat.rotY(-angle);
		else initMat.rotY(angle);
		setTransformation(initMat);
	}
	
	public void act() {
		getTransformation().mul(rotate);
	}
}
