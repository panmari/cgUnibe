package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import task1.Cylinder;

public class CylinderTest {

	@Test
	public void test() {
		Cylinder c = new Cylinder(1, 1, 4);
		System.out.println(c.getElements());
	}
	
	@Test
	public void testSizeOfArrays() {
		Cylinder c = new Cylinder(1, 1, 4);
		assertEquals(0, c.getElements().get(0).getNumberOfComponents() % 3);
		assertEquals(c.getElements().get(0).getNumberOfComponents(), 
				c.getElements().get(1).getNumberOfComponents());
		assertEquals(0, c.getIndices().length % 3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddVertices() {
		Cylinder c = new Cylinder(1, 1, 4);
		//TODO: add test
	}

}
