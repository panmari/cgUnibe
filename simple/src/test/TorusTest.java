package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import jrtr.VertexData;
import jrtr.VertexData.VertexElement;

import org.junit.Test;

import task1.Torus;

public class TorusTest {

	private Torus t;
	
	public void setUp() {
		
	}
	@Test
	public void test() {
		t = new Torus(1, 1, 4, 4);
		//do some checks
		VertexElement vertices = t.getElements().get(0);
		assertEquals(VertexData.Semantic.POSITION, vertices.getSemantic());
		assertEquals(4*4*3, vertices.getData().length);
		for (int i: t.getIndices())
			assertTrue(i < t.getElements().get(0).getNumberOfComponents()/3);
		System.out.println(Arrays.toString(t.getIndices()));
		assertEquals(8*4, t.getIndices().length/3);
	}

}
