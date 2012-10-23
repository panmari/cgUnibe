package jrtr;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;

import jrtr.VertexData.VertexElement;


/**
 * A skeleton for a software renderer. It works in combination with
 * {@link SWRenderPanel}, which displays the output image. In project 3 
 * you will implement your own rasterizer in this class.
 * <p>
 * To use the software renderer, you will simply replace {@link GLRenderPanel} 
 * with {@link SWRenderPanel} in the user application.
 */
public class SWRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private float[][] zBuffer;
	private BufferedImage colorBuffer;
	private Matrix4f viewPortMatrix;
	private Matrix4f mergedDisplayMatrix;
	private BufferedImage clearBuffer;
	private int width;
	private int height;
		
	public void setSceneManager(SceneManagerInterface sceneManager)
	{
		this.sceneManager = sceneManager;
	}
	
	/**
	 * This is called by the SWRenderPanel to render the scene to the 
	 * software frame buffer.
	 */
	public void display()
	{
		if(sceneManager == null) return;
		
		beginFrame();
	
		SceneManagerIterator iterator = sceneManager.iterator();	
		while(iterator.hasNext())
		{
			draw(iterator.next());
		}		
		
		endFrame();
	}

	/**
	 * This is called by the {@link SWJPanel} to obtain the color buffer that
	 * will be displayed.
	 */
	public BufferedImage getColorBuffer()
	{
		return colorBuffer;
	}
	
	/**
	 * Set a new viewport size. The render context will also need to store
	 * a viewport matrix, which you need to reset here. 
	 */
	public void setViewportSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		viewPortMatrix = new Matrix4f();
		viewPortMatrix.setM00(width/2f);
		viewPortMatrix.setM11(height/2f);
		viewPortMatrix.setM22(1/2f);
		viewPortMatrix.setColumn(3, width/2f, height/2f, 1/2f, 1);
		colorBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		clearBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	}
		
	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame()
	{
		zBuffer = new float[width][height];
		mergedDisplayMatrix = new Matrix4f(viewPortMatrix);
		mergedDisplayMatrix.mul(sceneManager.getFrustum().getProjectionMatrix());
		mergedDisplayMatrix.mul(sceneManager.getCamera().getCameraMatrix());
		colorBuffer.setData( clearBuffer.getRaster() );

	}
	
	private void endFrame()
	{		
	}
	
	/**
	 * The main rendering method. You will need to implement this to draw
	 * 3D objects.
	 */
	private void draw(RenderItem renderItem)	
	{
		Matrix4f t = new Matrix4f(mergedDisplayMatrix);
		t.mul(renderItem.getT());
		drawTrianglesSeparately(renderItem.getShape(), t);
		//drawDotty(renderItem.getShape(), t);
			
	}
	/**
	 * Draws only the vertices of the shapes as dots. Normals and indices are ignored.
	 * @param shape
	 * @param t
	 */
	private void drawDotty(Shape shape, Matrix4f t) {
		float[] points = shape.getVertexData().getElements().getLast().getData();
		Color white = new Color(255, 255, 255);
		for (int i = 0; i < points.length; i+=3) {
			Point4f v = new Point4f(points[i], points[i + 1], points[i + 2], 1);
			t.transform(v);
			int x = Math.round(v.x / v.w);
			int y = Math.round(v.y / v.w);
			if (x >= 0 && y >= 0 && y < colorBuffer.getHeight() && x < colorBuffer.getWidth())
				colorBuffer.setRGB(x, colorBuffer.getHeight() - y - 1, white.getRGB());
		}
	}

	private void drawTrianglesSeparately(Shape toRender, Matrix4f t) {
		VertexData vertexData = toRender.getVertexData();
		Point4f[] positions = new Point4f[3];
		Color3f[] colors = new Color3f[3];
		Point4f[] normals = new Point4f[3];
		int k = 0; //keeps track of triangle
		int[] indices = vertexData.getIndices();
		for (int i = 0; i < indices.length; i++) {
			for(VertexElement ve: vertexData.getElements()) {
				Point4f p;
				switch (ve.getSemantic()) {
					case POSITION:
						p = getPointAt(ve, indices[i]);
						t.transform(p);
						positions[k] = p;
						k++; //increment k here, bc color and normal might be missing
						break;
					case COLOR:
						colors[k] = getColorAt(ve, indices[i]);;
						break;
					case NORMAL:
						//dont care
						normals[k] = getPointAt(ve, indices[i]);;
						break;
				
				}
			}
			if (k == 3) {
				rasterizeTriangle(positions, colors, normals);
				positions = new Point4f[3];
				colors = new Color3f[3];
				normals = new Point4f[3];
				k = 0;
			}
		}
	}

	private void rasterizeTriangle(Point4f[] positions, Color3f[] colors, Point4f[] normals) {
		Matrix3f alphabetagamma = new Matrix3f();
		Point topLeft = new Point(width - 1, height - 1);
		Point botRight = new Point(0, 0);
		for (int i = 0; i < 3; i++) {
			//TODO: do I really need/want to make this division?
			//this defeats the fckin purpose of ... everything!
			topLeft.x = (int) Math.min(topLeft.x, positions[i].x/positions[i].w);
			topLeft.y = (int) Math.min(topLeft.y, positions[i].y/positions[i].w);
			botRight.x = (int) Math.max(botRight.x, positions[i].x/positions[i].w);
			botRight.y = (int) Math.max(botRight.y, positions[i].y/positions[i].w);
			float[] row = { positions[i].x, positions[i].y, positions[i].w };
			alphabetagamma.setRow(i, row);
		}
		alphabetagamma.invert();
		
		validifyBoundingRectangle(topLeft, botRight);
		
		for (int y = topLeft.y; y <= botRight.y; y++) {
			for (int x = topLeft.x; x <= botRight.x; x++) {
				if (isInsideTriangle(x, y, alphabetagamma)) {
					colorBuffer.setRGB(x, colorBuffer.getHeight() - y - 1, getRGBof(colors[0]));
				}
			}
		}
	}

	private void validifyBoundingRectangle(Point topLeft, Point botRight) {
		topLeft.x = Math.max(0, topLeft.x);
		topLeft.y = Math.max(0, topLeft.y);
		botRight.x = Math.min(width - 1, botRight.x);
		botRight.y = Math.min(height - 1, botRight.y);
	}

	private int getRGBof(Color3f color3f) {
		return new Color(color3f.x, color3f.y, color3f.z).getRGB();
	}

	private boolean isInsideTriangle(int x, int y, Matrix3f alphabetagamma) {
		Vector3f abgVector = new Vector3f();
		for (int i = 0; i < 3; i++) {
			alphabetagamma.getColumn(i, abgVector);
			if (abgVector.dot(new Vector3f(x, y, 1)) < 0)
				return false;
		}
		return true;
	}

	public Point4f getPointAt(VertexElement ve, int index) {
		return new Point4f(ve.getData()[index*3], ve.getData()[index*3 + 1], ve.getData()[index*3 + 2], 1);
	}
	
	public Color3f getColorAt(VertexElement ve, int index) {
		return new Color3f(ve.getData()[index*3], ve.getData()[index*3 + 1], ve.getData()[index*3 + 2]);
	}
	
	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public Shader makeShader()	
	{
		return new SWShader();
	}

	/**
	 * Does nothing. We will not implement textures for the software renderer.
	 */
	public Texture makeTexture()
	{
		return new SWTexture();
	}
}
