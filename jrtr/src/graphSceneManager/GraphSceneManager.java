package graphSceneManager;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.PointLight;
import jrtr.RenderItem;
import jrtr.SceneManagerInterface;
import jrtr.SceneManagerIterator;
import jrtr.Shape;

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
		
		Stack<StackWrapper> sceneStack = new Stack<StackWrapper>();
		
		public GraphSceneManagerItr(GraphSceneManager sceneManager)
		{
			sceneStack.push(new StackWrapper(sceneManager.rootNode, sceneManager.rootNode.getTransformation()));
		}
		
		public boolean hasNext()
		{
			return !sceneStack.empty();
		}
		
		public RenderItem next()
		{
			while (sceneStack.peek().node.getChildren() != null) {
				StackWrapper current = sceneStack.pop();
				for (Node node: current.node.getChildren()) {
					Matrix4f t = new Matrix4f();
					t.mul(current.t, node.getTransformation());
					sceneStack.push(new StackWrapper(node, t));
				}
			}
			StackWrapper next = sceneStack.pop();
			Shape nextShape = ((ShapeNode) next.node).getShape();
			return new RenderItem(nextShape, next.t);
		}
		
		private class StackWrapper {
			private Node node;
			private Matrix4f t;

			StackWrapper(Node n, Matrix4f t) {
				this.node = n;
				this.t = t;
			}
		}
	}
}
