package jrtr;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class PointLight extends Light {

	private Point3f position;

	public PointLight(Color3f color, float intensity, Point3f position) {
		super(color, intensity);
		this.position = position;
	}
}
