package task2c;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;

import task1.AbstractShape;
import task2c.HeightMap.NoHeightPresentException;

public class FractalLandscape extends AbstractShape {

	private int edge;
	private float randomness;
	private HeightMap map;
	float initialMaxHeight;
	
	public FractalLandscape(int n) {
		super((int) Math.pow((Math.pow(2, n) + 1), 2));
		edge = (int) MathFloat.pow(2, n) + 1;
		randomness = edge/4f;
		this.map = new HeightMap(edge);
		this.initialMaxHeight = edge/2;
		initCorners();
		
		int resolution = edge - 1;
		
		while (resolution > 1) {
			LinkedList<Point> mids = new LinkedList<Point>();
			for (int x = 0; x < edge; x += resolution)
				for (int y = 0; y < edge; y += resolution)
					mids.add(squareStep(new Point(x, y), new Point(x + resolution, y + resolution)));
			
			for (Point p: mids) {
				diamondStep(p, resolution);
			}
			randomness = randomness/2;
			resolution = resolution/2;
		}
		
		int counter = 0;
		for (int x = 0; x < edge; x++) {
			for (int y = 0; y < edge; y++) {
				computeNormal(x, y);
				vertices.appendTuple(x - edge/2f, map.getHeightFor(x, y), y - edge/2f);
				if (x < edge - 1 && y < edge - 1)
					addQuadrangle(counter, counter + 1, counter + edge + 1, counter + edge);
				counter++;
			}
		}
		this.addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
		this.addElement(normals.getFinishedArray(), Semantic.NORMAL, 3);
		this.addIndicesList(indicesList);
	}

	private void computeNormal(int x, int y) {
		try {
			Vector3f v = new Vector3f(x, map.getHeightFor(x, y), y);
			Vector3f down = new Vector3f(x + 1, map.getHeightFor(x, y + 1), y + 1);
			Vector3f right = new Vector3f(x + 1, map.getHeightFor(x + 1, y), y);
			down.sub(v);
			right.sub(v);
			Vector3f cross = new Vector3f();
			cross.cross(down, right);
			normals.appendVector(cross);
		} catch (ArrayIndexOutOfBoundsException e) {
			//i dont even...
			//TODO: handle edge cases more graceful
			normals.appendTuple(0, 1, 0);
		}
	}



	private Point squareStep(Point topLeft, Point bottomRight) {
		if (topLeft.x >= bottomRight.x - 1 || topLeft.y >= bottomRight.y - 1)
			return null;
		int x = (bottomRight.x - topLeft.x)/2 + topLeft.x;
		int y = (bottomRight.y - topLeft.y)/2 + topLeft.y;
		map.setHeightFor(x, y, calculateHeight(topLeft, bottomRight));
		return new Point(x, y);
	}

	private void diamondStep(Point middle, int distance) {
		setHeightDiamond(middle.x - distance, middle.y, distance);
		setHeightDiamond(middle.x, middle.y - distance, distance);
		setHeightDiamond(middle.x + distance, middle.y, distance);
		setHeightDiamond(middle.x, middle.y + distance, distance);
	}

	private void setHeightDiamond(int x, int y, int distance) {
		float sumOfHeights = 0;
		int divider = 0;
		
		Point[] diamondPoints = getDiamondPoints(x, y, distance);
		for (Point p: diamondPoints) {
			try {
				sumOfHeights += map.getHeightFor(p.x, p.y);
				divider++;
			} catch (NoHeightPresentException e) {
				//nothing is added, divider is not increased
			}
		}
		map.setHeightFor(x, y, sumOfHeights/divider + (float) Math.random()*randomness - randomness/2);
	}

	private Point[] getDiamondPoints(int x, int y, int distance) {
		Point[] points = new Point[4];
		points[0] = new Point(x - distance, y);
		points[1] = new Point(x + distance, y);
		points[2] = new Point(x, y - distance);
		points[3] = new Point(x, y + distance);
		return points;
	}

	private float calculateHeight(Point topLeft, Point bottomRight) {
		float sumOfHeights = 0;
		sumOfHeights += map.getHeightFor(topLeft.x,topLeft.y);
		sumOfHeights += map.getHeightFor(topLeft.x, bottomRight.y );
		sumOfHeights += map.getHeightFor(bottomRight.x, topLeft.y);
		sumOfHeights += map.getHeightFor(bottomRight.x, bottomRight.y);
		return sumOfHeights/4 + (float) Math.random()*randomness - randomness/2;
	}
	
	private void initCorners() {
		map.setHeightFor(0, 0, initHeight());
		map.setHeightFor(edge - 1, 0, initHeight());
		map.setHeightFor(0, edge - 1 , initHeight());
		map.setHeightFor(edge - 1, edge - 1, initHeight());
	}
	
	private float initHeight() {
		return (float) (initialMaxHeight*Math.random());
	}
}
