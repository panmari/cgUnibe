package task2c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;

import task1.AbstractShape;

public class FractalLandscape extends AbstractShape {

	int initialMaxHeight = 10;
	float[][] grid;
	private int edge;
	private float randomness = 1;
	
	public FractalLandscape(int n) {
		super((int) Math.pow((Math.pow(2, n) + 1), 2));
		edge = (int) MathFloat.pow(2, n) + 1;
		grid = new float[edge][edge];
		grid[0][0] = initHeight();
		grid[0][edge - 1] = initHeight();
		grid[edge - 1][0] = initHeight();
		grid[edge - 1][edge - 1] = initHeight();
		
		squareStep(0, edge - 1);
		this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
	}

	private float initHeight() {
		return (float) (initialMaxHeight*Math.random());
	}

	private void squareStep(int topLeft, int bottomRight) {
		int middle = (bottomRight - topLeft)/2 + topLeft;
		grid[middle][middle] = calculateHeight(topLeft, bottomRight);
		diamondStep(topLeft, bottomRight, middle);
	}

	private void diamondStep(int topLeft, int bottomRight, int middle) {
		grid[topLeft][middle] = calculateHeightDiamond(topLeft, middle, middle - topLeft);
		grid[middle][topLeft];
		grid[bottomRight][middle];
		grid[middle][bottomRight];
		//call squareStep on every newly created square
		squareStep();
		squareStep();
		squareStep();
		squareStep();
	}

	private float calculateHeightDiamond(int x, int y, int distance) {
		float sumOfHeights = 0;
		if (x - distance < 0)
			sumOfHeights += grid[x + distance][y];
		if (x + distance >= edge)
			sumOfHeights += grid[x + distance][y];
		return sumOfHeights/4 + (float) Math.random()*randomness - randomness/2;
		return 0;
	}

	private float calculateHeight(int topLeft, int bottomRight) {
		float sumOfHeights = 0;
		sumOfHeights += grid[topLeft][topLeft];
		sumOfHeights += grid[topLeft][bottomRight];
		sumOfHeights += grid[bottomRight][topLeft];
		sumOfHeights += grid[bottomRight][bottomRight];
		return sumOfHeights/4 + (float) Math.random()*randomness - randomness/2;
	}

}
