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
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.PointLight;
import jrtr.graphSceneManager.LightNode;

public class FlyingCameraInputListener implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

	private Camera c;
	private Matrix4f rot;
	private MouseEvent prevEvent;
	private final float factor = 0.01f;
	private PointLight light;
	
	public FlyingCameraInputListener(Camera c) {
		this.c = c;
		rot = new Matrix4f();
	}
	
	public FlyingCameraInputListener(Camera c, PointLight l) {
		this(c);
		this.light = l;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (prevEvent == null)
			prevEvent = e;
		Point3f lap = c.getLookAtPoint();
		lap.sub(c.getCenterOfProjection()); 
		int diffX = e.getX() - prevEvent.getX();
		rot.set(new AxisAngle4f(c.getCameraYAxis(), - factor/5 * diffX));
		rot.transform(lap);
		int diffY = e.getY() - prevEvent.getY();
		rot.set(new AxisAngle4f(c.getCameraXAxis(), - factor/5 * diffY));
		rot.transform(lap);
		lap.add(c.getCenterOfProjection());
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
				if (light != null)
					light.getPosition().add(new Vector3f(0,1,0));
				break;
			case KeyEvent.VK_W:
				step(-1);
				break;
			case KeyEvent.VK_DOWN:
				if (light != null)
					light.getPosition().add(new Vector3f(0,-1,0));
				break;
			case KeyEvent.VK_S:
				step(1);
				break;
			case KeyEvent.VK_LEFT:
				if (light != null)
					light.getPosition().sub(rightStep);
				break;
			case KeyEvent.VK_A:
				c.getCenterOfProjection().sub(rightStep);
				c.getLookAtPoint().sub(rightStep);
				break;
			case KeyEvent.VK_RIGHT:
				if (light != null)
					light.getPosition().add(rightStep);
				break;
			case KeyEvent.VK_D:
				c.getCenterOfProjection().add(rightStep);
				c.getLookAtPoint().add(rightStep);
				break;
			case KeyEvent.VK_SPACE:
				dive(-1);
				break;
			case KeyEvent.VK_C:
				dive(1);
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
		step(e.getWheelRotation()*e.getScrollAmount());
		c.update();
	}

	/**
	 * positive value means backward, negative forward
	 * @param amount
	 */
	private void step(int amount) {
		Vector3f backStep = new Vector3f(c.getCameraZAxis());
		backStep.scale(amount);
		c.getCenterOfProjection().add(backStep);
		c.getLookAtPoint().add(backStep);
	}
	/**
	 * positive value means downwards, negative upwards
	 * @param i
	 */
	private void dive(int amount) {
		Vector3f upStep = new Vector3f(c.getCameraYAxis());
		upStep.scale(-amount);
		c.getCenterOfProjection().add(upStep);
		c.getLookAtPoint().add(upStep);
	}

}
