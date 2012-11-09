package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	
	private Texture texture;
	private float diffuseReflectionCoefficient;
	
	public Material(SWTexture texture) {
		this(texture, 1);
	}
	
	public Material(Texture texture, float diffuseReflectionCoefficient) {
		this.texture = texture;
		this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
	}
	
	public Material() {
		this(null);
	}

	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture tex) {
		this.texture = tex;
	}
	
	public float getDiffuseReflectionCoefficient() {
		return diffuseReflectionCoefficient;
	}
}
