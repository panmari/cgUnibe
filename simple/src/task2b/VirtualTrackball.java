package task2b;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.GLRenderPanel;
import jrtr.ObjReader;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.SWRenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;

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
		jframe.setSize(500, 900);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
		VirtualTrackballListener l = new VirtualTrackballListener(shape, renderPanel);
	    renderPanel.getCanvas().addMouseListener(l);
		renderPanel.getCanvas().addMouseMotionListener(l);
		renderPanel.getCanvas().addMouseWheelListener(l);
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
}
