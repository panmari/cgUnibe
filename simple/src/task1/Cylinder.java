package task1;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;

import jogamp.graph.math.MathFloat;
import jrtr.VertexData;

public class Cylinder extends AbstractShape {
	private float height;
	private float radius;
	private int resolution;
	private int upperDiscCenterVertex;
	private int lowerDiscCenterVertex;
	/**
	 * For now, the cylinder is always centered around (0,0,0)
	 * @param height
	 * @param radius
	 */
	public Cylinder(float height, float radius, int resolution) {
		super(2*(resolution + 1));
		this.resolution = resolution;
		this.radius = radius;
		this.height = height;
		this.upperDiscCenterVertex = 0;
		addDisc(height/2);
		this.lowerDiscCenterVertex = verticesList.size()/3;
		addDisc(-height/2);
		addElement(toFloatArray(verticesList), VertexData.Semantic.POSITION, 3);
		addElement(toFloatArray(colorsList), VertexData.Semantic.COLOR, 3);
		for (int i = 1; i < lowerDiscCenterVertex; i++) {
			addIndex(upperDiscCenterVertex, i, getAdjacentDiscVertex(i));
		}
		
		for (int i = lowerDiscCenterVertex + 1; i < getNumberOfVertices(); i++) {
			addIndex(lowerDiscCenterVertex, i, getAdjacentDiscVertex(i));
		}
		addMantle();
		addIndicesList(indicesList);
	}
	
	/**
	 * Adds a disc to the mesh centered around the z-axis on 
	 * the given zcoordinate. Radius & resolution are taken from
	 * instance variables
	 * @param zcoordinate
	 */
	private void addDisc(float zcoordinate) {
		Point3f centerCircle = new Point3f(0, 0, zcoordinate);
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.rotZ(2*MathFloat.PI/resolution);
		addVertex(centerCircle); //center of circle is first point in mesh
		addColor(0, 0, 0);
		Point3f p = new Point3f(radius, 0, 0); //first point of circle
		for (int i = 0; i < resolution; i++) {
			Point3f meshPoint = new Point3f();
			meshPoint.add(p, centerCircle);
			addVertex(meshPoint);
			addColor(i % 2, i % 2, i % 2);
			rotationMatrix.transform(p);
		}
	}
	
	private void addMantle() {
		for (int i = 1; i <= resolution; i++) {
			addIndex(i, getAdjacentDiscVertex(i), i + lowerDiscCenterVertex);
			addIndex(getAdjacentDiscVertex(i), getAdjacentDiscVertex(i) + lowerDiscCenterVertex, i + lowerDiscCenterVertex);
		}
	}
	
	/**
	 * TODO: Refactor this ugly method
	 * @param vertex
	 * @return
	 */
	private int getAdjacentDiscVertex(int vertex) {
		if (vertex < lowerDiscCenterVertex)
			if ((vertex + 1) % lowerDiscCenterVertex == 0)
				return 1;
			else return  vertex + 1;
		else if ((vertex + 1) % getNumberOfVertices() == 0)
				return lowerDiscCenterVertex + 1;
			else return vertex + 1;
	}
}
