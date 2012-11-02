package jrtr;

import javax.vecmath.Color3f;

/**
 * Stores the properties of a light source. To be implemented for 
 * the "Texturing and Shading" project.
 */
public abstract class Light {
	protected Color3f color;
	protected float intensity;
	
	public Light(Color3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
}
