package ProjectileNMK;

import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import polyfun.Polynomial;

public class TwoDMotion extends AbstractSimulation {
	/**
	 * Creates four things: a simulation of a ball dropping, a Velocity/Time Graph,
	 * a Position/Time Graph, and an Acceleration/Time Graph.
	 */

	// simulation
	Circle c = new Circle();
	DisplayFrame d = new DisplayFrame("X", "Y", "Dropping Ball Simulation");
	Trail trail = new Trail();

	// position
	PlotFrame xty = new PlotFrame("t", "position", "Position/Time Graph Y");
	PlotFrame xtx = new PlotFrame("t", "position", "Position/Time Graph X");

	// velocity
	PlotFrame vty = new PlotFrame("t", "velocity", "Velocity/Time Graph Y");
	PlotFrame vtx = new PlotFrame("t", "velocity", "Velocity/Time Graph X");

	// acceleration
	PlotFrame aty = new PlotFrame("t", "acceleration", "Acceleration/Time Graph Y");
	PlotFrame atx = new PlotFrame("t", "acceleration", "Acceleration/Time Graph X");

	// beta
	double b = .02;

	// time
	double timeStep = .1;
	double tf = 0;

	// acceleration
	double g = 9.81;
	double ay = -g;
	double ax = 0;

	// givens (angle and v0)
	double v0 = 89.99999;
	double theta = 36.4;

	// velocity x
	double v0x = v0 * Math.cos(Math.toRadians(theta));
	double vx = v0x;
	double midvx = v0x + ax * .05 * timeStep;

	// velocity y
	double v0y = v0 * Math.sin(Math.toRadians(theta));
	double vy = v0y;
	double midvy = v0y + ay * .05 * timeStep;

	// balls
	ArrayList<Circle> balls = new ArrayList<Circle>();

	// position
	double midAreay = 0;
	double trapAreay = 0;
	// double Areay = (2 * midAreay + trapAreay) / 3;
	double Areay = 0;
	double x0 = 0;
	int x = 1; // for ball in simulation
	double midAreax;
	double trapAreax;
	double Areax = 0;

	@Override
	public void initialize() {
		// simulation
		c.setXY(control.getDouble("x"), control.getDouble("y"));
		d.setVisible(true);
		d.setDefaultCloseOperation(3);
		d.addDrawable(c);
		// position x
		xty.setDefaultCloseOperation(3);
		xty.setVisible(true);
		// position y
		xtx.setDefaultCloseOperation(3);
		xtx.setVisible(true);
		// velocity x
		vty.setDefaultCloseOperation(3);
		vty.setVisible(true);
		// velocity y
		vtx.setDefaultCloseOperation(3);
		vtx.setVisible(true);
		// acceleration x
		aty.setDefaultCloseOperation(3);
		aty.setVisible(true);
		// acceleration y
		atx.setDefaultCloseOperation(3);
		atx.setVisible(true);
		balls.add(c);
	}

	protected void doStep() {
		// velocity y
		vty.append(1, tf, vy);
		vty.setLineColor(1, Color.blue);
		// velocity x
		vtx.append(1, tf, vx);
		vtx.setLineColor(1, Color.blue);

		// acceleration y
		aty.append(2, tf, ay);
		aty.setLineColor(2, Color.green);
		// acceleration x
		atx.append(2, tf, ax);
		atx.setLineColor(2, Color.green);

		// ball moves according to position function
		System.out.println(Areax + ", " + Areay);
		c.setXY(Areax, Areay);
		trail.addPoint(Areax, Areay);
		d.addDrawable(trail);

		// position
		xty.append(3, tf, Areay);
		xty.setLineColor(3, Color.cyan);
		xtx.append(3, tf, Areax);
		xtx.setLineColor(3, Color.cyan);

		// new acceleration
		if (vy < 0) {
			ay = -(g - (b * vy * vy));
		} else if (vy >= 0) {
			ay = -(g + (b * vy * vy));
		}
		ax = -(b * vx * vx);

		// new velocity x
		vy = v0y + ay * timeStep;
		// midvy = v0y + ay * .05 * timeStep; // find velocity at midpoint of slice
		// new velocity y
		vx = v0x + ax * timeStep;
		// midvx = v0x + ax * .05 * timeStep; // find velocity at midpoint of slice

		// position x
		// midAreay = midvy * timeStep;
		// trapAreay = timeStep * (.05 * (v0y + vy));
		// Areay = Areay + (2 * midAreay + trapAreay) / 3;
		Areay = Areay + (timeStep * vy);

		// position y
		// midAreax = midvx * timeStep;
		// trapAreax = timeStep * (.05 * (v0x + vx));
		// Areax = Areax + (2 * midAreax + trapAreax) / 3;
		Areax = Areax + (timeStep * vx);

		// add time
		tf = tf + timeStep;

		// update velocity
		v0y = vy;
		v0x = vx;

	}

	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);

	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new TwoDMotion());

	}
}
