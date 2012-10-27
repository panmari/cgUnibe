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
	 * this is ugly & slow, refactor pls!
	 * @param x
	 * @param y
	 * @return
	 */
	public int getBilinearInterpolatedColor(float x, float y) {
		Point2f scaled = new Point2f(getScaledX(x), getScaledY(y));
		int[] top = new int[2], bottom = new int[2];
		//this step is very bad
		top[0] = texture.getRGB(floor(scaled.x), floor(scaled.y));
		top[1] = texture.getRGB(ceil(scaled.x), floor(scaled.y));
		bottom[0] = texture.getRGB(floor(scaled.x), ceil(scaled.y));
		bottom[1] = texture.getRGB(ceil(scaled.x), ceil(scaled.y));
		float horzCoeff = ceil(scaled.x) - scaled.x;
		int[] left = getRGBArrayOfHexaColor(top[0]);
		int[] right = getRGBArrayOfHexaColor(top[1]);
		int[] weightedTop = interpolateBetween(left, right, horzCoeff);
		left = getRGBArrayOfHexaColor(bottom[0]);
		right = getRGBArrayOfHexaColor(bottom[1]);
		int[] weightedBottom = interpolateBetween(left, right, horzCoeff);
		float vertCoeff = ceil(scaled.y) - scaled.y;
		int[] result = interpolateBetween(weightedTop, weightedBottom, vertCoeff);
		
		return (result[0] << 16) | (result[1] << 8) | result[2];
	}
	
	private int[] interpolateBetween(int[] colorNear, int[] colorFar, float coeff) {
		int[] avg = new int[3];
		for (int i = 0; i < colorNear.length; i++) {
			avg[i] = (int)(colorNear[i]*coeff + colorFar[i]*(1-coeff));
		}
		return avg;
	}
	
	private int[] getRGBArrayOfHexaColor(int hexaColor) {
		int[] rgb = new int[3];
		int bitmask = 0x0000FF;
		for (int i = 0; i < 3; i++) {
			rgb[2 - i] = (hexaColor >> 8*i) & bitmask;
		}
		return rgb;
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

	private int ceil(float f) {
		return (int) Math.ceil(f);
	}

}
