package jrtr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		// TODO Auto-generated method stub
		return 0;
	}
	
	private float getScaledX(float x) {
		return x*(width - 1);
	}
	
	private float getScaledY(float y) {
		return (1 - y)*(height - 1);
	}

}
