package graphSceneManager;

import java.util.List;

import javax.vecmath.Matrix4f;

public abstract class Leaf implements Node {

	protected Matrix4f t;
	public Leaf() {
		this.t = new Matrix4f();
		t.setIdentity();
	}
	public Leaf(Matrix4f t) {
		this.t = t;
	}
	
	@Override
	public Matrix4f getTransformation() {
		return t;
	}

	public void setTransformation(Matrix4f t) {
		this.t = t;
	}
	
	@Override
	public List<Node> getChildren() {
		return null;
	}

}
