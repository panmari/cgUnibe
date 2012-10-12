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
	float[][] grid;
	private int edge;
	
	public FractalLandscape(int n) {
		super((int) Math.pow((Math.pow(2, n) + 1), 2));
		edge = (int) MathFloat.pow(2, n) + 1;
		grid = new float[edge][edge];
		rot.rotY(MathFloat.PI/2);
		List<Vector3f> corners = new ArrayList<Vector3f>();
		grid[0][0] = (float) Math.random()*initialMaxHeight;
		squareStep(corners);
		
		this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
	}

	private int gridPos(float f) {
		return Math.round(f) + (edge - 1)/2 ;
	}

	private void squareStep(List<Vector3f> corners) {
		Vector3f a = corners.get(2), b = corners.get(0);
		Vector3f squarePoint = new Vector3f();
		squarePoint.sub(a, b);
		squarePoint.scale(.5f);
		squarePoint.add(b);
		squarePoint.setY(calculateHeight(corners));
		System.out.println(squarePoint);
		diamondStep(corners, squarePoint);
	}

	private void diamondStep(List<Vector3f> corners, Vector3f squarePoint) {
		Vector3f diamondPoint = new Vector3f(corners.get(0));
		List<Vector3f> diamondCorners = new ArrayList<Vector3f>();
		diamondPoint.scale(.5f);
		diamondPoint.scaleAdd(.5f, corners.get(1));
		for (int i = 0; i < 4; i++) {
			diamondPoint.setZ(calculateHeight(corners)); //these are not the correct corners
			diamondCorners.add(new Vector3f(diamondPoint));
			rot.transform(diamondPoint);
		}
		//call squareStep on every newly created square
		squareStep();
		squareStep();
		squareStep();
		squareStep();
	}

	private float calculateHeight(List<Vector3f> corners) {
		float sumOfHeights = 0;
		for (Vector3f v: corners) {
			sumOfHeights += v.getY();
		}
		return sumOfHeights/4 + (float) Math.random()*20;
	}

}
