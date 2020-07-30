package ProjectileNMK;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.ImageIcon;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.frames.DisplayFrame;

public class TennisSim extends AbstractSimulation {
	// What this simulation incorporates:
	// lift
	// drag (air resistance)
	// bouncing and the coefficient of restitution (how much y velocity you lose
	// when it bounces)
	// spin (comparing top spin, no spin, and back spin)
	// friction

	/**
	 * Setting things that pertain to: The SIMULATION itself
	 */
	// creates a display frame
	DisplayFrame d = new DisplayFrame("Meters", "Meters", "Tennis Simulation");
	// creates array list of tennis balls
	ArrayList<TennisBall> tballs = new ArrayList<TennisBall>();
	// determines the number of balls that are hit at the same time
	int maxBalls = 30;
	// sets the amount of seconds in each time interval
	double timeStep = .01;
	// creates a variable for setting the colors of balls w different types of spin
	Color colorType;

	/**
	 * Setting things that pertain to: the TENNIS BALL
	 */
	// starting ANGLE
	double theta = 30.0;
	// theta will change but angle is always the initial angle the ball is shot at
	double angle = theta;
	// initializing the returning angle for the balls with no spin
	double returnAngleNo = 0;
	// initializing the returning angle for the balls with no back
	double returnAngleBack = 0;
	// initializing the returning angle for the balls with no top
	double returnAngleTop = 0;
	// initializes a variable that is then used to spin out the return angle (is
	// later set to equal the no spin, back spin, and top spin initial angles
	// depending on the ball)
	double returnAngle = 0;

	// acceleration of gravity
	double g = 9.81;
	// initializing x acceleration and velocity
	double ax = 0;
	// initializing y acceleration and velocity
	double ay = 0;

	// VELOCITY
	// initial velocity
	double v0 = 13;
	// sets the initial velocity for the balls with top spin, back spin, and no spin
	// equal to the initial velocity with which you hit the tennis ball
	double v0top = v0;
	double v0back = v0;
	double v0no = v0;
	// initial y velocity
	double vy = 0;
	// initial x velocity
	double vx = 0;
	// this variable changes every step
	double velocity = 0;

	// POSITION
	// starting x position in meters
	double x0 = 0;
	// starting y position in meters
	double y0 = 2;
	// x position of the net
	double pNet = 11.8872;
	// y position of the top of the net
	double hNet = 1.07;

	// force of the LIFT of a sphere affected by spin
	// creating variable spin for the spin on the ball
	double spin = 0;
	// Spin velocity in revolutions/second
	double Vspin = 1000;
	// initial lift
	double lift = 0;
	// r is the radius of a tennis ball in meters
	double r = 0.067;
	// no v and no spin
	double lifty = 0;
	// average velocity of the air
	double Vair = 500;

	// These are equations for how the lift affects the drag:
	// lift = lift coefficient * area * density * v^2 * 1/2
	// drag = initial drag - lift
	// surface area of a tennis ball in square meters
	double area = 0.012664491;
	// lift coefficient
	double Cl = 0;
	// lift coefficient with back spin
	double Clback = (1 / (2 + (v0 / Vspin)));
	// lift coefficient with top spin
	double Cltop = -(1 / (2 + (v0 / Vspin)));
	// density of air in kg/m3
	double density = 1.225;
	// for printing out the data the end
	String Spin = "";

	// DRAG
	// drag coefficient for a tennis ball
	double Cd0 = 0.5;
	// drag coefficient without lift
	double Cd = Cd0 - lift;
	// constant part of the drag (air resistance for a spherical object)
	// it does not factor in the velocity because it changes
	double dragNoV = Cd * area * density * .5;
	// horizontal drag (no lift)
	double dragX = Cd0 * area * density * .5;

	// VELOCITIES LOST when it bounces
	// COR ("coefficient of restitution") -- 0.6 for grass, 0.83 for hard courts,
	// and 0.85 for clay courts.
	// How much of your y-velocity you keep when the ball bounces
	double COR = .83;
	// COF ("coefficient of friction") -- grass is about 0.6, hard courts about 0.7,
	// and clay about 0.8.
	// How much of your x-velocity you keep when the ball bounces
	double COF = .7;

