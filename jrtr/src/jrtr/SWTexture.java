package jrtr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Point2f;

/**
 * Manages textures for the software renderer. Not implemented here.
 */
public class SWTexture implements Texture {

	BufferedImage texture;
	int width, height;
	
	public void load(String fileName) throws IOException {
		texture = ImageIO.read(new File(fileName));
		width = texture.getWidth();
		height = texture.getHeight();
		
	}

	public int getNearestNeighbourColor(float x, float y) {
		return texture.getRGB(Math.round(getScaledX(x)), Math.round(getScaledY(y)));
	}

	public int getBilinearInterpolatedColor(float x, float y) {
		//float[] scaled =  { getScaledX(x), getScaledY(y) };
		Point2f scaled = new Point2f(getScaledX(x), getScaledY(y));
		int[] top = new int[2], bottom = new int[2];
		top[0] = texture.getRGB((int) scaled.x, (int) scaled.y);
		top[1] = texture.getRGB(((int) scaled.x) + 1, (int) scaled.y);
		bottom[0] = texture.getRGB((int) scaled.x, ((int) scaled.y) + 1);
		bottom[1] = texture.getRGB(((int) scaled.x) + 1, ((int) scaled.y) + 1);
		//TODO scale the values. Must be converted first to a jawa.awt.Color?
		return 0;
	}
	
	private float getScaledX(float x) {
		return x*(width - 1);
	}
	
	private float getScaledY(float y) {
		return (1 - y)*(height - 1);
	}

}
