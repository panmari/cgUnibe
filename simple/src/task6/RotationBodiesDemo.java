package task6;
import jrtr.*;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.vecmath.*;

import task1.Cylinder;
import task1.Torus;
import task2c.FlyingCameraInputListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class RotationBodiesDemo
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static List<Shape> shapes = new ArrayList<Shape>();
	static float angle;

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
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
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
			for (Shape shape: shapes) {
	    		Matrix4f t = shape.getTransformation();
	    		Matrix4f rotX = new Matrix4f();
	    		rotX.rotX(angle);
	    		Matrix4f rotY = new Matrix4f();
	    		rotY.rotY(angle);
	    		t.mul(rotX);
	    		t.mul(rotY);
	    		shape.setTransformation(t);
			}
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}
	}

	/**
	 * A mouse listener for the main window of this application. This can be
	 * used to process mouse events.
	 */
	public static class SimpleMouseListener implements MouseListener
	{
    	public void mousePressed(MouseEvent e) {}
    	public void mouseReleased(MouseEvent e) {}
    	public void mouseEntered(MouseEvent e) {}
    	public void mouseExited(MouseEvent e) {}
    	public void mouseClicked(MouseEvent e) {}
	}
	
	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 */
	public static void main(String[] args)
	{		
		
				
		// Make a scene manager and add the object
		sceneManager = new SimpleSceneManager();
		Point2f[] controlPoints = new Point2f[4];
		controlPoints[0] = new Point2f(0,0);
		controlPoints[1] = new Point2f(1,1);
		controlPoints[2] = new Point2f(2,2);
		controlPoints[3] = new Point2f(3,3);
		BezierCurve schraeg = new BezierCurve(1, controlPoints, 20);
		Shape c = new Shape(new RotationBody(schraeg, 20));
		//shapes.add(c);
		
		controlPoints = new Point2f[4];
		controlPoints[0] = new Point2f(0,0);
		controlPoints[1] = new Point2f(1,0);
		controlPoints[2] = new Point2f(1.5f,3);
		controlPoints[3] = new Point2f(0,3);
		BezierCurve eggCurve = new BezierCurve(1, controlPoints, 40);
		Shape egg = new Shape(new RotationBody(eggCurve, 40));
		shapes.add(egg);
		
		
		for (Shape shape: shapes)
			sceneManager.addShape(shape);
		sceneManager.addPointLight(new PointLight(new Color3f(1,0,0), 10, new Point3f(0,5,0)));
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
		// Add a mouse listener
		FlyingCameraInputListener l = new FlyingCameraInputListener(sceneManager.getCamera());
	    renderPanel.getCanvas().addMouseListener(l);
	    renderPanel.getCanvas().addMouseMotionListener(l);
	    renderPanel.getCanvas().addMouseWheelListener(l);
	    renderPanel.getCanvas().addKeyListener(l);
	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}
