package task6;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.jogamp.opengl.math.FloatUtil;

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
		stepRot.rotY(FloatUtil.PI*2/rotationSteps);
		Matrix4f currentRot = new Matrix4f();
		currentRot.setIdentity();
		for (int i = 0; i < rotationSteps; i++) {
			for (int j = 0; j < curveLength; j++) {
				Point4f p = curve.getCurveMesh()[j];
				Vector4f tangent = curve.getTangents()[j];
				Point3f currentVertex = new Point3f(p.x, p.y, p.z);
				Vector3f currentNormal = new Vector3f(-tangent.y, tangent.x, 0);
				currentRot.transform(currentVertex);
				currentRot.transform(currentNormal);
				vertices.appendVector(currentVertex);
				normals.appendVector(currentNormal);
				//add surface:
				int currentIndex = i*curveLength + j;
				
				if (j < curveLength - 1) {
					addQuadrangle(currentIndex, 
							currentIndex + 1, 
							(currentIndex + curveLength + 1) % n, 
							(currentIndex + curveLength) % n);
				}
				
				//texel:
				Point2f texel = new Point2f(i*2/(float)rotationSteps, j/(float)curveLength);
				if (i > rotationSteps/2f)
					texel.x = 2 - texel.x;
				addTexel(currentIndex, texel);
			}
			currentRot.mul(stepRot);
		}
		addElement(vertices.getFinishedArray(), Semantic.POSITION, 3);
		addElement(normals.getFinishedArray(), Semantic.NORMAL, 3);
		addElement(this.texels, Semantic.TEXCOORD, 2);
		addIndicesList(indicesList);
	}
}
