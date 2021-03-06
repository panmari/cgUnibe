package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2f;

import jrtr.VertexData;

public abstract class AbstractShape extends VertexData {

	protected List<Integer> indicesList = new ArrayList<Integer>();
	protected FloatVertexElement vertices;
	protected FloatVertexElement colors;
	protected FloatVertexElement normals;
	protected float[] texels;
	private int numberOfVertices;
	
	
	public AbstractShape(int numberOfVertices) {
		super(numberOfVertices);
		this.numberOfVertices = numberOfVertices;
		vertices = new FloatVertexElement(numberOfVertices);
		colors = new FloatVertexElement(numberOfVertices);
		normals = new FloatVertexElement(numberOfVertices);
		texels = new float[2*numberOfVertices];
	}

	protected void addIndicesList(List<Integer> indicesList) {
		int[] indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		addIndices(indices);
	}
		
	protected void addIndex(int k, int m, int l) {
		if (k >= numberOfVertices || m >= numberOfVertices || l >= numberOfVertices)
			throw new IllegalArgumentException("One of these vertices does not exist: " +
											+ k + ", " + m + ", " + l);
		indicesList.add(k);
		indicesList.add(m);
		indicesList.add(l);
	}
	
	/**
	 * Corners should be clockwise
	 */
	protected void addQuadrangle(int i, int j, int k, int l) {
		addIndex(i, j, k);
		addIndex(i, k, l);
	}
	
	protected void addTexel(int vertexNr, Point2f p) {
		addTexel(vertexNr, p.x, p.y);
	}
	
	protected void addTexel(int vertexNr, float x, float y) {
		texels[vertexNr*2] = x;
		texels[vertexNr*2 + 1] = y;
	}

}
