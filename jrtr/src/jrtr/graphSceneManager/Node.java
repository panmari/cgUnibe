package jrtr.graphSceneManager;

import java.util.List;

import javax.vecmath.Matrix4f;

public interface Node {
	public Matrix4f getTransformation();
	public List<Node> getChildren();
}
