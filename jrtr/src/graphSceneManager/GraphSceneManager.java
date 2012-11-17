package graphSceneManager;

import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.PointLight;
import jrtr.RenderItem;
import jrtr.SceneManagerInterface;
import jrtr.SceneManagerIterator;
import jrtr.Shape;
import jrtr.SimpleSceneManager;

public class GraphSceneManager implements SceneManagerInterface {

	private Node rootNode;
	private Camera camera;
	private Frustum frustum;

	public GraphSceneManager(Node rootNode, Camera c, Frustum f) {
		this.rootNode = rootNode;
		this.camera = c;
		this.frustum = f;
	}
	
	public GraphSceneManager(Node rootNode) {
		this(rootNode, new Camera(), new Frustum());
	}
	
	@Override
	public SceneManagerIterator iterator() {
		return new GraphSceneManagerItr(this);
	}

	@Override
	public Iterator<PointLight> lightIterator() {
		return Collections.emptyIterator();
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public Frustum getFrustum() {
		return frustum;
	}
	
	private class GraphSceneManagerItr implements SceneManagerIterator {
		
		Stack<Node> sceneStack = new Stack<Node>();
		
		public GraphSceneManagerItr(GraphSceneManager sceneManager)
		{
			sceneStack.push(sceneManager.rootNode);
		}
		
		public boolean hasNext()
		{
			return !sceneStack.empty();
		}
		
		public RenderItem next()
		{
			while (sceneStack.peek().getChildren() != null) {
				Node current = sceneStack.pop();
				for (Node n: current.getChildren()) {
					n.getTransformation().mul(current.getTransformation());
					sceneStack.push(n);
				}
			}
			ShapeNode nextShapeNode = (ShapeNode) sceneStack.pop();
			return new RenderItem(nextShapeNode.getShape(), nextShapeNode.getTransformation());
		}
		
	}
}
