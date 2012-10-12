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
		System.out.println(fl);
	}
	
	@Test
	public void shouldInitiateSmallLSValidly() {
		FractalLandscape fl = new FractalLandscape(1);
		System.out.println(fl);
	}
	
	@Test
	public void shouldInitiateMediumLSValidly() {
		FractalLandscape fl = new FractalLandscape(2);
		System.out.println(fl);
	}
	
}
