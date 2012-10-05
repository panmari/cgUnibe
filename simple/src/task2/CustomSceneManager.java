package task2;

import java.util.LinkedList;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.Shape;
import jrtr.SimpleSceneManager;

public class CustomSceneManager extends SimpleSceneManager {
	
	public CustomSceneManager(Camera c, Frustum f)
	{
		shapes = new LinkedList<Shape>();
		camera = c;
		frustum = f;
	}
}
