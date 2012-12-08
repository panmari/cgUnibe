package task6;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import jogamp.graph.math.MathFloat;

import task1.AbstractShape;

public class RotationBody extends AbstractShape {

	/**
	 * All rotation bodies are rotated around y axis.
	 * @param curve
	 * @param rotationSteps
	 */
	public RotationBody(BezierCurve curve, int rotationSteps) {
		super(curve.getCurveMesh().length*rotationSteps);
		int n = curve.getCurveMesh().length*rotationSteps;
		int curveLength = curve.getCurveMesh().length;
		Matrix4f stepRot = new Matrix4f();
		stepRot.rotY(MathFloat.PI*2/rotationSteps);
		Matrix4f currentRot = new Matrix4f();
		currentRot.setIdentity();
		for (int i = 0; i < rotationSteps; i++) {
			for (int j = 0; j < curve.getCurveMesh().length; j++) {
				Point4f p = curve.getCurveMesh()[j];
				Vector4f tangent = curve.getTangents()[j];
				Point3f currentVertex = new Point3f(p.x, p.y, p.z);
				Vector3f currentNormal = new Vector3f(-tangent.y, tangent.x, 0);
				currentRot.transform(currentVertex);
				currentRot.transform(currentNormal);
				vertices.appendVector(currentVertex);
				normals.appendVector(currentNormal);
				if (j < curve.getCurveMesh().length ) {
					int currentIndex = i*rotationSteps + j;
					addQuadrangle(currentIndex % n, (currentIndex + 1) % n, 
							(currentIndex + 1 + curveLength) % n, 
							(currentIndex + curveLength) % n);
					}
			}
			currentRot.mul(stepRot);
		}
		addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
		addElement(normals.getFinishedArray(), Semantic.NORMAL, 3);
		addIndicesList(indicesList);
	}
}
