package ProjectileNMK;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class BasicBall extends AbstractSimulation {
	/**
	 * This program is going to be used for Simulations and Animations
	 */

	Circle c = new Circle();
	DisplayFrame frame = new DisplayFrame("X", "Y", "Display Frame Test");

	@Override
	protected void doStep() {
		// TODO Auto-generated method stub
		c.setXY(c.getX(), c.getY() - 0.1);
	}

	public void initialize() {
		c.setXY(control.getDouble("x"), control.getDouble("y"));
		frame.setVisible(true);
		frame.addDrawable(c);
	}

	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new BasicBall());
		
	}

}
