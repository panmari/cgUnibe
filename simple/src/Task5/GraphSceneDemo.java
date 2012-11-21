package Task5;

import jogamp.graph.math.MathFloat;
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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class GraphSceneDemo
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SceneManagerInterface sceneManager;
	static Shape shape;
	static float angle;
	private static TransformGroup body;
	private static TransformGroup leftArm;
	private static TransformGroup leftLeg;
	private static TransformGroup rightLeg;
	
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
		private int counter;

		public void run()
		{
    		counter++;
			// Update transformation
    		Matrix4f t = body.getTransformation();
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(angle);
    		Matrix4f rotXinvert = new Matrix4f(rotX);
    		rotXinvert.invert();
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(angle);
    		if (counter % 250 < 125) {
    			rightLeg.getTransformation().mul(rotX);
    			leftLeg.getTransformation().mul(rotXinvert);
    		} else {
    			rightLeg.getTransformation().mul(rotXinvert);
    			leftLeg.getTransformation().mul(rotX);
    		}
    		
    		leftArm.getTransformation().mul(rotX);
    		t.mul(rotY, t);
    		
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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
		// Make a simple geometric object: a cube
		
		// The vertex positions of the cube
		float v[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1,		// front face
			         -1,-1,-1, -1,-1,1, -1,1,1, -1,1,-1,	// left face
				  	 1,-1,-1,-1,-1,-1, -1,1,-1, 1,1,-1,		// back face
					 1,-1,1, 1,-1,-1, 1,1,-1, 1,1,1,		// right face
					 1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		// top face
					-1,-1,1, -1,-1,-1, 1,-1,-1, 1,-1,1};	// bottom face

		// The vertex colors
		float c[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1};

		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1,
		         	-1,0,0, -1,0,0, -1,0,0, -1,0,0,
			  	    0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1, 
				    1,0,0, 1,0,0, 1,0,0, 1,0,0,
				    0,1,0, 0,1,0, 0,1,0, 0,1,0, 
				    0,-1,0, 0,-1,0, 0,-1,0,  0,-1,0};  
		
		float uv[] = {0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1};
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = new VertexData(24);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(uv, VertexData.Semantic.TEXCOORD, 2);
		
		// The triangles (three vertex indices for each triangle)
		int indices[] = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22};	// bottom face

		vertexData.addIndices(indices);
				
		// Make a scene manager and add the object
		
		
		shape = new Shape(ObjReader.read("teapot_tex.obj", 3));
		
		
		ShapeNode torus = new ShapeNode(new Shape(new Cylinder(5, 1, 20)));
		ShapeNode head = new ShapeNode(new Shape(new Torus(.5f, .2f, 20, 20)));
		Matrix4f m = new Matrix4f();
		m.rotX(MathFloat.PI/2);
		torus.getTransformation().mul(m);
		head.getTransformation().setTranslation(new Vector3f(0, 3.5f, 0));
		TransformGroup upperBody = new TransformGroup();
		upperBody.addChild(torus);
		
		leftArm = makeArm(1.5f);
		TransformGroup rightArm = makeArm(1f);

		Matrix4f rot = new Matrix4f();
		rot.rotX(MathFloat.PI/4);
		leftArm.getTransformation().setTranslation(new Vector3f(-1, 2.5f, 0f));
		rightArm.getTransformation().setTranslation(new Vector3f(1, 2.5f, 0f));
		
		leftLeg = makeArm(2f);
		rightLeg = makeArm(2f);
		Matrix4f rotX90 = new Matrix4f();
		rotX90.rotX(-MathFloat.PI/2 + 60*0.01f);
		leftLeg.getTransformation().setTranslation(new Vector3f(-1, -2, 0));
		leftLeg.getTransformation().mul(rotX90);
		rightLeg.getTransformation().setTranslation(new Vector3f(1, -2, 0));
		rotX90.rotX(-MathFloat.PI/2 - 60*0.01f);
		rightLeg.getTransformation().mul(rotX90);
		PointLight light = new PointLight(new Color3f(1,0,0), 20f, new Point3f(0,0,-6));
		leftArm.addChild(new LightNode(light));
		upperBody.addChild(leftArm, rightArm, leftLeg, rightLeg);
		
		body = new TransformGroup();
		body.getTransformation().setTranslation(new Vector3f(8, 0, 0));
		body.addChild(upperBody);
		body.addChild(head);

		
		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		
		Node rootNode =  body;
		Camera cam = new Camera(new Point3f(0,0,-20), new Point3f(0,0,0), new Vector3f(0,1,0));
		sceneManager = new GraphSceneManager(rootNode, cam, new Frustum());
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
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

	private static TransformGroup makeArm(float size) {
		Shape armPart = new Shape(new Cylinder(size, .2f, 20));
		TransformGroup arm = new TransformGroup();
		ShapeNode upper = new ShapeNode(armPart);
		upper.getTransformation().setTranslation(new Vector3f(0,0, -size));
		TransformGroup lower = new TransformGroup();
		lower.addChild(new ShapeNode(armPart));
		lower.getTransformation().setTranslation(new Vector3f(0,0,-size*2 - .5f));
		arm.addChild(upper);
		arm.addChild(lower);
		return arm;
	}
}
