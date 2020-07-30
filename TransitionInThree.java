package ProjectileNMK;

import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import polyfun.Polynomial;

public class TransitionInThree extends AbstractSimulation {
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
	double g = 9.81;
	// double ay = -g;
	// double ax = 0;

	// givens (angle and v0)
	// double v0 = 113.4;
	double v0 = 65;
	double theta = 20;
	double MinTheta = 20;
	double MaxTheta = 85;

	// velocity x
	// double v0x = v0 * Math.cos(Math.toRadians(theta));
	// double vx = v0x;
	// double midvx = v0x + ax * .05 * timeStep;

	// velocity y
	// double v0y = v0 * Math.sin(Math.toRadians(theta));
	// double vy = v0y;
	// double midvy = v0y + ay * .05 * timeStep;

	// balls
	ArrayList<Ball> balls = new ArrayList<Ball>();

	// position
	// int x = 1; // for ball in simulation
	// double midAreay = 0;
	// double trapAreay = 0;
	// double Areay = (2 * midAreay + trapAreay) / 3;
	// double x0 = 0;
	// double midAreax;
	// double trapAreax;
	// double Areax;

	// keeping track
	// double trials = 0;

	@Override
	public void initialize() {
		// simulation
		d.setVisible(true);
		d.setDefaultCloseOperation(3);
		d.addDrawable(c);
		DrawableShape wall = DrawableShape.createRectangle(100.5, 5.5, 1, 11);
		d.addDrawable(wall);
		d.setPreferredMinMaxX(-1, 105);
		d.setPreferredMinMaxY(0, 55);
		for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
			Ball b = new Ball(0, 0, 0, -g, v0 * Math.cos(Math.toRadians(theta)), v0 * Math.sin(Math.toRadians(theta)), v0,
					theta);
			b.setXY(control.getDouble("x"), control.getDouble("y"));
			b.pixRadius = 3;
			balls.add(b);
			d.addDrawable(b);
			theta = theta + .1;
		}
	}

	protected void doStep() {
		// ball moves according to position function
		for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
			// System.out.println(i + " THETA: " + balls.get(i).getTheta());
			// System.out.println(i + " Vy: " + balls.get(i).getVy());

			// setting acceleration y
			if (balls.get(i).getVy() > 0) {
				// System.out.println("before: " + balls.get(i).getAy());
				balls.get(i).setAy(-g - (b * balls.get(i).getVy() * balls.get(i).getVy()));
				// System.out.println("after: " + balls.get(i).getAy());
				System.out.println("");
			} else if (balls.get(i).getVy() < 0) {
				balls.get(i).setAy(-g + (b * balls.get(i).getVy() * balls.get(i).getVy()));
			} else {
				balls.get(i).setAy(-g);
			}

			// acceleration x
			balls.get(i).setAx(-(b * balls.get(i).getVx() * balls.get(i).getVx()));

			// velocity x
			// making the x velocity = the x velocity
			balls.get(i).setVx(v0 + balls.get(i).getAx() * .05 * timeStep);
			System.out.println(balls.get(i).getVx() + balls.get(i).getAx() * .05 * timeStep);
			// velocity y
			balls.get(i).setVy(v0 + balls.get(i).getAy() * .05 * timeStep);

			// TRAPEZOID area balls.get(i).getPx() + timeStep * (balls.get(i).getVx() -
			// timeStep + balls.get(i).getVx()) / 2)
			// x position
			// past x position + timestep * (velocity + (timestep+velocity))/2
			balls.get(i).setPx(balls.get(i).getPx() + timeStep * balls.get(i).getVx());

			balls.get(i).setPy(balls.get(i).getPy() + timeStep * balls.get(i).getVy());

			balls.get(i).setXY(balls.get(i).getPx(), balls.get(i).getPy());

			if (balls.get(i).getPx() >= 100 && balls.get(i).getPy() >= 11) {
				System.out.println("velocity = " + v0);
				System.out.println("angle = " + theta + "°");
				System.out.println(balls.get(i).getPx() + ", " + balls.get(i).getPy());
				System.out.println("");
				this.stopSimulation();
			} else if (balls.get(i).getPy() < 0) {
				v0 = v0 + 1;
				balls.get(i).setPx(0);
				balls.get(i).setPy(0);
			}
		}

		// trail.addPoint(Areax, Areay);
		// d.addDrawable(trail);

		// position y
		// USING SIMPSONS:
		// midAreay = midvy * timeStep;
		// trapAreay = timeStep * (.05 * (v0y + vy));
		// Areay = Areay + (2 * midAreay + trapAreay) / 3;

		// USING RECTANGLES:
		// Areay = Areay + vy * timeStep;

		// TRAPEZOID
		// Areay = Areay + timeStep * (vy - timeStep + vy) / 2;

		// position x
		// USING SIMPSONS:
		// midAreax = midvx * timeStep;
		// trapAreax = timeStep * (.05 * (v0x + vx));
		// Areax = Areax + (2 * midAreax + trapAreax) / 3;

		// USING RECTANGLES:
		// Areax = Areax + vx * timeStep;

		// TRAPEZOID
		// Areax = Areax + timeStep * (vx - timeStep + vx) / 2;

		/**
		 * new velocity y vy = v0y + ay * timeStep; midvy = v0y + ay * .05 * timeStep;
		 * // find velocity at midpoint of slice // new velocity x vx = v0x + ax *
		 * timeStep; midvx = v0x + ax * .05 * timeStep; // find velocity at midpoint of
		 * slice
		 */

		// add time
		tf = tf + timeStep;

		// update velocity
		// v0y = vy;
		// v0x = vx;

		// new acceleration ay = -g - (b * vy * vy);, ay = -g + (b * vy * vy);, ay = -g;
		// if (vy > 0) {
		// for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
		// balls.get(i).setAy(-g - (b * balls.get(i).getVy() * balls.get(i).getVy()));
		// }
		// } else if (vy < 0) {
		// for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
		// balls.get(i).setAy(-g + (b * balls.get(i).getVy() * balls.get(i).getVy()));
		// }
		// } else {
		// for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
		// balls.get(i).setAy(-g);
		// }
		// }
		// System.out.println("ay:" + ay);
		// System.out.println("stuff: " + (b * vy * vy));
		/*
		 * ax = -(b * vx * vx);
		 */
		// for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
		// balls.get(i).setAx(-(b * balls.get(i).getVx() * balls.get(i).getVx()));
		// }

		// if (Areax == 100 && !(Areay == 11) || Areax < 100 && trials > 8) {
		// Areay = -1;
		// v0 = v0 + 0.2;
		// theta = ogTheta;
		// trials = 0;
		// }

		// another try
		// if (Areay < 0) {
		// // acceleration
		// ay = -g;
		// ax = 0;
		//
		// // givens (angle and v0)
		// theta = theta + 1;
		//
		// // velocity x
		// v0x = v0 * Math.cos(Math.toRadians(theta));
		// vx = v0x;
		// midvx = v0x + ax * .05 * timeStep;
		//
		// // velocity y
		// v0y = v0 * Math.sin(Math.toRadians(theta));
		// vy = v0y;
		// midvy = v0y + ay * .05 * timeStep;
		//
		// // position
		// midAreay = 0;
		// trapAreay = 0;
		// Areay = (2 * midAreay + trapAreay) / 3;
		// x0 = 0;
		// midAreax = 0;
		// trapAreax = 0;
		// Areax = 0;
		// Areay = 0;
		//
		// // keep track
		// trials = trials + 1;
		// }
		// if (Areax > 99.99999 && Areax < 100.5 && Areay > 10.99999 && Areay < 11.5) {

		// for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
		// if (balls.get(i).getPx() >= 100 && balls.get(i).getPy() >= 11) {
		// System.out.println("velocity = " + v0);
		// System.out.println("angle = " + theta + "°");
		// System.out.println(Areax + ", " + Areay);
		// System.out.println("");
		// }
		// }
	}

	public void reset() {
		control.setValue("x", 0);
		control.setValue("y", 0);
		control.setValue("v0", v0);
	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new TransitionInThree());

	}
}
