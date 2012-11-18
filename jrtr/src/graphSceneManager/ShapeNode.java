package graphSceneManager;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public class ShapeNode extends Leaf {
	
	private Shape shape;
	private Matrix4f t;

	public ShapeNode(Shape shape) {
		this.shape = shape;
		t = new Matrix4f();
		t.setIdentity();
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void setTransformation(Matrix4f t) {
		this.t = t;
	}

	@Override
	public Matrix4f getTransformation() {
		return t;
	}

	@Override
	public List<Node> getChildren() {
		return null;
	}

}
