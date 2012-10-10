package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import task2c.FractalLandscape;


public class FractalLandscapeTest {

	float e = 0.00001f;
	
	@Test
	public void shouldInitiateCornersValidly() {
		FractalLandscape fl = new FractalLandscape(0);
		float[] vertices = fl.getElements().get(0).getData();
		System.out.println(Arrays.toString(vertices));
		assertEquals(2, vertices[0], e);
		assertEquals(2, vertices[2], e);
		assertEquals(2, vertices[3], e);
		assertEquals(-2, vertices[5], e);
	}
	
}