	// @Override
	public void initialize() {
		// Rectangle(double center x-position, double center y-position, double width,
		// double height)
		// creates the net
		DrawableShape net = DrawableShape.createRectangle(11.9872, 0.535, 0.2, hNet);
		// creates the court floor
		DrawableShape floor = DrawableShape.createRectangle(11.9872, 0.05, 24, 0.1);
		// adds net to simulation
		d.addDrawable(net);
		// adds the court floor to simulation
		d.addDrawable(floor);
		// color of net
		net.setMarkerColor(Color.GREEN, Color.LIGHT_GRAY);
		// color of floor
		floor.setMarkerColor(Color.GRAY, Color.RED);
		// simulation is visible
		d.setVisible(true);
		// can close simulation easily
		d.setDefaultCloseOperation(3);
		// sets xmin xmax ymin ymax for the display frame
		d.setPreferredMinMax(-1, 24.7744, -1, 5);

		// initializes each ball
		for (int i = 0; i < maxBalls; i++) {
			// first third of the balls have no spin
			if (i < 1.0 / 3.0 * maxBalls) {
				// no spin
				spin = Cl;
				// sets the x velocity
				vx = v0no * Math.cos(Math.toRadians(theta));
				// add 1 so that each ball with no spin has a velocity 1 m/s greater than the
				// one that came before it with no spin
				v0no++;
				// makes the balls with no spin magenta
				colorType = Color.magenta;
			}
			// second third of the balls have back spin
			else if (i >= 1.0 / 3.0 * maxBalls && i < 2.0 / 3.0 * maxBalls) {
				// back spin
				spin = Clback;
				// sets the x velocity
				vx = v0back * Math.cos(Math.toRadians(theta));
				// add 1 so that each ball with back spin has a velocity 1 m/s greater than the
				// one that came before it with back spin
				v0back++;
				// makes the balls with back spin gray
				colorType = Color.gray;
			}
			// last third of the balls have top spin
			else if (i >= 2.0 / 3.0 * maxBalls) {
				// top spin
				spin = Cltop;
				// sets the x velocity
				vx = v0top * Math.cos(Math.toRadians(theta));
				// add 1 so that each ball with top spin has a velocity 1 m/s greater than the
				// one that came before it with top spin
				v0top++;
				// makes the balls with top spin blue
				colorType = Color.BLUE;
			}
			// setting y velocity
			vy = v0 * Math.sin(Math.toRadians(theta));
			// System.out.println("vy " + vy);
			// setting initial accelerations
			ax = -dragX * Math.pow(vx, 2.0);
			ay = -g - (dragNoV * Math.pow(vy, 2.0));
			// creates a ball(x position, y position, x acceleration, y acceleration, x
			// velocity, y velocity, initial velocity, angle, spin, number of bounces)
			TennisBall t = new TennisBall(x0, y0, ax, ay, vx, vy, v0, theta, spin, 0, lift, dragNoV, dragX, true);
			// makes the balls start at the given coordinates
			t.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of balls to 3 pix
			t.pixRadius = 3;
			// adds the ball to the array list of balls called "balls"
			tballs.add(t);
			// adds the balls to the simulation
			d.addDrawable(t);
			// sets color for each ball
			tballs.get(i).color = colorType;
		}
	}

