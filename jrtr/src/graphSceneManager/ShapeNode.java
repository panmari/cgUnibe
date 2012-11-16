package graphSceneManager;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public class ShapeNode extends Leaf {
	
	private Shape shape;
	private Matrix4f m;

	public ShapeNode(Shape shape, Matrix4f m) {
		this.shape = shape;
		this.m = m;
	}
	
	public Shape getShape() {
		return shape;
	}

}
