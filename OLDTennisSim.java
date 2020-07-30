package ProjectileNMK;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.frames.DisplayFrame;

public class OLDTennisSim extends AbstractSimulation {
	// what is the question that you asked
	// i learned:

	// surface area of a tennis ball 286.5 sq inches = 0.18483834 sq meters
	double area = 0.012664491;
	// initial velocity
	double v0 = 20;
	// density of air in kg/m3
	double density = 1.225;

	// LIFT
	// Cl = L / (A * .5 * r * V^2);
	// L = (4 * pi^2 * b^3 * s * rho * V) * 4 / 3;
	double spin = 0;
	// lift coefficient
	double Vspin = 100;
	double Cl = 0;
	double lift = 0;
	double dragX = 0;

	// drag coefficient for a tennis ball (was believed to be 0.54 to 0.5, but
	// recent experiments yield half of that)
	double Cd = 0.5;
	// setting variable for the drag without velocities on the tennis ball
	double dragNoV = Cd * area * density * .5;
	// starting angle
	double theta = 10.0;

	// simulation
	DisplayFrame d = new DisplayFrame("Meters", "Meters", "Tennis Simulation");

	// sets the amount of seconds in each time interval
	double timeStep = .01;

	// acceleration of gravity
	double g = 9.81;
	double ax = 0;
	double ay = 0;
	double vy = 0;
	double vx = 0;

	// number of balls
	int maxBalls = 3;

	// starting position
	double x0 = 0;
	double y0 = 1.7;

	// position goal
	double Finalx = 25;
	double Finaly = 0;

	// velocity lost when it bounces
	double vLostFloor = .8;

	// creates array list of balls
	ArrayList<TennisBall> tballs = new ArrayList<TennisBall>();

	// @Override
	public void initialize() {
		// simulation is visible
		d.setVisible(true);
		// can close simulation easily
		d.setDefaultCloseOperation(3);
		// xmin xmax ymin ymax
		d.setPreferredMinMax(-1, 30, -1, 5);

		// initializes each ball
		for (int i = 0; i < maxBalls; i++) {
			ax = -dragNoV * v0 * Math.cos(Math.toRadians(theta)) * v0 * Math.cos(Math.toRadians(theta));
			ay = -g - (dragNoV * v0 * Math.sin(Math.toRadians(theta)) * v0 * Math.sin(Math.toRadians(theta)));
			vx = v0 * Math.cos(Math.toRadians(theta));
			vy = v0 * Math.sin(Math.toRadians(theta));
			// creates a ball:
			// starts at (1.1)
			// x acceleration = 0
			// y acceleration = -9.81
			// x velocity = initial velocity * cosine of given angle
			// y velocity = initial velocity * sine of given angle
			// initial velocity = inputed initial velocity;
			// first ball starts with an angle thats given
			TennisBall t = new TennisBall(x0, y0, ax, ay, vx, vy, v0, theta, spin, 0, lift, dragNoV, dragX, true);
			// makes the balls start at the given coordinates
			t.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of balls to 3 pix
			t.pixRadius = 3;
			// adds the ball to the array list of balls called "balls"
			tballs.add(t);
			// adds the balls to the simulation
			d.addDrawable(t);
			// color
			tballs.get(i).color = Color.green;
		}
	}

