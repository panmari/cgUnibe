package graphSceneManager;

import javax.vecmath.Matrix4f;

import jrtr.PointLight;

public class LightNode extends Leaf {

	private PointLight l;

	public LightNode(PointLight l, Matrix4f t) {
		super(t);
		this.l = l;
	}
	
	public LightNode(PointLight l) {
		super();
		this.l = l;
	}
	
	public PointLight getLight() {
		return l;
	}
	

}
