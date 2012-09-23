package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import task1.Cylinder;

public class CylinderTest {

	@Test
	public void test() {
		Cylinder c = new Cylinder(1, 1, 4);
		System.out.println(Arrays.toString(c.getMesh()));
	}
	
	@Test
	public void testSizeOfArrays() {
		Cylinder c = new Cylinder(1, 1, 4);
		assertEquals(0, c.getMesh().length % 3);
		assertEquals(c.getColors().length, c.getMesh().length);
		assertEquals(0, c.getIndices().length % 3);
	}

}
