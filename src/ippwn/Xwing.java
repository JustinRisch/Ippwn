package ippwn;

import java.awt.Component;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Xwing extends JLabel implements Runnable {
	int right = 1;
	public static double speed = 2;
	public static double missleDensity = 7;
	public boolean isDead=false;
	long timeSinceFired = (0);

	public Xwing() {
		this.setText(">o<");
		this.setBounds(40, 20, 28, 20);
		this.setVisible(true);
	}

	public synchronized void isHit() {
		isDead=true;
		this.setLocation(-50,-50);
		Xwing.speed += .1;
		Ippwn.removeComp(this);
	}

	public Xwing(int x, int y) {
		this.setText(">o<");
		this.setBounds(x, y, 28, 20);
		this.setVisible(true);
	}

	public void move() {
		if (Ippwn.gameOver || isDead)
			return;
		this.setLocation((this.getX() + (int) speed * right), this.getY());

		if (this.getX() <= 0) {
			right *= -1;
			setLocation(0, this.getY() + 30);
		} else if (this.getX() >= Ippwn.jta.getWidth() - this.getWidth()) {
			right *= -1;
			setLocation(Ippwn.jta.getWidth() - this.getWidth(),
					this.getY() + 30);
		}
		this.revalidate();
		this.repaint();

		if (this.getY() > 30 // delay firing until they get to the second row.
				&& this.timeSinceFired() > (missleDensity * 1000)
						* Math.random()) {
			Ippwn.fire(this.getX(), this.getY());
			timeSinceFired = System.currentTimeMillis();
		}
		if (this.isCollidingWith(Ippwn.Ship))
			Ippwn.Ship.isHit();

	}

	public long timeSinceFired() {
		return System.currentTimeMillis() - this.timeSinceFired;
	}

	public boolean isCollidingWith(Component e) {
		return this.getBounds().intersects(e.getBounds());
	}

	public void run() {
		try {
			while (this.getX() >= 0) {
				if (Ippwn.gameOver || isDead)
					return;
				move();
				Thread.sleep(15);
			}
			this.setVisible(false);
		} catch (Exception e) {
			Ippwn.removeComp(this);
		}
	}
}
