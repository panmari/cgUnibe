package jrtr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple scene manager that stores objects in a linked list.
 */
public class SimpleSceneManager implements SceneManagerInterface {

	protected LinkedList<Shape> shapes;
	protected Camera camera;
	protected Frustum frustum;
	private List<PointLight> pointLights;
	
	public SimpleSceneManager(Camera c, Frustum f)
	{
		shapes = new LinkedList<Shape>();
		pointLights = new LinkedList<PointLight>();
		camera = c;
		frustum = f;
	}
	
	public SimpleSceneManager()
	{
		this(new Camera(), new Frustum());
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public Frustum getFrustum()
	{
		return frustum;
	}
	
	public void addShape(Shape shape)
	{
		shapes.add(shape);
	}
	
	public SceneManagerIterator iterator()
	{
		return new SimpleSceneManagerItr(this);
	}
	
	public void addPointLight(PointLight pl) {
		this.pointLights.add(pl);
	}
	
	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Iterator<PointLight> lightIterator()
	{
		return pointLights.iterator();
	}

	private class SimpleSceneManagerItr implements SceneManagerIterator {
		
		public SimpleSceneManagerItr(SimpleSceneManager sceneManager)
		{
			itr = sceneManager.shapes.listIterator(0);
		}
		
		public boolean hasNext()
		{
			return itr.hasNext();
		}
		
		public RenderItem next()
		{
			Shape shape = itr.next();
			// Here the transformation in the RenderItem is simply the 
			// transformation matrix of the shape. More sophisticated 
			// scene managers will set the transformation for the 
			// RenderItem differently.
			return new RenderItem(shape, shape.getTransformation());
		}
		
		ListIterator<Shape> itr;
	}
}
