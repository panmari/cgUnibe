package task2c;

import java.awt.Point;
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
		squareStep(new Point(0,0), new Point(edge - 1, edge -1));
		
		int counter = 0;
		for (int x = 0; x < edge; x++) {
			for (int y = 0; y < edge; y++) {
				vertices.appendTuple(x - edge/2f, grid[x][y], y - edge/2f);
				if (x < edge - 1 && y < edge - 1)
					addQuadrangle(counter, counter + 1, counter + edge + 1, counter + edge);
				counter++;
			}
		}
		this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
		this.addIndicesList(indicesList);
	}

	private float initHeight() {
		return (float) (initialMaxHeight*Math.random());
	}

	private void squareStep(Point topLeft, Point bottomRight) {
		if (topLeft.x >= bottomRight.x - 1 || topLeft.y >= bottomRight.y - 1)
			return;
		int x = (bottomRight.x - topLeft.x)/2 + topLeft.x;
		int y = (bottomRight.y - topLeft.y)/2 + topLeft.y;
		Point middle = new Point(x, y);
		grid[middle.x][middle.y] = calculateHeight(topLeft, bottomRight);
		diamondStep(topLeft, bottomRight, middle);
	}

	private void diamondStep(Point topLeft, Point bottomRight, Point middle) {
		int distance = Math.abs(topLeft.x - middle.x);
		setHeightDiamond(topLeft.x, middle.y, distance);
		setHeightDiamond(middle.x, topLeft.y, distance);
		setHeightDiamond(bottomRight.x, middle.y, distance);
		setHeightDiamond(middle.x, bottomRight.y, distance);
		//call squareStep on every newly created square
		squareStep(topLeft, middle);
		squareStep(new Point(middle.x, topLeft.y), new Point(bottomRight.x, middle.y));
		squareStep(new Point(topLeft.x, middle.y), new Point(middle.x, bottomRight.y));
		squareStep(middle, bottomRight);
	}

	private void setHeightDiamond(int x, int y, int distance) {
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
			throw new RuntimeException("Not possible divider for: " + x + " " + y );
		grid[x][y] =  sumOfHeights/divider + (float) Math.random()*randomness - randomness/2;
	}

	private float calculateHeight(Point topLeft, Point bottomRight) {
		float sumOfHeights = 0;
		sumOfHeights += grid[topLeft.x][topLeft.y];
		sumOfHeights += grid[topLeft.x][bottomRight.y];
		sumOfHeights += grid[bottomRight.x][topLeft.y];
		sumOfHeights += grid[bottomRight.x][bottomRight.y];
		return sumOfHeights/4 + (float) Math.random()*randomness - randomness/2;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < edge; i++)
			result += Arrays.toString(grid[i]) + "\n";
		return result;
	}
}
