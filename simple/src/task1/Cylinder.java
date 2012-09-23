package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;

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
		for (int i = 1; i <= resolution; i++) {
			addIndex(upperDiscCenterVertex, i, getAdjacentCircleVertex(i));
		}
		
		for (int i = lowerDiscCenterVertex + 1; i < getNumberOfVertices() - 1; i++) {
			addIndex(lowerDiscCenterVertex, i, getAdjacentCircleVertex(i));
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
			addIndex(i, getAdjacentCircleVertex(i), i + lowerDiscCenterVertex);
			addIndex(getAdjacentCircleVertex(i), getAdjacentCircleVertex(i) + lowerDiscCenterVertex, i + lowerDiscCenterVertex);
		}
	}
	
	private int getAdjacentCircleVertex(int vertex) {
		if (vertex % resolution == 0)
			return 1;
		else return  vertex + 1;
	}
}
