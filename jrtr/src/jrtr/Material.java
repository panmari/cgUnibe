package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	
	public enum MaterialType { Paper }
	private SWTexture texture;
	private MaterialType type;
	
	/**
	 * Old constructor for compatability reasons, always of type paper
	 * @param texture
	 */
	public Material(SWTexture texture) {
		this(texture, MaterialType.Paper);
	}
	
	public Material(SWTexture texture, MaterialType type) {
		this.texture = texture;
		this.type = type;
	}
	
	public SWTexture getTexture() {
		return texture;
	}
	
	/**
	 * For this method to work, the GLShaderManager has to be initialized first!
	 * @return the shader corresponding to the Materialtype set. 
	 */
	public GLShader getShader() {
		return GLShaderManager.getShaderFor(type);
	}
}
