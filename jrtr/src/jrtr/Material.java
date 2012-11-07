package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	
	public enum MaterialType { Paper }
	private SWTexture texture;
	private float diffuseReflectionCoefficient;
	
	public Material(SWTexture texture) {
		this(texture, 3);
	}
	
	public Material(SWTexture texture, float diffuseReflectionCoefficient) {
		this.texture = texture;
		this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
	}
	
	public Material() {
		this(null);
	}

	public SWTexture getTexture() {
		return texture;
	}
	
	public float getDiffuseReflectionCoefficient() {
		return diffuseReflectionCoefficient;
	}
}
