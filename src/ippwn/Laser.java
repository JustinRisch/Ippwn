package ippwn;

import java.awt.Component;
import java.util.Optional;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class Laser extends JLabel implements Runnable {
	int direction = -1;
	int addToScore = 0;

	public Laser() {
	}

	public Laser(int x) {
		this.setBounds(x - 5, 250, 10, 10);
		this.setText("^");
		this.setVisible(true);
	}

	public boolean hit(Component c) {
		return (this.getBounds().intersection(c.getBounds()).getHeight() > 0);
	}

	public boolean isCollidingWith(Component e) {
		return this.getBounds().intersects(e.getBounds());
	}

	public void run() {
		try {
			while (this.getY() > -10) {
				if (Ippwn.gameOver)
					return;
				this.setLocation(this.getX(), this.getY() + (2 * direction));
				this.revalidate();
				this.repaint();
				Ippwn.ships.parallelStream().map(Optional::ofNullable)
						.filter(x -> x.isPresent())
						.filter(x -> isCollidingWith(x.get())).findFirst()
						.ifPresent(e -> e.ifPresent(x -> {
							this.setLocation(-20, -20);
							x.isHit();
							addToScore = 1; // prevents one laser from getting 2
											// points.
							}));
				try {
					Thread.sleep(9);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Ippwn.contentPane.remove(this);
			Ippwn.shots--;
			Ippwn.score += addToScore;
			Ippwn.frame.setTitle("Score: " + Ippwn.score);
			if (Ippwn.score >= 10 * Ippwn.wave)
				if (Ippwn.wave % 3 == 1) {
					YWing.speed = Ippwn.wave + 2;
					Ippwn.spawnWave2();
					Ippwn.wave++;
				} else if (Ippwn.wave % 3 == 0) {
					Xwing.speed = Ippwn.wave;
					Ippwn.spawnWave1();
					Ippwn.wave++;
				} else if (Ippwn.wave % 3 == 2) {
					BossShip.speed = Ippwn.wave - 1;
					Ippwn.spawnBoss();
					Ippwn.wave++;
				}
		} catch (Exception e) {
			//blocks the glitch where bullets become frozen
			Ippwn.removeComp(this);
			Ippwn.shots--;
		}
	}
}