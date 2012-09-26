package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import jrtr.VertexData;
import jrtr.VertexData.VertexElement;

import org.junit.Before;
import org.junit.Test;

import task1.Torus;

public class TorusTest {

	private Torus t;
	private VertexElement vertices;
	private float epsilon = 0.0001f;
	
	@Before
	public void setUp() {
		t = new Torus(1, 1, 4, 4);
		vertices = t.getElements().get(1);
	}
	@Test
	public void test() {
		assertEquals(4*4*3, vertices.getData().length);
		
		for (int i: t.getIndices())
			assertTrue(i < vertices.getData().length/3);
		assertEquals(8*4, t.getIndices().length/3);
	}
	
	@Test
	public void firstCrossSectionTest() {
		assertEquals(VertexData.Semantic.POSITION, vertices.getSemantic());
		//first point
		assertEquals(2, vertices.getData()[0], epsilon);
		assertEquals(0, vertices.getData()[1], epsilon);
		assertEquals(0, vertices.getData()[2], epsilon);
		//second point
		assertEquals(1, vertices.getData()[3], epsilon);
		assertEquals(0, vertices.getData()[4], epsilon);
		assertEquals(-1, vertices.getData()[5], epsilon);
		//third point
		assertEquals(0, vertices.getData()[6], epsilon);
		assertEquals(0, vertices.getData()[7], epsilon);
		assertEquals(0, vertices.getData()[8], epsilon);
		//fourth point
		assertEquals(1, vertices.getData()[9], epsilon);
		assertEquals(0, vertices.getData()[10], epsilon);
		assertEquals(1, vertices.getData()[11], epsilon);
	}
	
	@Test
	public void secondCrossSectionTest() {
		assertEquals(VertexData.Semantic.POSITION, vertices.getSemantic());
		//first point
		assertEquals(0, vertices.getData()[12], epsilon);
		assertEquals(2, vertices.getData()[13], epsilon);
		assertEquals(0, vertices.getData()[14], epsilon);
		//second point
		assertEquals(0, vertices.getData()[15], epsilon);
		assertEquals(1, vertices.getData()[16], epsilon);
		assertEquals(-1, vertices.getData()[17], epsilon);
		//third point
		assertEquals(0, vertices.getData()[18], epsilon);
		assertEquals(0, vertices.getData()[19], epsilon);
		assertEquals(0, vertices.getData()[20], epsilon);
		//fourth point
		assertEquals(0, vertices.getData()[21], epsilon);
		assertEquals(1, vertices.getData()[22], epsilon);
		assertEquals(1, vertices.getData()[23], epsilon);
	}

}
