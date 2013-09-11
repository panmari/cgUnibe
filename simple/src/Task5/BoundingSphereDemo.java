package Task5;

import jrtr.*;
import jrtr.graphSceneManager.GraphSceneManager;
import jrtr.graphSceneManager.ShapeNode;
import jrtr.graphSceneManager.TransformGroup;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.vecmath.*;

import task2c.FlyingCameraInputListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class BoundingSphereDemo
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SceneManagerInterface sceneManager;
	static Shape shape;
	static float angle;
	static JFrame jframe;
	
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
			
			System.out.println("loading texture");
			Texture chessBoard = renderContext.makeTexture();
			Texture woodTex = renderContext.makeTexture();
			Texture plant = renderContext.makeTexture();
			
			Shader diffuse = renderContext.makeShader();
			Shader tron = renderContext.makeShader();
			try {
				chessBoard.load("../jrtr/textures/chessboard2.jpg");
				woodTex.load("../jrtr/textures/wood.jpg");
				plant.load("../jrtr/textures/plant.jpg");
				diffuse.load("../jrtr/shaders/diffuse.vert", "../jrtr/shaders/diffuse.frag");
				tron.load("../jrtr/shaders/tron.vert", "../jrtr/shaders/tron.frag");
			} catch (IOException e) {
				System.out.println("error loading texture");
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		private int frame, nframes = 10;
		private long oldTime = System.nanoTime();

		public void run()
		{
			if(frame == nframes){
			       long newTime = System.nanoTime();
			       float timeElapsed = (newTime-oldTime)/1000000000f; //convert nanosec to sec
			       oldTime = newTime;
			       jframe.setTitle("FPS: " +  nframes/timeElapsed);
			       frame=0;
			} else
			       frame++;

    		renderPanel.getCanvas().repaint(); 
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
		shape = new Shape(ObjReader.read("teapot_tex.obj", 1));
		
		TransformGroup root = new TransformGroup();
		for (int x = 0; x < 20; x++)
			for (int z = 0; z < 20; z++){
				ShapeNode s = new ShapeNode(shape);
				s.getTransformation().setTranslation(new Vector3f(x*2, 0, z*2));
				root.addChild(s);
			}
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		sceneManager = new GraphSceneManager(root);
		// Make the main window of this application and add the renderer to it
		jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

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
