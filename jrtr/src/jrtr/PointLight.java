package jrtr;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class PointLight extends Light {

	Point3f position;

	public PointLight(Color3f color, float radiance, Point3f position) {
		super(color, radiance);
		this.position = position;
	}
	
	public Point3f getPosition() {
		return position;
	}
}
