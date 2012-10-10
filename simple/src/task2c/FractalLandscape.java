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
		for (int i = 0; i < 4; i++) {
			float height = (float) Math.random()*initialMaxHeight;
			corner.setY(height);
			vertices.appendVector(corner);
			corners.add(new Vector3f(corner));
			rot.transform(corner);
		}
		//if (n > 0) //TODO: should make a condition inside square step
		squareStep(corners);
		
		this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
	}

	private void squareStep(List<Vector3f> corners) {
		Vector3f a = corners.get(2), b = corners.get(0);
		Vector3f squarePoint = new Vector3f();
		squarePoint.sub(a, b);
		squarePoint.scale(.5f);
		squarePoint.add(b);
		squarePoint.setY(calculateHeight(corners));
		System.out.println(squarePoint);
	}

	private float calculateHeight(List<Vector3f> corners) {
		float sumOfHeights = 0;
		for (Vector3f v: corners) {
			sumOfHeights += v.getY();
		}
		return sumOfHeights/4 + (float) Math.random()*20;
	}

}
