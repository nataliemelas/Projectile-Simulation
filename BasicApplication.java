package ProjectileNMK;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;

public class BasicApplication extends AbstractSimulation {
	int x;

	@Override
	public void initialize() {
		int z = control.getInt("x");
	}

	protected void doStep() {
		control.println("X = " + x);
		x++;
	}

	public void reset() {
		control.setValue("x", 50);
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new BasicApplication());
	}

}
