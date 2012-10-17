package task2c;

import java.awt.Point;
import java.util.Arrays;

public class HeightMap {

	private float[][] grid;
	private int initialMaxHeight;
	private int edge;
	
	public HeightMap(int edge) {
		grid = new float[edge][edge];
		this.edge = edge;
	}
	
	public void setHeightFor(int x, int y, float height) {
		setHeightFor(x, y, height, false);
	}
	
	public void setHeightFor(int x, int y, float height, boolean force) {
		if (grid[x][y] != 0 && !force)
			return;
		grid[x][y] = height;
	}
	
	public void setHeightFor(Point p, float height) {
		setHeightFor(p.x, p.y, height);
	}
	
	public float getHeightFor(int x, int y) {
		if (grid[x][y] == 0)
			throw new NoHeightPresentException();
		return grid[x][y];
	}
	
	public float getHeightFor(Point p) {
		return getHeightFor(p.x, p.y);
	}
	
	public String toString() {
		String result = "";
		for (int i = 0; i < edge; i++)
			result += Arrays.toString(grid[i]) + "\n";
		return result;
	}
	
	@SuppressWarnings("serial")
	class NoHeightPresentException extends RuntimeException {
		
	}

}
