package task2c;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Camera;

public class CameraInputListener implements KeyListener, MouseMotionListener {

	private Camera c;
	private Matrix4f rotLeft, rotRight;
	CameraInputListener(Camera c) {
		this.c = c;
		rotLeft = new Matrix4f();
		rotLeft.rotY(-0.03f);
		rotRight = new Matrix4f();
		rotRight.invert(rotLeft);
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent k) {
		Vector3f dir = new Vector3f();
		dir.sub(c.getLookAtPoint(), c.getCenterOfProjection());
		dir.normalize();
		switch (k.getKeyCode()) {
			case KeyEvent.VK_UP:
				c.getCenterOfProjection().add(dir);
				break;
			case KeyEvent.VK_DOWN:
				c.getCenterOfProjection().sub(dir);
				break;
			case KeyEvent.VK_LEFT:
				rotLeft.transform(c.getCenterOfProjection());
				break;
			case KeyEvent.VK_RIGHT:
				rotRight.transform(c.getCenterOfProjection());
				break;
			}
		c.update();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
