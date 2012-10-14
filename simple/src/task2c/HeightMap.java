package task2c;

import java.util.Arrays;

public class HeightMap {

	private float[][] grid;
	private int initialMaxHeight;
	private int edge;
	
	public HeightMap(int edge, float initialMaxHeight) {
		grid = new float[edge][edge];
		this.edge = edge;
		initialMaxHeight = edge;
		initCorners();
	}

	private void initCorners() {
		grid[0][0] = initHeight();
		grid[0][edge - 1] = initHeight();
		grid[edge - 1][0] = initHeight();
		grid[edge - 1][edge - 1] = initHeight();
	}
	
	private float initHeight() {
		return (float) (initialMaxHeight*Math.random());
	}
	
	public void setHeightFor(int x, int y, float height) {
		if (grid[x][y] != 0)
			return;
		grid[x][y] = height;
	}
	
	public float getHeightFor(int x, int y) {
		try {
			if (grid[x][y] != 0)
				throw new NoHeightPresentException();
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoHeightPresentException();
		}
		return grid[x][y];
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
