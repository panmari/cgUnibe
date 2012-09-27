package test;

import static org.junit.Assert.*;

import javax.vecmath.Vector3f;

import org.junit.Test;

import task1.FloatVertexElement;

public class TestFloatVertexData {

	@Test(expected=RuntimeException.class)
	public void notFullVertexElementShouldThrowException() {
		FloatVertexElement vd = new FloatVertexElement(2);
		vd.appendTuple(1, 2, 3);
		vd.getFinishedArray();
	}
	
	@Test
	public void filledVertexElementShouldReturn() {
		FloatVertexElement vd = new FloatVertexElement(1);
		vd.appendTuple(1, 2, 3);
		float[] f = vd.getFinishedArray();
		double epsilon = 0.0000001;
		assertEquals(1, f[0], epsilon);
		assertEquals(2, f[1], epsilon);
		assertEquals(3, f[2], epsilon);
	}
	
	@Test
	public void vectorAppendAndTupelAppendShouldDeliverSameResult() {
		FloatVertexElement vdVector = new FloatVertexElement(1);
		FloatVertexElement vdTupel = new FloatVertexElement(1);
		vdTupel.appendTuple(1, 2, 3);
		vdVector.appendVector(new Vector3f(1,2,3));
		double epsilon = 0.0000001;
		float[] fTupel = vdTupel.getFinishedArray();
		float[] fVector = vdVector.getFinishedArray();
		assertEquals(fTupel[0], fVector[0], epsilon);
		assertEquals(fTupel[1], fVector[1], epsilon);
		assertEquals(fTupel[2], fVector[2], epsilon);
	}

}
