package graphSceneManager;

import java.util.LinkedList;
import java.util.List;

public abstract class Group implements Node {

	List<Node> children = new LinkedList<Node>();
	
	public void addChild(Node n) {
		children.add(n);
	}
	
	public List<Node> getChildren() {
		return children;
	}
}
