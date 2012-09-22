package test;

import static org.junit.Assert.*;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import org.junit.Test;

public class VecmathTests {

	//as always, epsilon is something small > 0
	private double epsilon = 0.0001;
	
	@Test
	public void testRotMatrix() {
		Vector3d v = new Vector3d(1,0,0);
		Matrix3d rotMat = new Matrix3d();
		rotMat.rotZ(Math.PI/2);
		rotMat.transform(v);
		
		// doesn't work bc gay double precision
		// assertEquals(new Vector3d(0,1,0), v);
		assertTrue(v.epsilonEquals(new Vector3d(0,1,0), epsilon));
	}

}
