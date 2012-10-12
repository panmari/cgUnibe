package task2c;

import java.util.ArrayList;
import java.util.Arrays;
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
		//this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
	}

	private float initHeight() {
		return (float) (initialMaxHeight*Math.random());
	}

	private void squareStep(int topLeft, int bottomRight) {
		if (topLeft >= bottomRight - 1 )
			return;
		int middle = (bottomRight - topLeft)/2 + topLeft;
		grid[middle][middle] = calculateHeight(topLeft, bottomRight);
		diamondStep(topLeft, bottomRight, middle);
	}

	private void diamondStep(int topLeft, int bottomRight, int middle) {
		grid[topLeft][middle] = calculateHeightDiamond(topLeft, middle);
		grid[middle][topLeft] = calculateHeightDiamond(middle, topLeft);
		grid[bottomRight][middle] = calculateHeightDiamond(bottomRight, middle);
		grid[middle][bottomRight] = calculateHeightDiamond(middle, bottomRight);
		//call squareStep on every newly created square
		squareStep(topLeft, middle);
		squareStep(middle, bottomRight);
		squareStep(middle, bottomRight);
		squareStep(bottomRight, middle);
	}

	private float calculateHeightDiamond(int x, int y) {
		int distance = Math.abs(x-y);
		float sumOfHeights = 0;
		int divider = 0;
		if (x - distance >= 0) {
			sumOfHeights += grid[x - distance][y];
			divider++;
		}
		
		if (x + distance < edge) {
			sumOfHeights += grid[x + distance][y];
			divider++;
		}
		
		if (y - distance >= 0) {
			sumOfHeights += grid[x][y - distance];
			divider++;
		}
		if (y + distance < edge) {
			sumOfHeights += grid[x][y + distance];
			divider++;
		}
		if (divider < 3)
			throw new RuntimeException("Not possible");
		return sumOfHeights/divider + (float) Math.random()*randomness - randomness/2;
	}

	private float calculateHeight(int topLeft, int bottomRight) {
		float sumOfHeights = 0;
		sumOfHeights += grid[topLeft][topLeft];
		sumOfHeights += grid[topLeft][bottomRight];
		sumOfHeights += grid[bottomRight][topLeft];
		sumOfHeights += grid[bottomRight][bottomRight];
		return sumOfHeights/4 + (float) Math.random()*randomness - randomness/2;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < edge; i++)
			result += Arrays.toString(grid[i]) + "\n";
		return result;
	}
}
