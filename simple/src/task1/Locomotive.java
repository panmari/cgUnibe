package task1;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Shape;

public class Locomotive extends AssembledShape implements Actable{
	
	/**
	 * Could be easily built out of cylinders & torus
	 * (Even only cylinders)
	 */
	
	Vector3f[] wheelCoordinates = {	new Vector3f(0, 0, 0),
									new Vector3f(1, 0, 0),
									new Vector3f(0, 0, 1),
									new Vector3f(1, 0, 1)};

	public Locomotive(Vector3f direction) {
		shapes = new ArrayList<Shape>();
		for (int i = 0; i < 4; i++) {
			Wheel w = new Wheel(.5f, direction, 1);
			Matrix4f t = new Matrix4f();
			t.setTranslation(wheelCoordinates[i]);
			Matrix4f init = w.getTransformation();
			init.add(t);
			w.setTransformation(init);
			shapes.add(w);
		}
	}
	
	public void act() {
		for (Shape w: shapes)
			((Actable)w).act();
	}

}