	protected void doStep() {
		for (int i = 0; i < maxBalls; i++) {
			tballs.get(0).color = Color.black;
			tballs.get(1).color = Color.blue;
			tballs.get(2).color = Color.red;
			tballs.get(0).setSpin(0);
			tballs.get(1).setSpin(.3);
			tballs.get(2).setSpin(-.3);
			lift = tballs.get(i).getSpin() * area * density * tballs.get(i).getV() * tballs.get(i).getV() * .5;
			Cd = Cd - lift;
			dragNoV = Cd * area * density * .5;

			// makes the simulation go faster
			//this.setDelayTime(100);

			// ACCELERATION
			// setting acceleration y
			// if the y velocity is greater than 0
			if (tballs.get(i).getVy() > 0) {
				// then you make the y acceleration -g - (b*v^2)
				// bballs.get(i).setAy(-g - (b * bballs.get(i).getVy() *
				// bballs.get(i).getVy()));
				tballs.get(i).setAy(-g - (dragNoV * tballs.get(i).getVy() * tballs.get(i).getVy()));
			}
			// if the y velocity is less than or equal to 0
			else if (tballs.get(i).getVy() <= 0) {
				// then you make they acceleration -g + (b*v^2)
				// bballs.get(i).setAy(-g + (b * bballs.get(i).getVy() *
				// bballs.get(i).getVy()));
				tballs.get(i).setAy(-g + (dragNoV * tballs.get(i).getVy() * tballs.get(i).getVy()));
			}

			// acceleration in the horizontal direction has no gravity, only air resistance
			// so x acceleration is just -(b*v^2)
			// bballs.get(i).setAx(-(b * bballs.get(i).getVx() * bballs.get(i).getVx()));
			tballs.get(i).setAx(-dragNoV * tballs.get(i).getVx() * tballs.get(i).getVx());

			// VELOCITY
			// updates velocity x (v(t) = v0 + at)
			// making the new x velocity = old x velocity + x acceleration *time
			tballs.get(i).setVx(tballs.get(i).getVx() + (tballs.get(i).getAx() * timeStep));
			// making the new y velocity = old y velocity + y acceleration *time
			tballs.get(i).setVy(tballs.get(i).getVy() + (tballs.get(i).getAy() * timeStep));

			// POSITION
			// finds position using right hand rule under an imaginary velocity/time graph
			// new y position = old y position + time*y velocity
			// time * y velocity is a rectangle and its area is the change in the ball's
			// position
			tballs.get(i).setPy(tballs.get(i).getPy() + (timeStep * tballs.get(i).getVy()));

			// new x position = old x position + time*x velocity
			// time * x velocity is a rectangle and its area is the change in the ball's
			// position
			tballs.get(i).setPx(tballs.get(i).getPx() + (timeStep * tballs.get(i).getVx()));

			// setting position on simulation
			tballs.get(i).setXY(tballs.get(i).getPx(), tballs.get(i).getPy());

			// If BALL WENT OVER THE WALL:
			// then the x coordinate is >= 100 and y coordinate is >= 11.3)
			if (tballs.get(i).getPx() >= Finalx && tballs.get(i).getPy() >= Finaly) {
				this.stopSimulation(); // stops the simulation
				System.out.println("velocity = " + tballs.get(i).getV() + "m/s"); // prints out velocity at that point
				System.out.println("angle = " + tballs.get(i).getTheta() + "°"); // prints out angle at that point
				System.out.println("(" + tballs.get(i).getPx() + ", " + tballs.get(i).getPy() + ")"); // prints out
																										// coordinates
				System.out.println("Has bounced " + tballs.get(i).getBounce() + " times"); // if it's in
				break; // breaks out of the for loop
				// code stops because because ball went over the wall
			}

			// IF BALL DIDN'T GO OVER THE WALL:
			// when the balls hit the ground
			// System.out.println("Px = " + bballs.get(i).getPx() + " Py = " +
			// bballs.get(i).getPy());
			if (tballs.get(i).getPy() <= 0) {
				tballs.get(i).setBounce(tballs.get(i).getBounce()+1);
				// updates the y velocity to be the y velocity * how much the item loses
				tballs.get(i).setVy(-tballs.get(i).getVy() * vLostFloor);
				// keeps the same angle because each ball has a different angle but the same
				// velocity
				// keeps the same x and y acceleration because it will update that according to
				// System.out.println("Px = " + bballs.get(3).getPx() + " Py = " +
				// bballs.get(3).getPy() + " Vx = " + bballs.get(3).getVx() + " Vy = " +
				// bballs.get(3).getVy() + " Angle = "
				// + bballs.get(3).getTheta());
				vy = tballs.get(i).getVy();
				vx = tballs.get(i).getVx();
				// the new velocity
				tballs.get(i).setTheta(Math.atan(vy / vx));
				tballs.get(i).setPy(0);
				// if (i == maxBalls - 1) {
				// for (int j = 0; j < maxBalls; j++) {
				// bballs.get(j).color = Color.green; // turns the balls white (as if they
				// "disappeared")
				// // updates the y velocity to be the y velocity * how much the item loses
				// bballs.get(j).setVy(-bballs.get(j).getVy() * vLostFloor);
				// // keeps the same angle because each ball has a different angle but the same
				// // velocity
				// // keeps the same x and y acceleration because it will update that according
				// to
				// System.out.println("Vx = " + bballs.get(3).getVx() + " Vy = " +
				// bballs.get(3).getVy()
				// + " Angle = " + bballs.get(3).getTheta());
				// vy = bballs.get(j).getVy();
				// vx = bballs.get(j).getVx();
				// // the new velocity
				// bballs.get(j).setTheta(Math.atan(vy / vx));
				// bballs.get(j).setPy(0);
				// }
				// }
			}
		}
	}

	public void reset() {
		// sets x coordinate of starting position of balls to 1
		control.setValue("x", x0);
		// sets y coordinate of starting position of ball to y
		control.setValue("y", y0);
		// sets the initial velocity of balls to v0 (which can be changed at the top of
		// the code)
		control.setValue("v0", v0);
	}

	public static void main(String[] args) {
		// allows us to see the simulation

		SimulationControl.createApp(new TennisOptimalV());
	}
}