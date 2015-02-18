package ippwn;

@SuppressWarnings("serial")
public class BossShip extends Xwing {
	int hp = 10;
	public static double speed = 2;
	public boolean isDead = false;

	public BossShip(int x, int y) {
		this.setText("<(o_o)>");
		this.setBounds(x, y, 51, 13);
		this.setVisible(true);
	}

	@Override
	synchronized public void isHit() {
		hp--;
		speed += .5;
		if (hp <= 0) {
			Ippwn.removeComp(this);
			isDead = true;
		}
	}

	@Override
	public void move() {

		if (Ippwn.gameOver || isDead)
			return;
		this.setLocation((this.getX() + (int) speed * right),
				(int) (this.getY() + speed / 2));

		if (this.getX() <= 0) {
			right *= -1;
			setLocation(0, this.getY() + 30);
		} else if (this.getX() >= Ippwn.jta.getWidth() - this.getWidth()) {
			right *= -1;
			setLocation(Ippwn.jta.getWidth() - this.getWidth(),
					this.getY() + 30);
		}
		if (this.getY() > Ippwn.jta.getHeight() - this.getHeight())
			this.setLocation(this.getX(), 0);

		this.revalidate();
		this.repaint();

		if (this.getY() > 30 // delay firing until they get to the second row.
				&& this.timeSinceFired() > (20_000) * Math.random()) {
			Ippwn.bossFire(this.getX(), this.getY());
			timeSinceFired = System.currentTimeMillis();
		}
		if (this.isCollidingWith(Ippwn.Ship))
			Ippwn.Ship.isHit();

	}
}

@SuppressWarnings("serial")
class BossMissle extends Missle {

	public BossMissle(int x, int y) {
		this.setBounds(x - 5, y, 8, 12);
		this.setText("()");
		this.setVisible(true);
	}

}
