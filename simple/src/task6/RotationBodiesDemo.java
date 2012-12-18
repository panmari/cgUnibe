package task6;
import jrtr.*;
import jrtr.graphSceneManager.GraphSceneManager;
import jrtr.graphSceneManager.LightNode;
import jrtr.graphSceneManager.Node;
import jrtr.graphSceneManager.ShapeNode;
import jrtr.graphSceneManager.TransformGroup;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

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
	static GraphSceneManager sceneManager;
	static List<Shape> shapes = new ArrayList<Shape>();
	static float angle;
	private static ShapeNode egg;
	private static Shape eggHolder;
	private static ShapeNode table;
	private static ShapeNode hat;

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
	
			
			Texture eggTexture = renderContext.makeTexture();
			Texture metal = renderContext.makeTexture();
			Texture pink = renderContext.makeTexture();
			try {
				eggTexture.load("../jrtr/textures/egg.jpg");
				metal.load("../jrtr/textures/metal.jpg");
				pink.load("../jrtr/textures/pink_cloth.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			egg.getShape().setMaterial(new Material(eggTexture));
			Material metalMat = new Material(metal);
			metalMat.setSpecularReflectionCoefficient(1);
			metalMat.setDiffuseReflectionCoefficient(.4f);
			eggHolder.setMaterial(metalMat);
			hat.getShape().setMaterial(new Material(pink));
			
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
		
		
		Matrix4f t = new Matrix4f();

		TransformGroup root = new TransformGroup();
		Point2f[] controlPoints;
		
		controlPoints = new Point2f[4];
		controlPoints[0] = new Point2f(3,0);
		controlPoints[1] = new Point2f(-1,.5f);
		controlPoints[2] = new Point2f(5f,3);
		controlPoints[3] = new Point2f(0,3);

		BezierCurve hatCurve = new BezierCurve(1, controlPoints, 40);
		
		hat = new ShapeNode(new Shape(new RotationBody(hatCurve, 10)));
		t.setIdentity();
		t.setTranslation(new Vector3f(-3, 12, 1));
		hat.setTransformation(t);
		
		controlPoints = new Point2f[7];
		controlPoints[0] = new Point2f(3,0);
		controlPoints[1] = new Point2f(.5f,0);
		controlPoints[2] = new Point2f(.5f,3);
		controlPoints[3] = new Point2f(.5f,4);
		controlPoints[4] = new Point2f(2f,5);
		controlPoints[5] = new Point2f(2f,6);
		controlPoints[6] = new Point2f(2f,7);

		BezierCurve eggHolderCurve = new BezierCurve(2, controlPoints, 40);
		
		eggHolder = new Shape(new RotationBody(eggHolderCurve, 40));
		TransformGroup eggStuff = new TransformGroup();
		eggStuff.addChild(new ShapeNode(eggHolder));
		
		controlPoints = new Point2f[4];
		controlPoints[0] = new Point2f(0,0);
		controlPoints[1] = new Point2f(3.5f,0);
		controlPoints[2] = new Point2f(2f,6);
		controlPoints[3] = new Point2f(0,6);
		BezierCurve eggCurve = new BezierCurve(1, controlPoints, 40);
		egg = new ShapeNode(new Shape(new RotationBody(eggCurve, 40)));
		t = new Matrix4f();
		t.setIdentity();
		t.setTranslation(new Vector3f(0,5,0));
		egg.setTransformation(t);
		eggStuff.addChild(egg);
		
		t = new Matrix4f();
		t.setIdentity();
		t.setTranslation(new Vector3f(3,12,0));
		t.setScale(.5f);
		eggStuff.setTransformation(t);
		
		TransformGroup tableGroup = new TransformGroup();
		tableGroup.addChild(eggStuff);
		
		controlPoints = new Point2f[16];
		controlPoints[0] = new Point2f(5,0);
		controlPoints[1] = new Point2f(5,.5f);
		controlPoints[2] = new Point2f(3,.5f);
		controlPoints[3] = new Point2f(.5f,2);
		controlPoints[4] = new Point2f(.5f,5);
		controlPoints[5] = new Point2f(.5f,8);
		controlPoints[6] = new Point2f(2,11);
		controlPoints[7] = new Point2f(5,11);
		controlPoints[8] = new Point2f(8,11);
		controlPoints[9] = new Point2f(11,11);
		controlPoints[10] = new Point2f(11,12);
		controlPoints[11] = new Point2f(11,12);
		controlPoints[12] = new Point2f(11,12);
		controlPoints[13] = new Point2f(8,12);
		controlPoints[14] = new Point2f(5,12);
		controlPoints[15] = new Point2f(0,12);
		BezierCurve tableCurve = new BezierCurve(5, controlPoints, 40);
		table = new ShapeNode(new Shape(new RotationBody(tableCurve, 40)));

		tableGroup.addChild(table, hat);
		
		root.addChild(tableGroup);
		sceneManager = new GraphSceneManager(root);
		
		root.addChild(new LightNode((new PointLight(new Color3f(1,0,0), 40, new Point3f(10,20,0)))));
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
