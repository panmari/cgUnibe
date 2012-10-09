package task2c;

import jogamp.graph.math.MathFloat;
import jrtr.*;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import javax.vecmath.*;

import task2b.VirtualTrackball.TrackballMouseListener;

import com.jogamp.graph.math.Quaternion;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class TestFractalLandscape
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
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
	public static class TrackballMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener
	{
		private Vector3f initialPoint;
		
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				shape.getTransformation().setIdentity();
				renderPanel.getCanvas().repaint();
				return;
			}
			if (initialPoint == null)
				initialPoint = convertToSphere(e);
    	}
    	public void mouseReleased(MouseEvent e) {
    		initialPoint = null;
    	}
    	public void mouseEntered(MouseEvent e) {
    	}
    	public void mouseExited(MouseEvent e) {
    	}
    	public void mouseClicked(MouseEvent e) {
    		
    	}
    	
    	private Vector3f convertToSphere(MouseEvent e) {
    		int width = renderPanel.getCanvas().getWidth();
    		int height = renderPanel.getCanvas().getHeight();
    		float uniformScale = Math.min(width, height);
    		float uniformTranslationX = width/uniformScale;
    		float uniformTranslationY = height/uniformScale;
    		//TODO: use uniform scale
    		float x = (float) 2*e.getX()/uniformScale - uniformTranslationX;
    		float y = uniformTranslationY - (float)2*e.getY()/uniformScale;
    		float z = MathFloat.sqrt(1 - x*x - y*y);
    		Vector3f p = new Vector3f(x, y, z);
    		p.normalize(); //is this really necessary?
    		return p;
    	}
		@Override
		public void mouseDragged(MouseEvent e) {
    		Vector3f newPoint = convertToSphere(e);
    		if (containsNan(newPoint)) //if out of window
    			return;
    		Vector3f axis = new Vector3f();
    		axis.cross(initialPoint, newPoint);
    		float theta = initialPoint.angle(newPoint);
    		Matrix4f m = shape.getTransformation();
    		Matrix4f rot = new Matrix4f();
    		rot.setIdentity();
    		rot.setRotation(new AxisAngle4f(axis.x, axis.y, axis.z, theta));
    		m.mul(rot, m);
    		renderPanel.getCanvas().repaint(); 
    		initialPoint = newPoint;
		}
		private boolean containsNan(Vector3f newPoint) {
			float p[] = new float[3];
			newPoint.get(p);
			for (float f: p) {
				if (new Float(f).isNaN())
					return true;
			}
			return false;
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		/**
		 * Simulates zooming by mouse wheel
		 */
		public void mouseWheelMoved(MouseWheelEvent e) {
			Matrix4f m = shape.getTransformation();
			m.setScale(m.getScale() + 0.1f*e.getWheelRotation());
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
		TrackballMouseListener l = new TrackballMouseListener();
	    renderPanel.getCanvas().addMouseListener(l);
		renderPanel.getCanvas().addMouseMotionListener(l);
		renderPanel.getCanvas().addMouseWheelListener(l);
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}
