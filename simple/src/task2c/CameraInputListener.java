package task2c;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.Camera;

public class CameraInputListener implements KeyListener, MouseMotionListener, MouseListener {

	private Camera c;
	private Matrix4f rot;
	private MouseEvent prevEvent;
	private final float factor = 0.0001f;
	CameraInputListener(Camera c) {
		this.c = c;
		rot = new Matrix4f();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (prevEvent == null)
			prevEvent = e;
		int diffX = e.getX() - prevEvent.getX();
		rot.rotY(0.01f * diffX);
		rot.transform(c.getCenterOfProjection());
		int diffY = e.getY() - prevEvent.getY();
		//todo implement rotation around camera x axis
		//idea: move change look-at-point?
		c.update();
		prevEvent = e;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent k) {
		Vector3f aheadStep = new Vector3f();
		aheadStep.sub(c.getLookAtPoint(), c.getCenterOfProjection());
		aheadStep.normalize();
		Vector3f leftStep = new Vector3f();
		leftStep.cross(c.getUpVector(), aheadStep);
		leftStep.normalize();
		switch (k.getKeyCode()) {
			case KeyEvent.VK_UP:
				c.getCenterOfProjection().add(aheadStep);
				break;
			case KeyEvent.VK_DOWN:
				c.getCenterOfProjection().sub(aheadStep);
				break;
			case KeyEvent.VK_LEFT:
				c.getCenterOfProjection().add(leftStep);
				c.getLookAtPoint().add(leftStep);
				break;
			case KeyEvent.VK_RIGHT:
				c.getCenterOfProjection().sub(leftStep);
				c.getLookAtPoint().sub(leftStep);
				break;
			}
		c.update();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		prevEvent = null;
	}

}
