package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	
	SWTexture texture;
	
	public Material(SWTexture texture) {
		this.texture = texture;
	}
	
	public SWTexture getTexture() {
		return texture;
	}
}
