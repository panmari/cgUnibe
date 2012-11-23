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
	private BoundingSphere boundingSphere;
	
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
		this.boundingSphere = new BoundingSphere(vertexData);
		System.out.println(boundingSphere);
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
	
	public String toString() {
		return boundingSphere.toString();
	}

	private VertexData vertexData;
	private Matrix4f t;
	
	public class BoundingSphere {
		
		float radius;
		Point3f center;

		private BoundingSphere(VertexData vd) {
			VertexElement positions;
			int index = 0;
			do {
				positions = vd.getElements().get(index);
				index++;
			} while(positions.getSemantic() != Semantic.POSITION);
			
			float[] positionsArray = positions.getData();
			int n = positionsArray.length;
			center = new Point3f();
			for (int i = 0; i < n; i+=3) {
				center.x += positionsArray[i]/n;
				center.y += positionsArray[i+1]/n;
				center.z += positionsArray[i+2]/n;
			}
			
			for (int i = 0; i < n; i+=3) {
				Point3f current = new Point3f();
				current.x = positionsArray[i];
				current.y = positionsArray[i+1];
				current.z = positionsArray[i+2];
				if (current.distance(center) > radius) 
					radius = current.distance(center);
			}
		}
		
		public String toString() {
			return "r=" + radius + " c=" + center;
		}
	}
}
