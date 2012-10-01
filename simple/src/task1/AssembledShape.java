package task1;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public abstract class AssembledShape {

	protected List<Shape> shapes;
	
	public void transform(Matrix4f matrix) {
		for (Shape s: shapes) {
			s.setTransformation(matrix);
		}
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}
}
