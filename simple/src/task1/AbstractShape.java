package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;

import jrtr.VertexData;

public abstract class AbstractShape extends VertexData {

	protected List<Integer> indicesList = new ArrayList<Integer>();
	protected List<Float> verticesList = new ArrayList<Float>();
	protected List<Float> colorsList = new ArrayList<Float>();
	
	public AbstractShape(int n) {
		super(n);
	}

	protected float[] toFloatArray(List<Float> list) {
		float[] f = new float[list.size()];
		for (int i = 0; i < list.size(); i++)
			f[i] = list.get(i);
		return f;
	}
	
	protected void addIndicesList(List<Integer> indicesList) {
		int[] indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		addIndices(indices);
	}
	
	protected void addVertex(Point3f vertex) {
		verticesList.add(vertex.x);
		verticesList.add(vertex.y);
		verticesList.add(vertex.z);
	}
	
	protected void addIndex(int k, int m, int l) {
		indicesList.add(k);
		indicesList.add(m);
		indicesList.add(l);
	}
	
	protected void addColor(float r, float g, float b) {
		colorsList.add(r);
		colorsList.add(g);
		colorsList.add(b);
	}
}
