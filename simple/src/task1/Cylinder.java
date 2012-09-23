package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;

import jogamp.graph.math.MathFloat;
import jrtr.VertexData;

public class Cylinder extends AbstractShape {
	// The vertex colors
	private float vertexColors[];
	
	/**
	 * For now, the cylinder is always centered around (0,0,0)
	 * @param height
	 * @param radius
	 */
	public Cylinder(float height, float radius, int resolution) {
		super(resolution + 1);
		Point3f centerCircle = new Point3f(0, 0, height/2);
		Matrix3f rotationMatrix = new Matrix3f();
		rotationMatrix.rotZ(2*MathFloat.PI/resolution);
		addVertex(centerCircle); //center of circle is first point in mesh
		Point3f p = new Point3f(radius, 0, 0); //first point of circle
		for (int i = 0; i < resolution; i++) {
			Point3f meshPoint = new Point3f();
			meshPoint.add(p, centerCircle);
			addVertex(meshPoint);
			rotationMatrix.transform(p);
		}
		
		addElement(toFloatArray(verticesList), VertexData.Semantic.POSITION, 3);
		
		for (int i = 0; i < verticesList.size()/3 - 1; i++) {
			addIndex(0, i, i + 1);
		}
		addIndex(0, 1, verticesList.size()/3 - 1);
		addIndicesList(indicesList);
	}
}
