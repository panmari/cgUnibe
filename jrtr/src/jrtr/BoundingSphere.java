package jrtr;

import javax.vecmath.Point3f;

import jrtr.VertexData.Semantic;
import jrtr.VertexData.VertexElement;


public class BoundingSphere {
	
	public float radius;
	public Point3f center;
	/**
	 * For testing only!
	 */
	public BoundingSphere(Point3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public BoundingSphere(VertexData vd) {
		VertexElement positions;
		int index = 0;
		do {
			positions = vd.getElements().get(index);
			index++;
		} while(positions.getSemantic() != Semantic.POSITION);
		
		float[] positionsArray = positions.getData();
		int n = positionsArray.length;
		center = new Point3f();
		for (int i = 0; i < n; i+=3) {
			center.x += positionsArray[i]/n;
			center.y += positionsArray[i+1]/n;
			center.z += positionsArray[i+2]/n;
		}
		
		for (int i = 0; i < n; i+=3) {
			Point3f current = new Point3f();
			current.x = positionsArray[i];
			current.y = positionsArray[i+1];
			current.z = positionsArray[i+2];
			if (current.distance(center) > radius) 
				radius = current.distance(center);
		}
	}
	
	public String toString() {
		return "r=" + radius + " c=" + center;
	}
}
