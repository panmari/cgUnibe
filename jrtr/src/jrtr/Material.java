package jrtr;


/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	
	private Texture texture;
	private Shader shader;
	private float diffuseReflectionCoefficient;
	
	public Material(Texture tex) {
		this(tex, 1);
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
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public Shader getShader() {
		return shader;
	}
	
	public String toString() {
		return "" + texture + ": " + diffuseReflectionCoefficient;
	}
}
