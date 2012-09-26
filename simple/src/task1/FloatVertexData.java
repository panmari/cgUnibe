package task1;

import javax.vecmath.Vector3f;

/**
 * Wrapper around a float-array. Can only append value triples at the 
 * head position of the array, every other mutation is not allowed.
 * @author mazzzy
 */
public class FloatVertexData {
	
	private float[] data;
	private int head;

	public FloatVertexData(int size) {
		this.data = new float[size*3];
		head = 0;
	}
	
	public void appendTupel(float x, float y, float z) {
		data[head++] = x;
		data[head++] = y;
		data[head++] = z;
	}
	
	public void appendVector(Vector3f vector) {
		appendTupel(vector.x, vector.y, vector.z);
	}
	
	public float[] getFinishedArray() {
		if (head == data.length)
			return data;
		else throw new RuntimeException("Not completely filled yet!");
	}
}
