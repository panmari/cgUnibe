package task2c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;

import task1.AbstractShape;

public class FractalLandscape extends AbstractShape {

	int initialMaxHeight = 500;
	Matrix3f rot = new Matrix3f();
	
	public FractalLandscape(int n) {
		super((int) Math.pow((Math.pow(2, n) + 1), 2));
		float edge = MathFloat.pow(2, n) + 1;
		rot.rotY(MathFloat.PI/2);
		List<Vector3f> corners = new ArrayList<Vector3f>();
		Vector3f corner = new Vector3f(edge, Float.NaN, edge);
		for (int i = 0; i <4; i++) {
			float height = (float) Math.random()*initialMaxHeight;
			corner.setY(height);
			vertices.appendVector(corner);
			corners.add(corner);
			rot.transform(corner);
		}
		while (true) { //this is bullshit... I'll go to bed now >.<
			Iterator<Vector3f> iter = corners.iterator();
			Vector3f prev = iter.next();
			while (iter.hasNext()){
				Vector3f current = iter.next();
				Vector3f v = new Vector3f(prev);
				v.add(current.scale(.5f));
				
			}
		}
		this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
	}

	private void addCorners(int i, int j) {
		
		
	}

}