	protected void doStep() {
		// makes the simulation go faster
		this.setDelayTime(30);

		for (int i = 0; i < maxBalls; i++) {
			// Lift force of a sphere affected by spin (changes the y position, hence the
			// variable name)
			lifty = (4 / 3) * (4 * Math.PI * Math.PI * r * r * r * density * tballs.get(i).getSpin() * Vair);
			// each ball has a specific lift (positionally) that affects the drag
			tballs.get(i).setLift(tballs.get(i).getSpin() * area * density
					* (Math.pow(tballs.get(i).getVx(), 2.0) + Math.pow(tballs.get(i).getVy(), 2.0)) * .5);
			// updating the drag coefficient
			Cd = Cd0 - tballs.get(i).getLift();
			// Cd = Cd0 - lifty;
			// sets the drag value without the velocity (because it changes)
			tballs.get(i).setDragNov(Cd * area * density * .5);

			// ACCELERATION
			// setting acceleration y
			// if the y velocity is greater than 0
			if (tballs.get(i).getVy() > 0.0) {
				// then you make the y acceleration -g - (b*v^2)
				tballs.get(i).setAy(
						-g - (tballs.get(i).getDragNov() * tballs.get(i).getVy() * tballs.get(i).getVy()) + lifty);
			}
			// if the y velocity is less than or equal to 0
			else if (tballs.get(i).getVy() <= 0.0) {
				// then you make they acceleration -g + (b*v^2)
				tballs.get(i).setAy(
						-g + (tballs.get(i).getDragNov() * tballs.get(i).getVy() * tballs.get(i).getVy()) + lifty);
			}

			// acceleration in the horizontal direction has no gravity, only air resistance
			// so x acceleration is just -(b*v^2)
			if (tballs.get(i).getVx() > 0.0) {
				tballs.get(i).setAx(-tballs.get(i).getDragX() * tballs.get(i).getVx() * tballs.get(i).getVx());
			} else if (tballs.get(i).getVx() <= 0.0) {
				tballs.get(i).setAx(tballs.get(i).getDragX() * tballs.get(i).getVx() * tballs.get(i).getVx());
			}

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

			// sets velocity
			velocity = Math.sqrt(Math.pow(tballs.get(i).getVx(), 2) + Math.pow(tballs.get(i).getVy(), 2));

			// WHEN IT'S OUT IT TURNS YELLOW

			// Different cases:
			// going right and is short
			if (((tballs.get(i).getVx() > 0 && tballs.get(i).getPx() <= pNet && tballs.get(i).getPy() <= hNet)
					// goes left and is short
					|| (tballs.get(i).getVx() < 0 && tballs.get(i).getPx() >= pNet && tballs.get(i).getPy() <= hNet)
					// going right and has not bounced, so it's too long
					|| (tballs.get(i).getVx() > 0 && tballs.get(i).getPx() >= 2 * pNet && tballs.get(i).getPy() > 0
							&& tballs.get(i).getBounce() == 0)
					// going left and has not bounced, so it's too long
					|| (tballs.get(i).getVx() < 0 && tballs.get(i).getPx() <= 0 && tballs.get(i).getPy() > 0
							&& tballs.get(i).getBounce() == 0))) {
				// make the ball yellow
				tballs.get(i).color = Color.yellow;
				// change boolean to signify that it went out
				tballs.get(i).setIsIn(false);
			}

			// When the ball is hit back and goes in
			if (tballs.get(i).getPx() >= 0 && tballs.get(i).getPx() <= pNet && tballs.get(i).getPy() <= 0
					&& tballs.get(i).isIsIn() == true) {
				// stops the simulation
				this.stopSimulation();
				// makes the ball bigger
				tballs.get(i).pixRadius = 4;
				// determines whether it is a ball in the first third, second third, or last
				// third
				// this will reveal with what spin it was hit
				if (i < 1.0 / 3.0 * maxBalls) {
					Spin = "no";
					returnAngle = returnAngleNo;
				} else if (i >= 1.0 / 3.0 * maxBalls && i < 2.0 / 3.0 * maxBalls) {
					Spin = "back";
					returnAngle = returnAngleBack;
				} else if (i >= 2.0 / 3.0 * maxBalls) {
					Spin = "top";
					returnAngle = returnAngleTop;
				}
				// Prints out information about the shot
				System.out.println("Initially shot at a " + angle + "° angle relative to the court");
				System.out.println("Was returned at a " + (-1 * returnAngle) + "° angle relative to the court");
				System.out.println("Had initial velocity of " + v0 + "m/s");
				System.out.println("Current velocity is -" + velocity + "m/s (when it hits the court)");
				System.out.println("The ball has " + Spin + " spin");
				System.out.println("The ball is now " + tballs.get(i).getPx() + " meters from the baseline");
				System.out.println("Has bounced " + tballs.get(i).getBounce() + " times on the return");
				break; // breaks out of the for loop
				// code stops because because the return ball is in
			}

			// If the balls hit the ground
			if (tballs.get(i).getPy() <= 0) {
				// counts how many times it's hit the ground
				tballs.get(i).setBounce(tballs.get(i).getBounce() + 1);

				// updates the y velocity to be the y velocity * how much the item loses by
				// hitting the ground
				tballs.get(i).setVy(-tballs.get(i).getVy() * COR);

				// ground friction
				// no spin: ground friction and gains top spin
				if (i < 1.0 / 3.0 * maxBalls) {
					tballs.get(i).setVx(tballs.get(i).getVx() * COF);
					tballs.get(i).setSpin(tballs.get(i).getSpin() - .2);
				}

				// back spin: ground friction and switches from back to top spin
				else if (i >= 1.0 / 3.0 * maxBalls && i < 2.0 / 3.0 * maxBalls) {
					tballs.get(i).setVx(tballs.get(i).getVx() * COF);
					if (tballs.get(i).getBounce() == 1) {
						tballs.get(i).setSpin(tballs.get(i).getSpin() - .1);
					} else {
						tballs.get(i).setSpin(tballs.get(i).getSpin() - .2);
					}
				}
				// top spin: ground friction and gains more top spin
				else if (i >= 2.0 / 3.0 * maxBalls) {
					tballs.get(i).setVx(tballs.get(i).getVx() * COF);
					tballs.get(i).setSpin(tballs.get(i).getSpin() - .2);
				}

				// the new angle as the inverse tan of (vy/vx)
				tballs.get(i).setTheta(Math.toDegrees(Math.atan(tballs.get(i).getVy() / tballs.get(i).getVx())));

				// if it's negative it's now 0
				tballs.get(i).setPy(0);
			}

			// the first ball was in and it needs to be RETURNED
			if (tballs.get(i).getPx() >= 23.78) {
				// sets the bounce = 0 (this is counting the number of bounces one way)
				tballs.get(i).setBounce(0);

				// the new angle as the inverse tan of (vy/vx)
				tballs.get(i).setTheta(Math.toDegrees(Math.atan(tballs.get(i).getVy() / tballs.get(i).getVx())));

				// Storing the new angle equal as the ball's return angle
				// Balls with no spin
				if (i == 0) {
					returnAngleNo = tballs.get(0).getTheta();
				}
				// Balls with back spin
				if (i == 1) {
					returnAngleBack = tballs.get(1).getTheta();
				}
				// Balls with top spin
				if (i == 2) {
					returnAngleTop = tballs.get(2).getTheta();
				}

				// The shot is being hit back with so much force that the ball instantly travels
				// 15m/s more in the x and the y directions
				tballs.get(i).setVx(-(velocity + 15) * Math.cos(Math.toRadians(tballs.get(i).getTheta())));
				tballs.get(i).setVy(-(velocity + 15) * Math.sin(Math.toRadians(tballs.get(i).getTheta())));
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

		SimulationControl.createApp(new TennisSim());
	}

}