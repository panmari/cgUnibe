package jrtr;

import jrtr.RenderContext;
import jrtr.VertexData.VertexElement;

import java.awt.Color;
import java.awt.image.*;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;


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
	private BufferedImage colorBuffer;
	private Matrix4f viewPortMatrix;
	private BufferedImage clearBuffer;
		
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
		Matrix4f cam = sceneManager.getCamera().getCameraMatrix();
		Matrix4f frust = sceneManager.getFrustum().getProjectionMatrix();
		SceneManagerIterator iter = sceneManager.iterator();
		Color white = new Color(255, 255, 255);
		while(iter.hasNext()) {
			RenderItem toRender = iter.next();
			float[] points = toRender.getShape().getVertexData().getElements().getLast().getData();
			for (int i = 0; i < points.length; i+=3) {
				Point4f v = new Point4f(points[i], points[i + 1], points[i + 2], 1);
				toRender.getT().transform(v);
				cam.transform(v);
				frust.transform(v);
				viewPortMatrix.transform(v);
				int x = Math.round(v.x / v.w);
				int y = Math.round(v.y / v.w);
				if (x >= 0 && y >= 0 && y < colorBuffer.getHeight() && x < colorBuffer.getWidth())
					colorBuffer.setRGB(colorBuffer.getWidth() - x - 1, colorBuffer.getHeight() - y - 1, white.getRGB());
			}
				
		}
			
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
