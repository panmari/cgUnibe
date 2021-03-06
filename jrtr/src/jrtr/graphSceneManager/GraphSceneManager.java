package jrtr.graphSceneManager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import jrtr.BoundingSphere;
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
	public LinkedList<PointLight> lights;

	public GraphSceneManager(Node rootNode, Camera c, Frustum f) {
		this.rootNode = rootNode;
		this.camera = c;
		this.frustum = f;
		this.lights = new LinkedList<PointLight>();
	}
	
	public GraphSceneManager(Node rootNode) {
		this(rootNode, new Camera(), new Frustum());
	}
	
	@Override
	public SceneManagerIterator iterator() {
		return new GraphSceneManagerItr(rootNode);
	}

	@Override
	public Iterator<PointLight> lightIterator() {
		return lights.iterator();
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public Frustum getFrustum() {
		return frustum;
	}
	
	/**
	 * Has to be called before iterating lights
	 * @author Mazzzy
	 */
	private class GraphSceneManagerItr implements SceneManagerIterator {
		
		Stack<StackWrapper> sceneStack = new Stack<StackWrapper>();
		
		private GraphSceneManagerItr(Node rootNode)
		{
			lights = new LinkedList<PointLight>();
			sceneStack.push(new StackWrapper(rootNode, rootNode.getTransformation()));
		}
		
		public boolean hasNext()
		{
			return !sceneStack.empty();
		}
		
		public RenderItem next()
		{
			while (!sceneStack.isEmpty() && sceneStack.peek().node.getChildren() != null) {
				StackWrapper current = sceneStack.pop();
				for (Node node: current.node.getChildren()) {
					Matrix4f t = new Matrix4f();
					t.mul(current.t, node.getTransformation());
					
					if (ShapeNode.class.isInstance(node)) {
						Shape s = ((ShapeNode) node).getShape();
						Point3f c = new Point3f(s.getBoundingSphere().center);
						node.getTransformation().transform(c);
						camera.getCameraMatrix().transform(c);
						if (!frustum.isOutside(new BoundingSphere(c, s.getBoundingSphere().radius)))
							sceneStack.push(new StackWrapper(node, t));
					}
					else if (!LightNode.class.isInstance(node))
						sceneStack.push(new StackWrapper(node, t));
					else {
						PointLight nextLight = ((LightNode) node).getLight();
						Point3f newPosition = new Point3f(nextLight.getPosition());
						t.transform(newPosition);
						lights.add(new PointLight(nextLight.getColor(), nextLight.getRadiance(), newPosition));
					}
				}
			}
			
			StackWrapper next;
			if (!sceneStack.isEmpty())
				next = sceneStack.pop();
			else return null;
			Shape nextShape = ((ShapeNode) next.node).getShape();
			return new RenderItem(nextShape, next.t);
		}
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
