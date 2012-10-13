package task2c;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.Camera;

public class CameraInputListener implements KeyListener, MouseMotionListener {

	private Camera c;
	private Matrix4f rotLeft, rotRight;
	CameraInputListener(Camera c) {
		this.c = c;
		rotLeft = new Matrix4f();
		rotLeft.rotY(MathFloat.PI/2); //by 90 degrees
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
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
