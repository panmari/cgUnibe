package task1;

import java.awt.Point;
import java.util.Arrays;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point2f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.VertexData;

public class Torus extends AbstractShape {

	
	private float bigR;
	private float smallR;
	private int bigResolution;
	private int smallResolution;

	/**
	 * Draws a torus with a zebra pattern.
	 * Lies in the x-y-plane, z-axis piercing through the hole in the middle.
	 * 
	 * @param bigR, radius to the middle of the ring
	 * @param smallR, "thickness" of the ring
	 * @param bigResolution, resolution in aspect to bigR
	 * @param smallResolution, resolution aspect to smallR
	 */
	public Torus(float bigR, float smallR, int bigResolution, int smallResolution) {
		super(bigResolution*smallResolution);
		this.bigR = bigR;
		this.smallR = smallR;
		this.bigResolution = bigResolution;
		this.smallResolution = smallResolution;
		
		Vector3f[] firstCrossSection = createFirstCrossSection();
		
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
		
		//add triangles
		int n = bigResolution*smallResolution;
		for (int k = 0; k < n; k+=smallResolution) {
			for (int i = k; i < k + smallResolution; i++) {
				int adjacentVector = (i + 1) % smallResolution + k;
				
				Vector3f first = new Vector3f(vertices.get(i));
				first.sub(new Vector3f(vertices.get((i + smallResolution) % n)));
				Vector3f second = new Vector3f(vertices.get(i));
				second.sub(new Vector3f(vertices.get(adjacentVector)));
				Vector3f normal = new Vector3f();
				normal.cross(first, second);
				normals.appendVector(normal);
				
				addQuadrangle(i, 
						(i + smallResolution) % n, 
						(adjacentVector + smallResolution) % n, 
						adjacentVector);
				Point2f p = new Point2f((i % smallResolution)*2/(float)smallResolution, k*2/(float)n);
				if (i % smallResolution > smallResolution/2f)
					p.x = 2 - p.x;
				if (k > n/2f)
					p.y = 2 - p.y;
				addTexel(i, p);
			}
		}
		addIndicesList(indicesList);
		
		addElement(normals.getFinishedArray(), VertexData.Semantic.NORMAL, 3);
		addElement(vertices.getFinishedArray(), VertexData.Semantic.POSITION, 3);
		addElement(colors.getFinishedArray(), VertexData.Semantic.COLOR, 3);
		addElement(texels, VertexData.Semantic.TEXCOORD, 2);
	}

	private Vector3f[] createFirstCrossSection() {
		Vector3f discCenter = new Vector3f(bigR, 0, 0);
		Vector3f discRadialVector = new Vector3f(-smallR, 0, 0);
		Matrix3f rotY = new Matrix3f();
		rotY.rotY(2*MathFloat.PI/smallResolution);
		Vector3f[] firstCrossSection = new Vector3f[smallResolution];
		for (int i = 0; i < smallResolution; i++) {
			Vector3f discPoint = new Vector3f();
			discPoint.add(discRadialVector, discCenter);
			vertices.appendVector(discPoint);
			colors.appendVector(new Color3f(0, 0, 0));
			firstCrossSection[i] = discPoint;
			rotY.transform(discRadialVector);
		}
		return firstCrossSection;
	}
}
