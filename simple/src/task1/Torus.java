package task1;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.VertexData;

public class Torus extends AbstractShape {

	public Torus(float bigR, float smallR, int bigResolution, int smallResolution) {
		super(bigResolution*smallResolution);
		
		//first crosssection:
		Vector3f discCenter = new Vector3f(bigR, 0, 0);
		Vector3f discRadialVector = new Vector3f(smallR, 0, 0);
		Matrix3f rotY = new Matrix3f();
		rotY.rotY(2*MathFloat.PI/smallResolution);
		Vector3f[] firstCrossSection = new Vector3f[smallResolution];
		for (int i = 0; i < smallResolution; i++) {
			Vector3f discPoint = new Vector3f();
			discPoint.add(discRadialVector, discCenter);
			vertices.appendVector(discPoint);
			colors.appendVector(new Color3f(i % 2, i % 2 , i % 2));
			firstCrossSection[i] = discPoint;
			rotY.transform(discRadialVector);
		}
		//construct other cross-sections out of first
		Matrix3f rotZ = new Matrix3f();
		rotZ.rotZ(2*MathFloat.PI/bigResolution);
		for (int i = 1; i < bigResolution; i++) {
			for (Vector3f fcsVector: firstCrossSection) {
				rotZ.transform(fcsVector);
				vertices.appendVector(fcsVector);
				colors.appendVector(new Color3f(i % 2, i % 2 , i % 2));
			}
		}
		int n = bigResolution*smallResolution;
		for (int k = 0; k < n; k+=smallResolution) {
			for (int i = k; i < k + smallResolution; i++) {
				addQuadrangle(i, 
						(i + smallResolution) % n, 
						((i + 1) % smallResolution + k + smallResolution) % n, 
						(i+1) % smallResolution + k);
			}
		}
		addIndicesList(indicesList);
		addElement(vertices.getFinishedArray(), VertexData.Semantic.POSITION, 3);
		addElement(colors.getFinishedArray(), VertexData.Semantic.COLOR, 3);
	}
}
