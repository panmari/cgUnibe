package task4;

import jrtr.*;

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
public class DiffuseShaderDemo
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float angle;
	static Texture chessBoard = null;
	private static Shape teapot;
	private static Shape cubeOne;
	private static Shape cylinder;
	
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
			chessBoard = renderContext.makeTexture();
			Texture woodTex = renderContext.makeTexture();
			Texture plant = renderContext.makeTexture();
			
			Shader diffuse = renderContext.makeShader();
			Shader toon = renderContext.makeShader();
			try {
				chessBoard.load("../jrtr/textures/chessboard2.jpg");
				woodTex.load("../jrtr/textures/wood.jpg");
				plant.load("../jrtr/textures/plant.jpg");
				diffuse.load("../jrtr/shaders/diffuse.vert", "../jrtr/shaders/diffuse.frag");
				toon.load("../jrtr/shaders/toon.vert", "../jrtr/shaders/toon.frag");
			} catch (IOException e) {
				System.out.println("error loading texture");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Material wood = new Material(woodTex, diffuse);
			wood.setPhongExponent(40);
			wood.setSpecularReflectionCoefficient(0);
			wood.setDiffuseReflectionCoefficient(1);
			cubeOne.setMaterial(wood);
			
			Material glossy = new Material(woodTex, diffuse, 0);
			glossy.setDiffuseReflectionCoefficient(0.5f);
			glossy.setSpecularReflectionCoefficient(40);
			glossy.setPhongExponent(50);
			teapot.setMaterial(glossy);
			
			Material test = new Material();
			test.setGlossMap(chessBoard);
			shape.setMaterial(test);
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
			// Update transformation
    		Matrix4f t = shape.getTransformation();
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(angle);
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(angle);
    		//t.mul(rotX);
    		t.mul(rotY);
    		
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
		sceneManager = new SimpleSceneManager(new Camera(new Point3f(0,0, 10), new Point3f(0,0,0), new Vector3f(0,1,0)), new Frustum());
		teapot = new Shape(ObjReader.read("teapot_tex.obj", 1));
		Matrix4f t = new Matrix4f();
		t.setIdentity();
		t.setTranslation(new Vector3f(-3, 0, 2));
		teapot.setTransformation(t);
		sceneManager.addShape(teapot);
		
		cubeOne = new Shape(vertexData);
		t = new Matrix4f();
		t.setIdentity();
		t.setTranslation(new Vector3f(3, 0, 0));
		cubeOne.setTransformation(t);
		sceneManager.addShape(cubeOne);
		shape = new Shape(vertexData);
		//shape.setMaterial(new Material(chessBoard));
		sceneManager.addShape(shape);
		
		cylinder = new Shape(new Torus(4, 1, 30, 30));
		cylinder.getTransformation().setTranslation(new Vector3f(0, 2, -3));
		
		sceneManager.addShape(cylinder);
		sceneManager.addPointLight(new PointLight(new Color3f(1,0,0), 10f, new Point3f(-3, 0, 15)));
		sceneManager.addPointLight(new PointLight(new Color3f(1,1,0), 10f, new Point3f(0, 4, 0)));

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
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
}
