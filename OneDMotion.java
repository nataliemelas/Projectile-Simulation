package ProjectileNMK;

import java.awt.Color;
import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import polyfun.Polynomial;

public class OneDMotion extends AbstractSimulation {
	/**
	 * Creates four things: a simulation of a ball dropping, a Velocity/Time Graph,
	 * a Position/Time Graph, and an Acceleration/Time Graph.
	 */

	// simulation
	//create a circle named c
	Circle c = new Circle();
	
	//create a display frame named d
	DisplayFrame d = new DisplayFrame("X", "Y", "Dropping Ball Simulation");

	// create position graph
	PlotFrame xt = new PlotFrame("t", "position", "Position/Time Graph");

	// create velocity graph
	PlotFrame vt = new PlotFrame("t", "velocity", "Velocity/Time Graph");

	// create acceleration graph
	PlotFrame at = new PlotFrame("t", "acceleration", "Acceleration/Time Graph");

	// beta value
	double b = .02;

	// time
	double timeStep = .1;
	double tf = 0;

	// velocity
	double v0 = 0;
	double v = v0;
	double midv = 0;

	// acceleration
	int g = 10;
	double a = -g;

	// balls
	ArrayList<Circle> balls = new ArrayList<Circle>();

	// position
	double midArea = 0;
	double trapArea = 0;
	double Area = (2 * midArea + trapArea) / 3;
	double x0 = 0;

	@Override
	public void initialize() {
		// simulation
		c.setXY(control.getDouble("x"), control.getDouble("y"));
		d.setVisible(false);
		d.addDrawable(c);
		// position
		xt.setDefaultCloseOperation(3);
		xt.setVisible(true);
		// velocity
		vt.setDefaultCloseOperation(3);
		vt.setVisible(true);
		// acceleration
		at.setDefaultCloseOperation(3);
		at.setVisible(true);
		balls.add(c);
	}

	protected void doStep() {
		// velocity graph as a function of time
		vt.append(1, tf, -(v0 + a * timeStep));
		vt.setLineColor(1, Color.blue);

		// acceleration graph as a function of time
		at.append(2, tf, -(g - (b * v * v)));
		at.setLineColor(2, Color.green);

		// new velocity
		v = v0 + a * timeStep;
		// middle velocity
		midv = v0 + a * .05 * timeStep; // find velocity at midpoint of slice

		// position using adapted simpson's rule
		midArea = midv * timeStep;
		trapArea = timeStep * (.05 * (v0 + v));
		Area = Area + (2 * midArea + trapArea) / 3;

		// ball moves according to position function
		c.setXY(c.getX(), -Area);

		// position graph as a function of time
		xt.append(3, tf, -Area);
		xt.setLineColor(3, Color.cyan);

		// add time
		tf = tf + timeStep;

		// update velocity
		v0 = v;

		// new acceleration taking into account air resistance
		a = g - (b * v * v);
	}

	public void reset() {
		// starts at the origin
		control.setValue("x", 0);
		control.setValue("y", 0);

	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new OneDMotion());

	}
}
