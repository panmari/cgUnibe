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
	 * Draws only the vertices of the given shape in white. Normals and indices are ignored.
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

	/**
	 * Draws all triangles given in <code>indices</code> of the given shape.
	 * Ignores normals.
	 * @param shape that is about to be drawn
	 * @param t accumulated transformation matrix for this shape
	 */
	private void drawTrianglesSeparately(Shape shape, Matrix4f t) {
		VertexData vertexData = shape.getVertexData();
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
						p = getPointAt(ve.getData(), indices[i]);
						t.transform(p);
						positions[k] = p;
						k++; //increment k here, bc color and normal might be missing
						break;
					case COLOR:
						colors[k] = getColorAt(ve, indices[i]);;
						break;
					case NORMAL:
						//dont care
						//normals[k] = getPointAt(ve.getData(), indices[i]);;
						break;
				
				}
			}
			if (k == 3) {
				rasterizeTriangle(positions, colors, normals);
				k = 0;
			}
		}
	}

	private void rasterizeTriangle(Point4f[] positions, Color3f[] colors, Point4f[] normals) {
		Matrix3f alphabetagamma = new Matrix3f();
		 //start with smallest possible bounding rectangle
		Point topLeft = new Point(width - 1, height - 1);
		Point botRight = new Point(0, 0);
		float[] oneOverWarray = new float[3];
		for (int i = 0; i < 3; i++) {
			//TODO: do I really need/want to make this division?
			//this defeats the fckin purpose of ... everything!
			topLeft.x = (int) Math.min(topLeft.x, positions[i].x/positions[i].w);
			topLeft.y = (int) Math.min(topLeft.y, positions[i].y/positions[i].w);
			botRight.x = (int) Math.max(botRight.x, positions[i].x/positions[i].w);
			botRight.y = (int) Math.max(botRight.y, positions[i].y/positions[i].w);
			float[] row = { positions[i].x, positions[i].y, positions[i].w };
			alphabetagamma.setRow(i, row);
			oneOverWarray[i] = 1/positions[i].w;
		}
		alphabetagamma.invert();
		
		validifyBoundingRectangle(topLeft, botRight);
		
		for (int y = topLeft.y; y <= botRight.y; y++) {
			for (int x = topLeft.x; x <= botRight.x; x++) {
				Vector3f edgeCoefficients = getEdgeCoefficients(x, y, alphabetagamma);
				if (edgeCoefficients != null) {
					float zvalue = new Vector3f(oneOverWarray).dot(edgeCoefficients);
					if (betterZvalue(x, y, zvalue)) {
						zBuffer[x][y] = zvalue;
						Color c = makeColor(edgeCoefficients, colors, zvalue);
						colorBuffer.setRGB(x, colorBuffer.getHeight() - y - 1, c.getRGB());
					}
				}
			}
		}
	}

	private Color makeColor(Vector3f edgeCoefficients, Color3f[] colors, float zvalue) {
		float[] resultingColor = new float[3];
		float[][] channelSplitColors = new float[3][3];
		for (int i = 0; i < 3; i++) {
			float[] channels = new float[3];
			colors[i].get(channels);
			channelSplitColors[i] = channels;
		}
		for (int i = 0; i < 3; i++) {
			Vector3f channel = new Vector3f(channelSplitColors[0][i], 
					channelSplitColors[1][i], 
					channelSplitColors[2][i]);
			resultingColor[i] += edgeCoefficients.dot(channel);
		}
		Color3f c = new Color3f(resultingColor);
		c.scale(1/(edgeCoefficients.x + edgeCoefficients.y + edgeCoefficients.z));
		//c.scale(zvalue);
		return c.get();
	}

	private boolean betterZvalue(int x, int y, float zvalue) {
		if (zBuffer[x][y] == 0)
			return true;
		else return zBuffer[x][y] < zvalue;
	}

	private void validifyBoundingRectangle(Point topLeft, Point botRight) {
		topLeft.x = Math.max(0, topLeft.x);
		topLeft.y = Math.max(0, topLeft.y);
		botRight.x = Math.min(width - 1, botRight.x);
		botRight.y = Math.min(height - 1, botRight.y);
	}

	private Vector3f getEdgeCoefficients(int x, int y, Matrix3f alphabetagamma) {
		Vector3f abgVector = new Vector3f();
		float[] coeffs = new float[3];
		for (int i = 0; i < 3; i++) {
			alphabetagamma.getColumn(i, abgVector);
			float coeff = abgVector.dot(new Vector3f(x, y, 1));
			if (coeff < 0)
				return null;
			else coeffs[i] = coeff;
		}
		return new Vector3f(coeffs);
	}

	/**
	 * Returns a Point4f of the index'th vector saved in data,
	 * assuming that all vectors in data have 3 coordinates.
	 * </br>
	 * The w-value is always 1.
	 * @param index
	 * @return
	 */
	public Point4f getPointAt(float[] data, int index) {
		return new Point4f(data[index*3], data[index*3 + 1], data[index*3 + 2], 1);
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
