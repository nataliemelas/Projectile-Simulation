package ProjectileNMK;

import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import polyfun.Polynomial;

public class BROKENgreenmonster extends AbstractSimulation {
	/**
	 * 
	 */

	// simulation
	Circle c = new Circle();
	DisplayFrame d = new DisplayFrame("X", "Y", "Baseball Simulation");
	Trail trail = new Trail();

	// beta
	double b = .02;

	// time
	double timeStep = .1;
	double tf = 0;

	// acceleration
	int g = 10;
	double ay = -g;
	double ax = 0;

	// givens (angle and v0)
	double v0 = 108;
	double ogTheta = 22;
	double theta = ogTheta;

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
	double Areay = (2 * midAreay + trapAreay) / 3;
	double x0 = 0;
	int x = 1; // for ball in simulation
	double midAreax;
	double trapAreax;
	double Areax;

	// keeping track
	double trials = 0;

	@Override
	public void initialize() {
		// simulation
		c.setXY(control.getDouble("x"), control.getDouble("y"));
		c.pixRadius = 2;
		d.setVisible(true);
		d.setDefaultCloseOperation(3);
		d.addDrawable(c);
		DrawableShape wall = DrawableShape.createRectangle(100.5, 5.5, 1, 11);
		d.addDrawable(wall);
		d.setPreferredMinMaxX(-1, 105);
		d.setPreferredMinMaxY(-1, 75);
		balls.add(c);
	}

	protected void doStep() {
		// ball moves according to position function
		// System.out.println("position: " + Areay);
		// System.out.println("velocity: " + vy);
		// System.out.println("acceleration: " + ay);
		//System.out.println("");
		c.setXY(Areax, Areay);
		trail.addPoint(Areax, Areay);
		d.addDrawable(trail);

		// position x
		midAreay = midvy * timeStep;
		trapAreay = timeStep * (.05 * (v0y + vy));
		Areay = Areay + (2 * midAreay + trapAreay) / 3;
		// position y
		midAreax = midvx * timeStep;
		trapAreax = timeStep * (.05 * (v0x + vx));
		Areax = Areax + (2 * midAreax + trapAreax) / 3;

		// new velocity x
		vy = v0y + ay * timeStep;
		midvy = v0y + ay * .05 * timeStep; // find velocity at midpoint of slice
		// new velocity y
		vx = v0x + ax * timeStep;
		midvx = v0x + ax * .05 * timeStep; // find velocity at midpoint of slice

		// add time
		tf = tf + timeStep;

		// update velocity
		v0y = vy;
		v0x = vx;

		// new acceleration
		if (vy > 0) {
			ay = -g - (b * vy * vy);
		} else if (vy < 0) {
			ay = -g + (b * vy * vy);
		} else {
			ay = -g;
		}
		// System.out.println("ay:" + ay);
		// System.out.println("stuff: " + (b * vy * vy));
		ax = -(b * vx * vx);

		if (Areax == 100 && !(Areay == 11) || Areax < 100 && trials > 6) {
			Areay = -1;
			v0 = v0 + 0.5;
			theta = ogTheta;
			trials = 0;
		}

		// another try
		if (Areay < 0) {
			// acceleration
			ay = -g;
			ax = 0;

			// givens (angle and v0)
			theta = theta + 1;

			// velocity x
			v0x = v0 * Math.cos(Math.toRadians(theta));
			vx = v0x;
			midvx = v0x + ax * .05 * timeStep;

			// velocity y
			v0y = v0 * Math.sin(Math.toRadians(theta));
			vy = v0y;
			midvy = v0y + ay * .05 * timeStep;

			// position
			midAreay = 0;
			trapAreay = 0;
			Areay = (2 * midAreay + trapAreay) / 3;
			x0 = 0;
			midAreax = 0;
			trapAreax = 0;
			Areax = 0;
			Areay = 0;

			// keep track
			trials = trials + 1;
		}
		if (Areax > 99 && Areax < 101 && Areay > 10 && Areay < 12) {
			System.out.println("velocity = " + v0);
			System.out.println("angle = " + theta + "°");
			System.out.println(Areax + ", " + Areay);
			System.out.println("");
		}
	}

	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);

	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new BROKENgreenmonster());

	}
}
