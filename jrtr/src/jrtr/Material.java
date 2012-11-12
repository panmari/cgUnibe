package jrtr;


/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	
	private Texture texture;
	private Shader shader;
	private float diffuseReflectionCoefficient, phongExponent;
	private float specularReclectionCoefficient;
	
	public Material() {
		this(null);
	}

	public Material(Texture tex) {
		this(tex, null);
	}
	public Material(Texture tex, Shader shader) {
		this(tex, shader, 1);
	}
	
	public Material(Texture texture, Shader shader, float diffuseReflectionCoefficient) {
		this.texture = texture;
		this.shader = shader;
		this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
		this.phongExponent = 1;
		this.specularReclectionCoefficient = 1;
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
	
	public void setPhongExponent(float phongExponent) {
		this.phongExponent = phongExponent;
	}
	
	public float getPhongExponent() {
		return phongExponent;
	}
	
	public void setSpecularReflectionCoefficient(float spc) {
		this.specularReclectionCoefficient = spc;
	}
	
	public float getSpecularReflectionCoefficient(){
		return this.specularReclectionCoefficient;
	}
	
	public String toString() {
		return "" + texture + ": " + diffuseReflectionCoefficient + " " + specularReclectionCoefficient;
	}

	public void setDiffuseReflectionCoefficient(float f) {
		this.diffuseReflectionCoefficient = f;
	}
}
