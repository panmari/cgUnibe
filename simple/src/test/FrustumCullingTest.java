package test;

import static org.junit.Assert.*;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Frustum;
import jrtr.BoundingSphere;

import org.junit.Before;
import org.junit.Test;

public class FrustumCullingTest {

	private Frustum standardFrustum;
	
	@Before
	public void setUp() {
		standardFrustum = new Frustum();
	}
	@Test
	public void fullyOutside() {
		BoundingSphere bs = new BoundingSphere(new Point3f(0,10,0), 5);
		assertTrue(standardFrustum.isOutsideOf(new Vector3f(0,1,0), new Point3f(0,0,0), bs));
	}

	@Test
	public void partlyInside() {
		BoundingSphere bs = new BoundingSphere(new Point3f(0,4,0), 5);
		assertFalse(standardFrustum.isOutsideOf(new Vector3f(0,1,0), new Point3f(0,0,0), bs));
	}
	
	@Test
	public void fullyInside() {
		BoundingSphere bs = new BoundingSphere(new Point3f(0,-4,0), 5);
		assertFalse(standardFrustum.isOutsideOf(new Vector3f(0,1,0), new Point3f(0,0,0), bs));
	}
	
	@Test
	public void boarderCase() {
		BoundingSphere bs = new BoundingSphere(new Point3f(0,-4,0), 4);
		assertFalse(standardFrustum.isOutsideOf(new Vector3f(0,1,0), new Point3f(0,0,0), bs));
	}

	@Test
	public void fullFrustumInside() {
		BoundingSphere bs = new BoundingSphere(new Point3f(0,-4,0), 4);
		assertFalse(standardFrustum.isOutside(bs));
	}
	
	@Test
	public void fullFrustumOutside() {
		BoundingSphere bs = new BoundingSphere(new Point3f(0,-4,-2), 1);
		assertTrue(standardFrustum.isOutside(bs));
	}
}
