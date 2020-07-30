package ProjectileNMK;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.DisplayFrame;

public class TennisServe extends AbstractSimulation {
	// what is the question that you asked/WHAT DID YOU LEARN
	//

	/**
	 * SIMULATION
	 */
	// creates a display frame
	DisplayFrame d = new DisplayFrame("Meters", "Meters", "Tennis Simulation");
	// creates array list of balls
	ArrayList<TennisBall> tballs = new ArrayList<TennisBall>();
	// number of balls
	int maxBalls = 30;
	// sets the amount of seconds in each time interval
	double timeStep = .01;
	// for setting colors of balls w diff types of spin
	Color colorType;

	/**
	 * TENNIS BALL
	 */
	// Uniform characteristics of the tennis ball/shot:
	// surface area of a tennis ball: 19.63 square inches = 0.012664491 square
	// meters
	double area = 0.012664491;
	// initial velocity
	double v0 = 25;
	double v0top = v0;
	double v0back = v0;
	double v0no = v0;
	ArrayList<Double> vels = new ArrayList<Double>();
	// density of air in kg/m3
	double density = 1.225;
	// average velocity of the air
	double Vair = 500;

	// SPIN/LIFT
	double spin = 0;
	// Spin velocity in revolutions/second
	double Vspin = 3000;
	// lift coefficient
	double Cl = 0;
	// lift coefficient with backspin
	double Clback = (1 / (2 + (v0 / Vspin)));
	// lift coefficient with topspin
	double Cltop = -(1 / (2 + (v0 / Vspin)));
	double lift = 0;
	// overall lift
	// l=(4/3)(4pi^2b^3spv)
	// r is the radius of a tennis ball in m
	double r = 0.067;
	// no v and no spin
	double lifty = 0;
	// ending message
	String Spin = "";

	// DRAG
	// drag coefficient for a tennis ball
	double Cd0 = 0.5;
	// drag without lift
	double Cd = Cd0 - lift;
	// constant part of the drag (air resistance for a spherical object)
	// it does not factor in the velocity because it changes
	double dragNoV = Cd * area * density * .5;
	// horizontal drag (no lift)
	double dragX = Cd0 * area * density * .5;

	// VELOCITIES LOST when it bounces
	// COR ("coefficient of restitution") -- 0.6 for grass, 0.83 for hard courts,
	// and 0.85 for clay courts.
	double COR = .83;
	// COF ("coefficient of friction") -- grass is about 0.6, hard courts about 0.7,
	// and clay about 0.8.
	double COF = .7;

	// starting ANGLE
	double theta = 5.0;

	// acceleration of gravity
	double g = 9.81;
	// initializing variables for x acceleration
	double ax = 0;
	// initializing variables for y acceleration
	double ay = 0;
	// initializing variables for x velocity
	double vy = 0;
	// initializing variables for y velocity
	double vx = 0;
	// initializing velocity
	double velocity = 0;

	// POSITION
	// starting position in meters
	double x0 = 0;
	double y0 = 2;
	double pNet = 11.8872;
	double hNet = 1.07;

	// @Override
	public void initialize() {
		// the net
		DrawableShape net = DrawableShape.createRectangle(11.9872, 0.535, 0.2, hNet);
		// floor
		DrawableShape floor = DrawableShape.createRectangle(11.9872, 0.05, 24, 0.1);
		// right service line
		DrawableShape serviceR = DrawableShape.createRectangle(11.9872 + 3.2, 0.05, 6.4, 0.1);
		// left service line
		DrawableShape serviceL = DrawableShape.createRectangle(11.9872 - 3.2, 0.05, 6.4, 0.1);
		// adds these to simulation
		d.addDrawable(net);
		d.addDrawable(floor);
		d.addDrawable(serviceR);
		d.addDrawable(serviceL);
		// colors the four markers
		net.setMarkerColor(Color.GREEN, Color.LIGHT_GRAY);
		floor.setMarkerColor(Color.GRAY, Color.RED);
		serviceR.setMarkerColor(Color.gray, Color.white);
		serviceL.setMarkerColor(Color.gray, Color.white);
		// simulation is visible
		d.setVisible(true);
		// can close simulation easily
		d.setDefaultCloseOperation(3);
		// xmin xmax ymin ymax
		d.setPreferredMinMax(-1, 24.7744, -1, 5);

		// initializes each ball
		for (int i = 0; i < maxBalls; i++) {
			if (i < 1.0 / 3.0 * maxBalls) {
				// no spin
				spin = Cl;
				// sets the x velocity
				vx = v0no * Math.cos(Math.toRadians(theta));
				// add 1 so that each ball with no spin has a velocity 1 m/s greater than the
				// one that came before it with no spin
				v0no = v0no + .1;
				v0 = v0no;
				// makes the balls with no spin magenta
				colorType = Color.magenta;
			} else if (i >= 1.0 / 3.0 * maxBalls && i < 2.0 / 3.0 * maxBalls) {
				// back spin
				spin = Clback;
				// sets the x velocity
				vx = v0back * Math.cos(Math.toRadians(theta));
				// add 1 so that each ball with back spin has a velocity 1 m/s greater than the
				// one that came before it with back spin
				v0back = v0back + .1;
				v0 = v0back;
				// makes the balls with back spin gray
				colorType = Color.gray;
			} else if (i >= 2.0 / 3.0 * maxBalls) {
				// top spin
				spin = Cltop;
				// sets the x velocity
				vx = v0top * Math.cos(Math.toRadians(theta));
				// add 1 so that each ball with top spin has a velocity 1 m/s greater than the
				// one that came before it with top spin
				v0top = v0top + .1;
				v0 = v0top;
				// makes the balls with top spin blue
				colorType = Color.BLUE;
			}
			// setting y velocity
			vy = v0 * Math.sin(Math.toRadians(theta));
			// setting initial accelerations
			ax = -dragX * Math.pow(vx, 2.0);
			ay = -g - (dragNoV * Math.pow(vy, 2.0));
			// creates a ball(x position, y position, x acceleration, y acceleration, x
			// velocity, y velocity, initial velocity, angle, spin, number of bounces)
			TennisBall t = new TennisBall(x0, y0, ax, ay, vx, vy, v0, theta, spin, 0, lift, dragNoV, dragX, true);
			// makes the balls start at the given coordinates
			t.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of balls to 3 pix
			t.pixRadius = 2;
			// adds the ball to the array list of balls called "balls"
			tballs.add(t);
			// adds the balls to the simulation
			d.addDrawable(t);
			// color
			tballs.get(i).color = colorType;
		}
	}

