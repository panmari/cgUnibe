package task1;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;

public class Torus extends AbstractShape {

	public Torus(float bigR, float smallR, int bigResolution, int smallResolution) {
		super(100);
		
		Vector3f discCenter = new Vector3f(bigR, 0, 0);
		Vector3f discRadialVector = new Vector3f(smallR, 0, 0);
		Matrix3f rotY = new Matrix3f();
		rotY.rotY(2*MathFloat.PI/smallResolution);
		for (int i = 0; i < smallResolution; i++) {
			Vector3f discPoint = new Vector3f();
			discPoint.add(discRadialVector, discCenter);
			vertices.appendVector(discPoint);
			rotY.transform(discRadialVector);
		}
	}
}
