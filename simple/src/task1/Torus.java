package task1;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.VertexData;

public class Torus extends AbstractShape {

	public Torus(float bigR, float smallR, int bigResolution, int smallResolution) {
		super(bigResolution*smallResolution);
		
		Vector3f discCenter = new Vector3f(bigR, 0, 0);
		Vector3f discRadialVector = new Vector3f(smallR, 0, 0);
		Matrix3f rotY = new Matrix3f();
		Matrix3f rotZ = new Matrix3f();
		rotY.rotY(2*MathFloat.PI/smallResolution);
		rotZ.rotZ(2*MathFloat.PI/bigResolution);
		for (int k = 0; k < bigResolution; k++) {
			for (int i = 0; i < smallResolution; i++) {
				Vector3f discPoint = new Vector3f();
				discPoint.add(discRadialVector, discCenter);
				vertices.appendVector(discPoint);
				colors.appendTuple(bigResolution % 2, smallResolution % 2, 1);
				rotY.transform(discRadialVector);
			}
			rotZ.transform(discCenter);
			rotZ.transform(discRadialVector);
		}
		for (int k = 0; k < bigResolution*smallResolution; k+=smallResolution) {
			for (int i = k; i < smallResolution; i++) {
				addQuadrangle(i, i + smallResolution, (i + 1) % smallResolution + k + smallResolution , (i+1) % smallResolution + k);
			}
		}
		addIndicesList(indicesList);
		addElement(vertices.getFinishedArray(), VertexData.Semantic.POSITION, 3);
		addElement(colors.getFinishedArray(), VertexData.Semantic.COLOR, 3);
	}
}
