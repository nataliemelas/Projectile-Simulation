package ProjectileNMK;

import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;
import polyfun.Polynomial;

public class RealData extends AbstractSimulation {
	/**
	 * QUESTION TWO:
	 * 
	 * This is ethan's projective data! (Mine was weird)
	 * Height: 1.17m
	 * Theta: 42 degrees
	 * 1 click velocity: v0 = 2.96m/s
	 * 
	 * Shot in theory (no air resistance): 1.608meters
	 * 
	 * Three trials:
	 * Shot 1: - 14.75 mm
	 * Shot 2: - 2.6 mm 
	 * Shot 3: - 24.5 mm
	 * Average shot: -13.95 mm
	 * So average landing spot: 1608m - 13.95 = 1594 = 1.594 meters
	 */
	//creates display frame
	DisplayFrame d = new DisplayFrame("Meters", "Meters", "Physics Projectile Simulation");

	// how many balls you shoot at a time
	double maxBalls = 250;

	// given beta
	double b = 0;

	// sets the amount of seconds in each time interval
	double timeStep = .001;

	// starting angle
	double theta = 42;

	// sets the initial velocity
	double v0 = 2.96;
	// initializes the x velocity
	double vx = v0 * Math.cos(Math.toRadians(theta));
	// initializes the y velocity
	double vy = v0 * Math.sin(Math.toRadians(theta));

	// acceleration of gravity
	double g = 9.81;
	// initializes the x acceleration
	double ax = -b * v0 * Math.cos(Math.toRadians(theta)) * v0 * Math.cos(Math.toRadians(theta));
	// initializes the y acceleration
	double ay = -g - (b * v0 * Math.sin(Math.toRadians(theta)) * v0 * Math.sin(Math.toRadians(theta)));

	// starting height
	double h = 1.17;

	// landing spot -- final x position
	double FinalPX = 1.594;
	// range: final x position - percent error to x position + percent error
	double PercentError = .0005;

	// creates array list of beta
	ArrayList<Projectile> proj = new ArrayList<Projectile>();

	@Override
	public void initialize() {
		// simulation is visible
		d.setVisible(true);
		// can close simulation easily
		d.setDefaultCloseOperation(3);
		// xmin xmax ymin ymax
		d.setPreferredMinMax(0, 2, -1, 3);

		// initializes each ball
		for (int i = 0; i < maxBalls; i++) {
			// creates a ball:
			// starts at (1.1)
			// x acceleration = 0
			// y acceleration = -9.81
			// x velocity = initial velocity * cosine of given angle
			// y velocity = initial velocity * sine of given angle
			// initial velocity = inputed initial velocity;
			// the angle at which it's shot
			// the beta with which the ball is shot
			Projectile bs = new Projectile(0, h, ax, ay, vx, vy, v0, theta, b);
			// makes the beta start at the given coordinates
			bs.setXY(control.getDouble("x"), control.getDouble("y"));
			// sets radius of beta to 3 pix
			bs.pixRadius = 3;
			// adds the ball to the array list of beta called "beta"
			proj.add(bs);
			// adds the beta to the simulation
			d.addDrawable(bs);
			// beta increases for each ball
			b = b + .0001;
		}
	}

	protected void doStep() {
		this.setDelayTime(1);
		// make sure to update acceleration/velocity/position for each ball
		for (int i = 0; i < maxBalls; i++) {
			// makes the simulation go faster
			this.setDelayTime(1);

			// sets the color of each ball
			proj.get(i).color = Color.magenta;

			// ACCELERATION
			// setting acceleration y
			// if the y velocity is greater than 0
			if (proj.get(i).getVy() > 0) {
				// then you make the y acceleration -g - (b*v^2)
				proj.get(i).setAy(-g - (proj.get(i).getB() * proj.get(i).getVy() * proj.get(i).getVy()));
			}
			// if the y velocity is less than or equal to 0
			else if (proj.get(i).getVy() <= 0) {
				// then you make they acceleration -g + (b*v^2)
				proj.get(i).setAy(-g + (proj.get(i).getB() * proj.get(i).getVy() * proj.get(i).getVy()));
			}

			// acceleration in the horizontal direction has no gravity, only air resistance
			// so x acceleration is just -(b*v^2)
			proj.get(i).setAx(-(proj.get(i).getB() * proj.get(i).getVx() * proj.get(i).getVx()));

			// VELOCITY
			// updates velocity x (v(t) = v0 + at)
			// making the new x velocity = old x velocity + x acceleration *time
			proj.get(i).setVx(proj.get(i).getVx() + (proj.get(i).getAx() * timeStep));
			// making the new y velocity = old y velocity + y acceleration *time
			proj.get(i).setVy(proj.get(i).getVy() + (proj.get(i).getAy() * timeStep));

			// POSITION
			// finds position using right hand rule under an imaginary velocity/time graph
			// new y position = old y position + time*y velocity
			// time * y velocity is a rectangle and its area is the change in the ball's
			// position
			proj.get(i).setPy(proj.get(i).getPy() + (timeStep * proj.get(i).getVy()));
			// new x position = old x position + time*x velocity
			// time * x velocity is a rectangle and its area is the change in the ball's
			// position
			proj.get(i).setPx(proj.get(i).getPx() + (timeStep * proj.get(i).getVx()));
			// setting position on simulation
			proj.get(i).setXY(proj.get(i).getPx(), proj.get(i).getPy());

			// If BALL WENT THE CORRECT DISTANCE:
			if (proj.get(i).getPx() >= FinalPX - PercentError && proj.get(i).getPx() <= FinalPX + PercentError
					&& proj.get(i).getPy() <= 0) {
				// prints x and y positions
				System.out.println("(" + proj.get(i).getPx() + ", " + proj.get(i).getPy() + ")");
				// prints out beta at that point
				System.out.println("Beta = " + proj.get(i).getB());
				System.out.println("");
				// stops the simulation
				this.stopSimulation();
			}

			// IF NO BALLS WENT THE CORRECT DISTANCE:
			// when the balls hits the ground, their y positions are <= 0
			if (proj.get(i).getPy() <= 0) {
				// turns the balls that hit in the range green
				proj.get(i).color = Color.green;
				// increases the ball's beta by .0001
				proj.get(i).setB(proj.get(i).getB() + .0001);
				// resets the x position to 0
				proj.get(i).setPx(0);
				// resets the y position to the initial height of the shot
				proj.get(i).setPy(h);
				// resets the initial x velocity
				proj.get(i).setVx(v0 * Math.cos(Math.toRadians(theta)));
				// resets the initial y velocity
				proj.get(i).setVy(v0 * Math.sin(Math.toRadians(theta)));
			}
		}
	}

	public void reset() {
		// sets x coordinate of starting position of beta to 0
		control.setValue("x", 0);
		// sets y coordinate of starting position of ball to the initial height
		control.setValue("y", h);
		// sets the initial velocity of the ball to v0 (which can be changed at the top
		// of
		// the code)
		control.setValue("v0", v0);
	}

	public static void main(String[] args) {
		// allows us to see the simulation
		SimulationControl.createApp(new RealData());
	}
}
