package ippwn;

import java.util.Arrays;

@SuppressWarnings("serial")
public class YWing extends Xwing {
	int right = 1;
	public static double speed = 2;
	public static double missleDensity = 20;
	long timeSinceFired = (0); 
	
	public YWing(int x, int y) {
		this.setText("\\/");
		this.setBounds(x, y, 22, 20);
		this.setVisible(true);
	}
	
	@Override 
	public void move() {
		if (Ippwn.gameOver)
			return;
		this.setLocation(this.getX(), this.getY() + (int)(speed));

		if (this.getY() > Ippwn.jta.getHeight())
			this.setLocation((int)(Math.random()*Ippwn.jta.getWidth()), 0);
		
		this.revalidate();
		this.repaint();

		if (this.isCollidingWith(Ippwn.Ship))
			Ippwn.Ship.isHit();
		
		Arrays.stream(Ippwn.ships).filter(x->this.isCollidingWith(x.get()))
		.forEach(x->this.right*=-1);
	}
}
