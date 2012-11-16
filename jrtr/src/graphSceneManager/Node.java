package graphSceneManager;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public interface Node {
	public Matrix4f getTransformation();
	public Shape getShape();
	public List<Node> getChildren();
}
