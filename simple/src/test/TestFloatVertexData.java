package test;

import static org.junit.Assert.*;

import javax.vecmath.Vector3f;

import org.junit.Test;

import task1.FloatVertexData;

public class TestFloatVertexData {

	@Test(expected=RuntimeException.class)
	public void notFullDataShouldThrowException() {
		FloatVertexData vd = new FloatVertexData(2);
		vd.appendTuple(1, 2, 3);
		vd.getFinishedArray();
	}
	
	@Test
	public void filledArrayShouldReturn() {
		FloatVertexData vd = new FloatVertexData(1);
		vd.appendTuple(1, 2, 3);
		float[] f = vd.getFinishedArray();
		double epsilon = 0.0000001;
		assertEquals(1, f[0], epsilon);
		assertEquals(2, f[1], epsilon);
		assertEquals(3, f[2], epsilon);
	}
	
	@Test
	public void vectorAppendAndTupelAppendShouldDeliverSameResult() {
		FloatVertexData vdVector = new FloatVertexData(1);
		FloatVertexData vdTupel = new FloatVertexData(1);
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
