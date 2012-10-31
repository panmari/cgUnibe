package task1;

import java.util.Arrays;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.VertexData;

public class Cylinder extends AbstractShape {
	private float height;
	private float radius;
	private int resolution;
	private int upperDiscCenterVertex;
	private int lowerDiscCenterVertex;
	
	/**
	 * The cylinder is centered around (0,0,0), the discs are
	 * parallel to the x-y-plane, the center goes through the z-axis.
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
		this.lowerDiscCenterVertex = vertices.getHeadPosition()/3;
		addDisc(-height/2);
		addElement(vertices.getFinishedArray(), VertexData.Semantic.POSITION, 3);
		addElement(colors.getFinishedArray(), VertexData.Semantic.COLOR, 3);

		for (int i = 1; i < lowerDiscCenterVertex; i++) {
			addIndex(upperDiscCenterVertex, i, getAdjacentDiscVertex(i));
		}
		
		for (int i = lowerDiscCenterVertex + 1; i < getNumberOfVertices(); i++) {
			addIndex(lowerDiscCenterVertex, i, getAdjacentDiscVertex(i));
		}
		addMantle();
		addIndicesList(indicesList);
		//addElement(normals.getFinishedArray(), VertexData.Semantic.NORMAL, 3);
		addElement(texels, VertexData.Semantic.TEXCOORD, 2);
	}
	
	/**
	 * Adds a disc to the mesh centered around the z-axis on 
	 * the given z-coordinate. Radius & resolution are taken from
	 * instance variables
	 * @param zcoordinate
	 */
	private void addDisc(float zcoordinate) {
		Vector3f centerCircle = new Vector3f(0, 0, zcoordinate);
		Vector3f normal = new Vector3f(centerCircle);
		normal.normalize();
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.rotZ(2*MathFloat.PI/resolution);
		normals.appendVector(normal);
		vertices.appendVector(centerCircle); //center of circle is first point in mesh
		colors.appendTuple(0, 0, 0);
		Vector3f p = new Vector3f(radius, 0, 0); //first point of circle
		Vector3f pNormal = firstNormal(p, centerCircle, rotationMatrix);
		pNormal.normalize();
		
		for (int i = 0; i < resolution; i++) {
			Vector3f meshPoint = new Vector3f();
			meshPoint.add(p, centerCircle);
			normals.appendVector(pNormal);
			vertices.appendVector(meshPoint);
			colors.appendTuple(i % 2, i % 2, i % 2);
			rotationMatrix.transform(p);
			rotationMatrix.transform(pNormal);
		}
	}
	
	private Vector3f firstNormal(Vector3f p, Vector3f centerCircle, Matrix3f rotationMatrix) {
		Vector3f upNormal = new Vector3f();
		Vector3f downNormal = new Vector3f();
		Vector3f nextP = new Vector3f(p);
		rotationMatrix.transform(nextP);
		Vector3f diffP = new Vector3f(p);
		diffP.sub(nextP);
		upNormal.cross(diffP, p);
		upNormal.normalize();
		Vector3f downVector = new Vector3f(centerCircle);
		downVector.scale(-1);
		downNormal.cross(p, downVector);
		downNormal.normalize();
		
		upNormal.add(downNormal); //take mean of both
		upNormal.normalize();
		return new Vector3f(centerCircle);
	}

	private void addMantle() {
		for (int i = 1; i <= resolution; i++) {
			addQuadrangle(i, getAdjacentDiscVertex(i), 
					getAdjacentDiscVertex(i) + lowerDiscCenterVertex, i + lowerDiscCenterVertex);
			float y = i*2/(float)resolution;
			if (i > resolution/2)
				y = 2 - y;
			addTexel(i, 0, y);
			addTexel(i + lowerDiscCenterVertex, 1, y);
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
