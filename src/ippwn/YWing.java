package ippwn;

@SuppressWarnings("serial")
public class YWing extends Xwing {
	int right = 1;
	public static double speed = 2;
	public boolean isDead=false;
	public static double missleDensity = 10;
	long timeSinceFired = (0);

	public YWing(int x, int y) {
		this.setText("\\/");
		this.setBounds(x, y, 22, 20);
		this.setVisible(true);
	}
	public synchronized void isHit() {
		isDead=true;
		Xwing.speed += .1;
		Ippwn.removeComp(this);
	}
	@Override
	public void move() {
		if (Ippwn.gameOver || isDead)
			return;
		this.setLocation(this.getX(), this.getY() + (int) (speed));

		if (this.getY() > Ippwn.jta.getHeight())
			this.setLocation((int) (Math.random() * Ippwn.jta.getWidth()), 0);

		this.revalidate();
		this.repaint();

		if (this.isCollidingWith(Ippwn.Ship))
			Ippwn.Ship.isHit();
		if (this.getY() > 30 // delay firing until they get to the second row.
			&& this.timeSinceFired() > (missleDensity * 1000)* Math.random()) {
			Ippwn.fire((int)this.getBounds().getCenterX(), this.getY());
			timeSinceFired = System.currentTimeMillis();
		}
		Ippwn.ships.parallelStream().filter(x -> this.isCollidingWith(x))
		.forEach(x -> this.right *= -1);
	}
	public long timeSinceFired(){
		return timeSinceFired;
	}
	
}
