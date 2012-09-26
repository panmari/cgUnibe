package task1;

import java.util.Arrays;

import javax.vecmath.Tuple3f;

/**
 * Wrapper around a float-array. Can only append value triples at the 
 * head position of the array, every other mutation is not allowed.
 * @author mazzzy
 */
public class FloatVertexData {
	
	private float[] data;
	private int head;

	/**
	 * 
	 * @param size, number of vertices you wanna put in here
	 */
	public FloatVertexData(int size) {
		this.data = new float[size*3];
		head = 0;
	}
	
	public void appendTuple(float x, float y, float z) {
		data[head++] = x;
		data[head++] = y;
		data[head++] = z;
	}
	
	public void appendVector(Tuple3f vector) {
		appendTuple(vector.x, vector.y, vector.z);
	}
	
	public float[] getFinishedArray() {
		if (head == data.length)
			return data;
		else throw new RuntimeException("Not completely filled yet!");
	}
	
	public int getHeadPosition() {
		return head;
	}
	
	public void printArray() {
		System.out.println("data:" + Arrays.toString(data));
	}
}
