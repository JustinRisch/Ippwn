package ippwn;

@SuppressWarnings("serial")
public class Missle extends Laser {

	int direction = 1;

	public Missle(int x, int y) {
		this.setBounds(x - 5, y + 5, 5, 10);
		this.setText("|");
		this.setVisible(true);

	}
	public Missle(){}
	public void run() {
		while (this.getY() < Ippwn.contentPane.getHeight()) {
			if (Ippwn.gameOver)
				return;
			this.setLocation(this.getX(), this.getY() + (2 * direction));
			this.revalidate();
			this.repaint();
			if (this.isCollidingWith(Ippwn.Ship))
				Ippwn.Ship.isHit();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Ippwn.contentPane.remove(this);

	}
}
