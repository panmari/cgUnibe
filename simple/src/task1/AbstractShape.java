package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;

import jrtr.VertexData;

public abstract class AbstractShape extends VertexData {

	protected List<Integer> indicesList = new ArrayList<Integer>();
	protected FloatVertexElement vertices;
	protected FloatVertexElement colors;
	
	public AbstractShape(int n) {
		super(n);
		vertices = new FloatVertexElement(n);
		colors = new FloatVertexElement(n);
	}

	protected void addIndicesList(List<Integer> indicesList) {
		int[] indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		addIndices(indices);
	}
		
	protected void addIndex(int k, int m, int l) {
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

}
