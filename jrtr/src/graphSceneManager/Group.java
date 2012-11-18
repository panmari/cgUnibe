package graphSceneManager;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix4f;

public abstract class Group implements Node {

	List<Node> children = new LinkedList<Node>();
	
	public void addChild(Node... nodes) {
		for (Node n: nodes)
			children.add(n);
	}
	
	public boolean removeChild(Node n) {
		return children.remove(n);
	}
	
	public List<Node> getChildren() {
		return children;
	}
}
