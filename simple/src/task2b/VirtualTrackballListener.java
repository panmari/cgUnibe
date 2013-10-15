package task2b;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.jogamp.opengl.math.FloatUtil;
import jrtr.RenderPanel;
import jrtr.Shape;

public class VirtualTrackballListener implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private Vector3f initialPoint;
	private Shape shape;
	private RenderPanel renderPanel;
	
	public VirtualTrackballListener(Shape shape, RenderPanel renderPanel) {
		this.shape = shape;
		this.renderPanel = renderPanel;
	}
	
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			shape.getTransformation().setIdentity();
			renderPanel.getCanvas().repaint();
			return;
		}
		if (initialPoint == null && !containsNan(convertToSphere(e)))
			initialPoint = convertToSphere(e);
	}
	public void mouseReleased(MouseEvent e) {
		initialPoint = null;
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseClicked(MouseEvent e) {
		
	}
	
	private Vector3f convertToSphere(MouseEvent e) {
		int width = renderPanel.getCanvas().getWidth();
		int height = renderPanel.getCanvas().getHeight();
		float uniformScale = Math.min(width, height);
		float uniformTranslationX = width/uniformScale;
		float uniformTranslationY = height/uniformScale;
		//TODO: use uniform scale
		float x = (float) 2*e.getX()/uniformScale - uniformTranslationX;
		float y = uniformTranslationY - (float)2*e.getY()/uniformScale;
		float z = FloatUtil.sqrt(1 - x*x - y*y);
		Vector3f p = new Vector3f(x, y, z);
		p.normalize(); //is this really necessary?
		return p;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Vector3f newPoint = convertToSphere(e);
		if (initialPoint == null || containsNan(newPoint)) //if out of window
			return;
		Vector3f axis = new Vector3f();
		axis.cross(initialPoint, newPoint);
		float theta = initialPoint.angle(newPoint);
		Matrix4f m = shape.getTransformation();
		Matrix4f rot = new Matrix4f();
		rot.setIdentity();
		rot.setRotation(new AxisAngle4f(axis.x, axis.y, axis.z, theta));
		m.mul(rot, m);
		initialPoint = newPoint;
		renderPanel.getCanvas().repaint(); 
	}
	
	private boolean containsNan(Vector3f newPoint) {
		float p[] = new float[3];
		newPoint.get(p);
		for (float f: p) {
			if (new Float(f).isNaN())
				return true;
		}
		return false;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	/**
	 * Simulates zooming by mouse wheel
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		Matrix4f m = shape.getTransformation();
		m.setScale(m.getScale() - 0.1f*e.getWheelRotation());
		renderPanel.getCanvas().repaint(); 
	}
}