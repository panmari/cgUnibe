package task2b;

import jogamp.graph.math.MathFloat;
import jrtr.*;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.vecmath.*;

import com.jogamp.graph.math.Quaternion;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class VirtualTrackball
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float angle;
	private static Point3f cop;
	private static Point3f lap;
	private static Vector3f up;
	private static Camera c;
	private static Matrix4f trans;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. 
	 */ 
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
	
			// Register a timer task
		    Timer timer = new Timer();
		    angle = 0.01f;
		    //timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}

	/**
	 * A timer task that generates an animation. This task triggers
	 * the redrawing of the 3D scene every time it is executed.
	 */
	public static class AnimationTask extends TimerTask
	{
		public void run()
		{
			
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
    	
		
		private Vector3f initialPoint;
		private boolean exited;
		
		public void mousePressed(MouseEvent e) {
			if (initialPoint == null)
				initialPoint = convertToSphere(e);
    	}
    	public void mouseReleased(MouseEvent e) {
    		if (exited)
    			return;
    		Vector3f newPoint = convertToSphere(e);
    		Vector3f axis = new Vector3f();
    		axis.cross(initialPoint, newPoint);
    		float theta = initialPoint.angle(newPoint);
    		Matrix4f m = shape.getTransformation();
    		Matrix4f rot = new Matrix4f();
    		rot.setIdentity();
    		rot.setRotation(new AxisAngle4f(axis.x, axis.y, axis.z, theta));
    		m.mul(rot, m);
    		renderPanel.getCanvas().repaint(); 
    		initialPoint = null;
    	}
    	public void mouseEntered(MouseEvent e) {
    		exited = false;
    	}
    	public void mouseExited(MouseEvent e) {
    		exited = true;
    	}
    	public void mouseClicked(MouseEvent e) {
    		
    	}
    	
    	private Vector3f convertToSphere(MouseEvent e) {
    		float x = (float) 2*e.getX()/renderPanel.getCanvas().getWidth() - 1;
    		float y = 1 - (float)2*e.getY()/renderPanel.getCanvas().getHeight();
    		float z = MathFloat.sqrt(1 - x*x - y*y);
    		Vector3f p = new Vector3f(x, y, z);
    		p.normalize(); //is this really necessary?
    		return p;
    	}
	}
	
	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
		
		// Make a scene manager and add the object
		sceneManager = new SimpleSceneManager(new Camera(), new Frustum());
		shape = new Shape(ObjReader.read("teapot.obj", 2));
		sceneManager.addShape(shape);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
	    renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());
		   	    	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}