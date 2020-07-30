package ProjectileNMK;

import org.opensourcephysics.display.Circle;

public class TennisBall extends Circle {
	// extends circle to make the balls circles
	// initializes the attributes for a ball
	double px = 0; // x position
	double py = 0; // y position
	double ax = 0; // x acceleration
	double ay = 0; // y acceleration
	double vx = 0; // x velocity
	double vy = 0; // y velocity
	double v = 0; // initial velocity
	double theta = 0; // angle
	double spin = 0; // spin
	int bounce = 0;
	double lift = 0;
	double dragNov = 0;
	double dragX = 0;
	boolean IsIn = true;

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
	 * Called on to get the spin of the ball
	 * 
	 * @return the spin of the ball
	 */
	public double getSpin() {
		return spin;
	}

	/**
	 * Takes in a double and stores it as the spin of the ball
	 * 
	 * @param spin
	 *            spin of the ball (spin is a double)
	 */
	public void setSpin(double spin) {
		this.spin = spin;
	}

	/**
	 * Called on to get the number of times the ball has bounced
	 * 
	 * @return number of times the ball bounced
	 */
	public int getBounce() {
		return bounce;
	}

	/**
	 * Takes in a double and stores it as the number of times the ball has hit the
	 * ground
	 * 
	 * @param bounce
	 *            number of times the ball bounces (is a double)
	 */
	public void setBounce(int bounce) {
		this.bounce = bounce;
	}

	/**
	 * Called on to get the lift coefficient of the ball
	 * 
	 * @return the lift coefficient of the ball
	 */
	public double getLift() {
		return lift;
	}

	/**
	 * Takes in a double and stores it as the ball's lift coefficient
	 * 
	 * @param lift
	 *            the lift coefficient of the ball (is a double)
	 */
	public void setLift(double lift) {
		this.lift = lift;
	}

	/**
	 * Called on to get the drag without velocity^2 of the ball
	 * 
	 * @return the drag without velocity^2 of the ball
	 */
	public double getDragNov() {
		return dragNov;
	}

	/**
	 * Takes in a double and stores it as the drag without velocity^2 of the ball
	 * 
	 * @param dragNov
	 *            the drag without velocity^2 of the ball (is a double)
	 */
	public void setDragNov(double dragNov) {
		this.dragNov = dragNov;
	}

	/**
	 * Called on to get the horizontal drag of the ball (no lift)
	 * 
	 * @return the horizontal drag of the ball (no lift)
	 */
	public double getDragX() {
		return dragX;
	}

	/**
	 * Takes in a double and stores it as the horizontal drag of the ball (no lift)
	 * 
	 * @param dragNov
	 *            the horizontal drag of the ball (no lift) (is a double)
	 */
	public void setDragX(double dragX) {
		this.dragX = dragX;
	}

	/**
	 * Called on to see if the ball is in or not
	 * 
	 * @return if the ball is in or not (true is in, false is not)
	 */
	public boolean isIsIn() {
		return IsIn;
	}

	/**
	 * Takes in true or false and stores it as whether the ball is in (true) or not
	 * (false)
	 * 
	 * @param isIn
	 *            true if the ball is in, false if the ball is not
	 */
	public void setIsIn(boolean isIn) {
		IsIn = isIn;
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
	 * @param spin
	 *            spin of the ball (is a double)
	 * @param bounce
	 *            number of times the ball bounces (is a double)
	 * @param lift
	 *            lift coefficient on the ball (is a double)
	 * @param dragNov
	 *            the drag without velocity^2 of the ball (is a double)
	 * @param dragNov
	 *            the horizontal drag of the ball (no lift) (is a double)
	 * @param isIn
	 *            true if the ball is in, false if the ball is not
	 */
	public TennisBall(double px, double py, double ax, double ay, double vx, double vy, double v, double theta,
			double spin, int bounce, double lift, double dragNov, double dragX, boolean isIn) {
		this.px = px;
		this.py = py;
		this.ax = ax;
		this.ay = ay;
		this.vx = vx;
		this.vy = vy;
		this.v = v;
		this.theta = theta;
		this.spin = spin;
		this.bounce = bounce;
		this.lift = lift;
		this.dragNov = dragNov;
		this.dragX = dragX;
		this.IsIn = isIn;
	}

}
