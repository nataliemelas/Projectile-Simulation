package ProjectileNMK;

import org.opensourcephysics.display.Circle;

public class Projectile extends Circle {
	// extends circle to make the balls circles
	// initializes the attributes for a ball
	double px = 0; // x position
	double py = 0; // y position
	double ax = 0; // x acceleration
	double ay = 0; // y acceleration
	double vx = 0; // x velocity
	double vy = 0; // y velocity
	double v = 0; // initial velocity
	double b = 0; // beta
	double theta = 0; // angle

	/**
	 * Called on to get the x position of the ball
	 * 
	 * @return the x position of the ball at that point in time as a double
	 */
	public double getPx() {
		return px;
	}

	/**
	 * Takes in a double and stores it as the x position of the ball
	 * 
	 * @param px
	 *            x position of the ball at that point in time (is a double)
	 */
	public void setPx(double px) {
		this.px = px;
	}

	/**
	 * Called on to get the y position of the ball
	 * 
	 * @return the y position of the ball at that point in time as a double
	 */
	public double getPy() {
		return py;
	}

	/**
	 * Takes in a double and stores it as the y position of the ball
	 * 
	 * @param py
	 *            y position of the ball at that point in time (is a double)
	 */
	public void setPy(double py) {
		this.py = py;
	}

	/**
	 * Called on to get the x acceleration of the ball
	 * 
	 * @return the x acceleration of the ball at that point in time as a double
	 */
	public double getAx() {
		return ax;
	}

	/**
	 * Takes in a double and stores it as the x acceleration of the ball
	 * 
	 * @param ax
	 *            x acceleration of the ball at that point in time (is a double)
	 */
	public void setAx(double ax) {
		this.ax = ax;
	}

	/**
	 * Called on to get the y acceleration of the ball
	 * 
	 * @return the y acceleration of the ball at that point in time as a double
	 */
	public double getAy() {
		return ay;
	}

	/**
	 * Takes in a double and stores it as the y acceleration of the ball
	 * 
	 * @param ay
	 *            y acceleration of the ball at that point in time (is a double)
	 */
	public void setAy(double ay) {
		this.ay = ay;
	}

	/**
	 * Called on to get the x velocity of the ball
	 * 
	 * @return the x velocity of the ball at that point in time as a double
	 */
	public double getVx() {
		return vx;
	}

	/**
	 * Takes in a double and stores it as the x velocity of the ball
	 * 
	 * @param vx
	 *            x velocity of the ball at that point in time (is a double)
	 */
	public void setVx(double vx) {
		this.vx = vx;
	}

	/**
	 * Called on to get the y velocity of the ball
	 * 
	 * @return the y velocity of the ball at that point in time as a double
	 */
	public double getVy() {
		return vy;
	}

	/**
	 * Takes in a double and stores it as the y velocity of the ball
	 * 
	 * @param vy
	 *            y velocity of the ball at that point in time (is a double)
	 */
	public void setVy(double vy) {
		this.vy = vy;
	}

	/**
	 * Called on to get the initial velocity of the ball
	 * 
	 * @return the initial velocity of the ball at that point in time as a double
	 */
	public double getV() {
		return v;
	}

	/**
	 * Takes in a double and stores it as the initial velocity of the ball
	 * 
	 * @param v
	 *            initial velocity of the ball (is a double)
	 */
	public void setV(double v) {
		this.v = v;
	}

	/**
	 * Called on to get the angle of the ball
	 * 
	 * @return the angle of the ball relative to the x axis (as a double)
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Takes in a double and stores it as the angle of the ball relative to the x
	 * axis
	 * 
	 * @param theta
	 *            angle of the ball relative to the x axis (is a double)
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	/**
	 * Called on to get the beta of the ball
	 * 
	 * @return the beat of the ball
	 */
	public double getB() {
		return b;
	}

	/**
	 * Takes in a double and stores it as the beta
	 * 
	 * @param b
	 *            beta of the ball
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * Creates a ball with a unique set of attributes. Ball(x position, y position,
	 * x acceleration, y acceleration, x velocity, y velocity, initial velocity,
	 * angle).
	 * 
	 * @param px
	 *            x position of the ball at that point in time (is a double)
	 * @param py
	 *            y position of the ball at that point in time (is a double)
	 * @param ax
	 *            x acceleration of the ball at that point in time (is a double)
	 * @param ay
	 *            y acceleration of the ball at that point in time (is a double)
	 * @param vx
	 *            x velocity of the ball at that point in time (is a double)
	 * @param vy
	 *            y velocity of the ball at that point in time (is a double)
	 * @param v
	 *            initial velocity of the ball (is a double)
	 * @param theta
	 *            angle of the ball relative to the x axis (is a double)
	 * @param b
	 *            beta of the ball       
	 */
	public Projectile(double px, double py, double ax, double ay, double vx, double vy, double v, double theta, double b) {
		this.px = px;
		this.py = py;
		this.ax = ax;
		this.ay = ay;
		this.vx = vx;
		this.vy = vy;
		this.v = v;
		this.theta = theta;
		this.b = b;
	}

}
