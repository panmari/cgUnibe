package graphSceneManager;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public class ShapeNode extends Leaf {
	
	private Shape shape;

	public ShapeNode(Shape shape) {
		this.shape = shape;
	}
	
	public Shape getShape() {
		return shape;
	}

	@Override
	public Matrix4f getTransformation() {
		return shape.getTransformation();
	}

	@Override
	public List<Node> getChildren() {
		return null;
	}

}
