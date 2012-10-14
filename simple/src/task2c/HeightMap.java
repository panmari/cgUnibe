package task2c;

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
