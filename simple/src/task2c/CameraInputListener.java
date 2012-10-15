package task2c;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.Camera;

public class CameraInputListener implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

	private Camera c;
	private Matrix4f rot;
	private MouseEvent prevEvent;
	private final float factor = 0.01f;
	CameraInputListener(Camera c) {
		this.c = c;
		rot = new Matrix4f();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (prevEvent == null)
			prevEvent = e;
		int diffX = e.getX() - prevEvent.getX();
		rot.rotY(factor * diffX);
		rot.transform(c.getCenterOfProjection());
		int diffY = e.getY() - prevEvent.getY();
		rot.rotX(factor/5 * diffY);
		c.getCameraRotation().mul(rot);
		c.update();
		prevEvent = e;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent k) {
		Vector3f rightStep = new Vector3f(c.getCameraXAxis());
		switch (k.getKeyCode()) {
			case KeyEvent.VK_UP:
				step(-1);
				break;
			case KeyEvent.VK_DOWN:
				step(1);
				break;
			case KeyEvent.VK_LEFT:
				c.getCenterOfProjection().sub(rightStep);
				c.getLookAtPoint().sub(rightStep);
				break;
			case KeyEvent.VK_RIGHT:
				c.getCenterOfProjection().add(rightStep);
				c.getLookAtPoint().add(rightStep);
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		step(e.getWheelRotation());
	}

	/**
	 * positive value means backward, negative forward
	 * @param amount
	 */
	private void step(int amount) {
		Vector3f backStep = new Vector3f(c.getCameraZAxis());
		backStep.scale(amount);
		c.getCenterOfProjection().add(backStep);
		c.update();
	}

}
