package jrtr;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

public class SpotLight extends Light {

	private Vector3f direction;

	public SpotLight(Color3f color, float intensity, Vector3f direction) {
		super(color, intensity);
		this.direction = direction;
	}
}
