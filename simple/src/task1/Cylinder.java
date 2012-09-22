package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;

import jogamp.graph.math.MathFloat;

public class Cylinder {

	// The vertex positions of the cube
	private float[] vertexPositions;
	private int[] indices;
	private List<Integer> indicesList = new ArrayList<Integer>();
	private List<Float> vertexPositionsList = new ArrayList<Float>();
	// The vertex colors
	private float vertexColors[];
	
	/**
	 * For now, the cylinder is always centered around (0,0,0)
	 * @param height
	 * @param radius
	 */
	public Cylinder(float height, float radius, int resolution) {
		Point3f centerCircle = new Point3f(0, 0, height/2);
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.rotZ(2*MathFloat.PI/resolution);
		addToMesh(centerCircle); //center of circle is first point in mesh
		Point3f p = new Point3f(radius, 0, 0); //first point of circle
		for (int i = 0; i < resolution; i++) {
			Point3f meshPoint = new Point3f();
			meshPoint.add(p, centerCircle);
			addToMesh(meshPoint);
			rotationMatrix.transform(p);
		}
		vertexPositions = new float[vertexPositionsList.size()];
		for (int i = 0; i < vertexPositionsList.size(); i++) {
			addToIndices(0, i, i+1);
			vertexPositions[i] = vertexPositionsList.get(i);
		}
		
		indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
	}

	private void addToMesh(Point3f meshPoint) {
		vertexPositionsList.add(meshPoint.x);
		vertexPositionsList.add(meshPoint.y);
		vertexPositionsList.add(meshPoint.z);
	}
	
	private void addToIndices(int k, int m, int l) {
		indicesList.add(k);
		indicesList.add(m);
		indicesList.add(l);
	}
	
	public float[] getMesh() {
		return vertexPositions;
	}

	public int[] getIndices() {
		return indices;
	}
}
