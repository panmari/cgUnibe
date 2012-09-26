package task1;

import javax.vecmath.Tuple3i;

/**
 * Wrapper around a Integer-array. Can only append value triples at the 
 * head position of the array, every other mutation is not allowed.
 * @author mazzzy
 * NOT IN USE, BC SIZE MAY VARY!
 */
public class IntegerVertexData {
	
	private int[] data;
	private int head;
	
	
	public IntegerVertexData(int size) {
		this.data = new int[size*3];
		head = 0;
	}
	
	public void appendTupel(int x, int y, int z) {
		data[head++] = x;
		data[head++] = y;
		data[head++] = z;
	}
	
	public void appendVector(Tuple3i vector) {
		appendTupel(vector.x, vector.y, vector.z);
	}
	
	public int[] getFinishedArray() {
		if (head == data.length)
			return data;
		else throw new RuntimeException("Not completely filled yet!");
	}
}
