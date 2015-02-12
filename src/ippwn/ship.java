package ippwn;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ship extends JLabel {
	int x, y, width, height;
	double speed =10;

	public ship() {
		this.setText("(o)");
		x = 200;
		y = 250;
		width = 16;
		height = 15;
	
		this.setBounds(x, y, width, height);
		this.setVisible(true);
	}
	public void isHit(){
		Ippwn.gameOver = true;
		this.setText("!BOOM!");
		this.setBounds(this.getX()-10, this.getY(), 100, height);
	}
	public void move(double i) {
		double temp = x + (speed * i);
		if (temp>0 && temp <450-width)
			x = (int) temp;
		this.setBounds(x, y, this.getWidth(), this.getHeight());
		
		this.revalidate();
		this.repaint();
	}


}

