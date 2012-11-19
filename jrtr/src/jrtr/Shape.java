package jrtr;
import javax.vecmath.*;

import jrtr.VertexData.Semantic;
import jrtr.VertexData.VertexElement;

/**
 * Represents a 3D shape. The shape currently just consists
 * of its vertex data. It should later be extended to include
 * material properties, shaders, etc.
 */
public class Shape {

	private Material material;
	/**
	 * Make a shape from {@link VertexData}.
	 *  
	 * @param vertexData the vertices of the shape.
	 */
	public Shape(VertexData vertexData)
	{
		this.vertexData = vertexData;
		this.material = new Material();
		t = new Matrix4f();
		t.setIdentity();
	}
	
	public VertexData getVertexData()
	{
		return vertexData;
	}
	
	public void setTransformation(Matrix4f t)
	{
		this.t = t;
	}
	
	public Matrix4f getTransformation()
	{
		return t;
	}
	
	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}

	/**
	 * To be implemented in the "Textures and Shading" project.
	 */
	public Material getMaterial()
	{
		return material;
	}

	private VertexData vertexData;
	private Matrix4f t;
	
	public class BoundingSphere {
		
		float radius;
		Point3f center;

		private BoundingSphere(VertexData vd) {
			VertexElement positions = vd.getElements().getFirst();
			if (positions.getSemantic() != Semantic.POSITION)
				throw new RuntimeException("Should be poaitions");
			float[] positionsArray = positions.getData();
			int n = positionsArray.length;
			center = new Point3f();
			for (int i = 0; i < n; i+=3) {
				center.x += positionsArray[i]/n;
				center.y += positionsArray[i+1]/n;
				center.z += positionsArray[i+2]/n;
			}
			//TODO: compute radius
			this.radius = radius;
		}
	}
}
