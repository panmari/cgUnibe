package graphSceneManager;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.PointLight;

public class LightNode extends Leaf {

	private Matrix4f t;
	private PointLight l;

	public LightNode(PointLight l, Matrix4f t) {
		this.l = l;
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
