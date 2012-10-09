package test;

import java.util.Arrays;

import org.junit.Test;

import task2c.FractalLandscape;


public class FractalLandscapeTest {

	@Test
	public void shouldInitiateCornersValidly() {
		FractalLandscape fl = new FractalLandscape(0);
		System.out.println(Arrays.toString(fl.getElements().get(0).getData()));
	}
	
}
