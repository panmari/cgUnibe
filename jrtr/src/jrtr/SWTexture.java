package jrtr;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

	/**
	 * this is so ugly, refactor pls!
	 * @param x
	 * @param y
	 * @return
	 */
	public int getBilinearInterpolatedColor(float x, float y) {
		Point2f scaled = new Point2f(getScaledX(x), getScaledY(y));
		Color[] top = new Color[2], bottom = new Color[2];
		top[0] = new Color(texture.getRGB(floor(scaled.x), floor(scaled.y)));
		top[1] = new Color(texture.getRGB(ceil(scaled.x), floor(scaled.y)));
		bottom[0] = new Color(texture.getRGB(floor(scaled.x), ceil(scaled.y)));
		bottom[1] = new Color(texture.getRGB(ceil(scaled.x), ceil(scaled.y)));
		float horzCoeff = ceil(scaled.x) - scaled.x;
		float[] left = new float[3], right = new float[3];
		top[0].getColorComponents(left);
		top[1].getColorComponents(right);
		float[] avgTop = interpolateBetween(left, right, horzCoeff);
		bottom[0].getColorComponents(left);
		bottom[1].getColorComponents(right);
		float[] avgBottom = interpolateBetween(left, right, horzCoeff);
		float vertCoeff = ceil(scaled.y) - scaled.y;
		float[] result = interpolateBetween(avgTop, avgBottom, vertCoeff);
		return new Color(top[0].getColorSpace(), result, 1).getRGB();
	}
	
	private float[] interpolateBetween(float[] colorNear, float[] colorFar, float coeff) {
		float[] avg = new float[3];
		for (int i = 0; i < colorNear.length; i++) {
			avg[i] = colorNear[i]*coeff + colorFar[i]*(1-coeff);
		}
		return avg;
	}
	
	private float getScaledX(float x) {
		return x*(width - 1);
	}
	
	private float getScaledY(float y) {
		return (1 - y)*(height - 1);
	}
	
	private int floor(float f) {
		return (int) f;
	}
	/**
	 * Not exactly ceil, but close enough for this purpose.
	 * @param f
	 * @return
	 */
	private int ceil(float f) {
		return (int) Math.ceil(f);
	}

}