	protected void doStep() {
		// makes the simulation go faster
		this.setDelayTime(50);

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
				tballs.get(i).pixRadius = 3;
				tballs.get(i).color = Color.yellow;
				tballs.get(i).setIsIn(false);
				// System.out.println(tballs.get(i).getVx() + ", " + tballs.get(i).getPx() + ",
				// " + tballs.get(i).getPy());
			}

			// When the ball goes out of bounds
			if (tballs.get(i).getPy() <= 0 && tballs.get(i).getPx() <= 18.3872 && tballs.get(i).isIsIn() == true
					&& i >= 2.0 / 3.0 * maxBalls) {
				vels.add(tballs.get(i).getV());
				if (tballs.get(maxBalls - 2).getPy() <= 0) {
					this.stopSimulation();
					tballs.get(i).pixRadius = 4;
					System.out.println("velocity = " + vels.get(vels.size() - 1) + "m/s"); // prints out velocity at
																							// that point
					System.out.println("The ball has top spin"); // prints out if the ball has spin
					System.out.println(
							"Hits the gound " + (18.3872 - tballs.get(i).getPx()) + " meters from the service line."); // prints
					// out
					// coordinates
					System.out.println("Has bounced " + tballs.get(i).getBounce() + " times"); // if it's in
					// breaks out of the for loop
					// code stops because because ball went over the wall
				}
			}

			// When the balls hit the ground
			if (tballs.get(i).getPy() <= 0) {
				// counts how many times it's hit the ground
				tballs.get(i).setBounce(tballs.get(i).getBounce() + 1);

				// updates the y velocity to be the y velocity * how much the item loses by
				// hitting the ground
				tballs.get(i).setVy(-tballs.get(i).getVy() * COR);

				// ground friction
				// no spin: ground friction and gains top spin
				if (tballs.get(i).getSpin() == Cl) {
					tballs.get(i).setVx(tballs.get(i).getVx() * COF);
					Cl = Cl - .2;
				}
				// back spin: ground friction and switches from back to top spin
				else if (tballs.get(i).getSpin() == Clback) {
					tballs.get(i).setVx(tballs.get(i).getVx() * COF);
					if (tballs.get(i).getBounce() == 1) {
						Clback = -.1;
					} else {
						Clback = Clback - .2;
					}
					tballs.get(i).setSpin(Clback);
				}
				// top spin: ground friction and gains more top spin
				else if (tballs.get(i).getSpin() == Cltop) {
					tballs.get(i).setVx(tballs.get(i).getVx() * COF);
					Cltop = Cltop - .2;
				}

				// the new angle as the inverse tan of (vy/vx)
				tballs.get(i).setTheta(Math.toDegrees(Math.atan(tballs.get(i).getVy() / tballs.get(i).getVx())));

				// if it's negative it's now 0
				tballs.get(i).setPy(0);
			}

			// return
			if (tballs.get(i).getPx() >= 23.7744) {
				tballs.get(i).setBounce(0);

				// the new angle as the inverse tan of (vy/vx)
				// System.out.println("Vy = " + tballs.get(i).getVy());
				// System.out.println("Vx = " + tballs.get(i).getVx());
				tballs.get(i).setTheta(Math.toDegrees(Math.atan(tballs.get(i).getVy() / tballs.get(i).getVx())));

				// updates the y velocity to be the y velocity * how much the item loses by
				// hitting the ground
				tballs.get(i).setVx(-(velocity + 5) * Math.cos(Math.toRadians(tballs.get(i).getTheta())));
				tballs.get(i).setVy(-(velocity + 5) * Math.sin(Math.toRadians(tballs.get(i).getTheta())));
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

		SimulationControl.createApp(new TennisServe());
	}
}