package ProjectileNMK;

import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

public class GreenMonster extends AbstractSimulation {
	// simulation
	DisplayFrame d = new DisplayFrame("Meters", "Meters", "Baseball Simulation");

	// given beta
	double b = .02;

	// sets the amount of seconds in each time interval
	double timeStep = .1;

	// acceleration of gravity
	double g = 9.81;
	double ax = 0;
	double ay = 0;
	
	// sets the initial velocity
	double v0 = 86.0;

	// starting angle
	double theta = 20.0;
	// smallest angle
	double MinTheta = 20.0;
	// largest angle
	double MaxTheta = 85.0;

	// creates array list of balls
	ArrayList<Ball> balls = new ArrayList<Ball>();

	@Override
	public void initialize() {
		// simulation is visible
		d.setVisible(true);
		// can close simulation easily
		d.setDefaultCloseOperation(3);
		// creates fenway wall
		// it is 1 m thick and 11.3 m tall
		DrawableShape wall = DrawableShape.createRectangle(100.5, 5.65, 1, 11.3);
		// adds wall to simulation
		d.addDrawable(wall);
		// xmin xmax ymin ymax
		d.setPreferredMinMax(-1, 105, 0, 55);

		// initializes each ball
		for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
			ax = -b * v0 * Math.cos(Math.toRadians(theta)) * v0 * Math.cos(Math.toRadians(theta));
			ay = -g - (b * v0 * Math.sin(Math.toRadians(theta)) * v0 * Math.sin(Math.toRadians(theta)));
			// creates a ball:
			// starts at (1.1)
			// x acceleration = 0
			// y acceleration = -9.81
			// x velocity = initial velocity * cosine of given angle
			// y velocity = initial velocity * sine of given angle
			// initial velocity = inputed initial velocity;
			// first ball starts with an angle thats given
			Ball b = new Ball(0, 1, ax, ay, v0 * Math.cos(Math.toRadians(theta)), v0 * Math.sin(Math.toRadians(theta)),
					v0, theta);
			// makes the balls start at the given coordinates
			b.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of balls to 3 pix
			b.pixRadius = 2;
			// adds the ball to the array list of balls called "balls"
			balls.add(b);
			// adds the balls to the simulation
			d.addDrawable(b);
			// angle increases by .1 for each ball
			theta = theta + .1;
		}
	}

	protected void doStep() {
		// make sure to update acceleration/velocity/position for each ball
		for (int i = 0; i < 10 * (MaxTheta - MinTheta); i++) {
			// makes the simulation go faster
			this.setDelayTime(10);

			// sets the color of each ball
			balls.get(i).color = Color.magenta;

			// ACCELERATION
			// setting acceleration y
			// if the y velocity is greater than 0
			if (balls.get(i).getVy() > 0) {
				// then you make the y acceleration -g - (b*v^2)
				balls.get(i).setAy(-g - (b * balls.get(i).getVy() * balls.get(i).getVy()));
			}
			// if the y velocity is less than or equal to 0
			else if (balls.get(i).getVy() <= 0) {
				// then you make they acceleration -g + (b*v^2)
				balls.get(i).setAy(-g + (b * balls.get(i).getVy() * balls.get(i).getVy()));
			}

			// acceleration in the horizontal direction has no gravity, only air resistance
			// so x acceleration is just -(b*v^2)
			balls.get(i).setAx(-(b * balls.get(i).getVx() * balls.get(i).getVx()));

			// VELOCITY
			// updates velocity x (v(t) = v0 + at)
			// making the new x velocity = old x velocity + x acceleration *time
			balls.get(i).setVx(balls.get(i).getVx() + (balls.get(i).getAx() * timeStep));
			// making the new y velocity = old y velocity + y acceleration *time
			balls.get(i).setVy(balls.get(i).getVy() + (balls.get(i).getAy() * timeStep));

			// POSITION
			// finds position using right hand rule under an imaginary velocity/time graph
			// new y position = old y position + time*y velocity
			// time * y velocity is a rectangle and its area is the change in the ball's
			// position
			balls.get(i).setPy(balls.get(i).getPy() + (timeStep * balls.get(i).getVy()));

			// new x position = old x position + time*x velocity
			// time * x velocity is a rectangle and its area is the change in the ball's
			// position
			balls.get(i).setPx(balls.get(i).getPx() + (timeStep * balls.get(i).getVx()));

			// setting position on simulation
			balls.get(i).setXY(balls.get(i).getPx(), balls.get(i).getPy());

			// If BALL WENT OVER THE WALL:
			// then the x coordinate is >= 100 and y coordinate is >= 11.3)
			if (balls.get(i).getPx() >= 100 && balls.get(i).getPy() >= 11.3) {
				this.stopSimulation(); // stops the simulation
				System.out.println("velocity = " + balls.get(i).getV() + "m/s"); // prints out velocity at that point
				System.out.println("angle = " + balls.get(i).getTheta() + "°"); // prints out angle at that point
				System.out.println("(" + balls.get(i).getPx() + ", " + balls.get(i).getPy() + ")"); // prints out
																									// coordinates
				break; // breaks out of the for loop
				// code stops because because ball went over the wall
			}

			// IF BALL DIDN'T GO OVER THE WALL:
			// when the balls hit the ground
			else if (balls.get(i).getPy() < 0) {
				balls.get(i).color = Color.green; // turns the balls white (as if they "disappeared")
				if (i == 649) { // when the final ball is on the ground
					for (int j = 0; j < 10 * (MaxTheta - MinTheta); j++) { // goes through every ball
						balls.get(j).setV(balls.get(j).getV() + .1); // increases the velocity by +.1
						balls.get(j).setPx(0); // sets the x position to 1
						balls.get(j).setPy(1); // sets the y position to 1
						// updates the x velocity to be the new velocity * cos of angle
						balls.get(j).setVx(balls.get(j).getV() * Math.cos(Math.toRadians(balls.get(j).getTheta())));
						// updates the y velocity to be the new velocity * sine of angle
						balls.get(j).setVy(balls.get(j).getV() * Math.sin(Math.toRadians(balls.get(j).getTheta())));
						// keeps the same angle because each ball has a different angle but the same
						// velocity
						// keeps the same x and y acceleration because it will update that according to
						// the new velocity
					}
				}
			}
		}
	}

	public void reset() {
		// sets x coordinate of starting position of balls to 1
		control.setValue("x", 0);
		// sets y coordinate of starting position of ball to y
		control.setValue("y", 1);
		// sets the initial velocity of balls to v0 (which can be changed at the top of
		// the code)
		control.setValue("v0", v0);
	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new GreenMonster());
	}
}
