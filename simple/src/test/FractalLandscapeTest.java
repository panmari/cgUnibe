package test;

import java.util.Arrays;

import org.junit.Test;

import task2c.FractalLandscape;

/**
 * This is not much of a test class, sadly. Only some print statements thrown together.
 *
 */
public class FractalLandscapeTest {

	float e = 0.00001f;
	
	@Test
	public void shouldInitiateTinyLSValidly() {
		FractalLandscape fl = new FractalLandscape(0);
	}
	
	@Test
	public void shouldInitiateSmallLSValidly() {
		FractalLandscape fl = new FractalLandscape(1);
		
	}
	
	@Test
	public void shouldInitiateMediumLSValidly() {
		FractalLandscape fl = new FractalLandscape(2);
		String indicesStr = Arrays.toString(fl.getIndices());
		float[] normals = fl.getElements().getFirst().getData();
		System.out.println(fl);
		System.out.println(indicesStr);
		System.out.println(Arrays.toString(normals));
		float[] vertices = fl.getElements().getLast().getData();
		System.out.println(Arrays.toString(vertices));
	}
	
	@Test
	public void shouldInitiateBiggerLSValidly() {
		FractalLandscape fl = new FractalLandscape(3);
		
	}
	
	@Test
	public void shouldInitiateHugeLSValidly() {
		FractalLandscape fl = new FractalLandscape(6);
		
	}
	
	@Test
	public void shouldFillInPointsCorrectly() {
		FractalLandscape fl = new FractalLandscape(6);
		System.out.println(fl);
	}
	
}
