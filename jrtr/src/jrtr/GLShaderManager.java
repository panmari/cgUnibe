package jrtr;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL3;

import jrtr.Material.MaterialType;

public class GLShaderManager {
	
	private static final String folder = "../jrtr/shaders/";
	private static Map<MaterialType, GLShader> shaders;
	
	/**
	 * Initializes all available shaders
	 * @param gl
	 */
	public static void initialize(GL3 gl) {
		shaders = new HashMap<MaterialType, GLShader>();
		for (MaterialType type: Material.MaterialType.values()) {
			GLShader s = new GLShader(gl);
			try {
				s.load(folder + type + ".vert", folder + type + ".frag");
			} catch (Exception e) {
				System.out.println("Could not locate following shader: " + type);
			}
			shaders.put(type, s);
		}
	}

	public static GLShader getShaderFor(MaterialType type) {
		return shaders.get(type);
	}
}
