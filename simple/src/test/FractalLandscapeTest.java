package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import task2c.FractalLandscape;


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
		float[] vertices = fl.getElements().getFirst().getData();
		System.out.println(fl);
		System.out.println(indicesStr);
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
	}
	
}
