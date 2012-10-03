package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public abstract class AssembledShape {

	protected List<MovingShape> shapes = new ArrayList<MovingShape>();
	
	public void transform(Matrix4f matrix) {
		for (Shape s: shapes) {
			s.setTransformation(matrix);
		}
	}
	
	public List<MovingShape> getShapes() {
		return shapes;
	}
}
