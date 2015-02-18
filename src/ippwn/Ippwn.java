package ippwn;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Ippwn extends JFrame {
	public static int score = 0;
	public static PlayField contentPane;
	public static int wave = 0;
	public static JTextArea jta = new JTextArea();
	public static int shots = 0;
	public static Collection<Xwing> ships = Collections.synchronizedCollection(new ArrayList<Xwing>());
	public static Ippwn frame;
	public static ship Ship;
	public static BossShip boss;
	public static boolean gameOver = false;

	public static void fire(int x, int y) {
		Missle missle = new Missle(x, y);
		contentPane.add(missle);
		new Thread(missle).start();
	}

	public static void bossFire(int x, int y) {
		Missle missle = new BossMissle(x, y);
		contentPane.add(missle);
		new Thread(missle).start();
	}

	public synchronized static void removeComp(Component e) {
		if (e instanceof Xwing){
		ships.remove(e);
		}
		contentPane.remove(e);
		e = null;
		System.gc();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				frame = new Ippwn();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	/**
	 * Create the frame.
	 */
	public Ippwn() {
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 450, 300);
		contentPane = new PlayField();
		jta.setBounds(0, 0, 450, 300);
		jta.setVisible(false);
		contentPane.add(jta);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		ControlListener cl = (e) -> {
			if (Ippwn.gameOver)
				return;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				if (Ippwn.wave == 0)
					spawnWave1();
				wave = 1;
				break;
			case KeyEvent.VK_RIGHT:
				Ship.move(1);
				break;
			case KeyEvent.VK_LEFT:
				Ship.move(-1);
				break;
			case KeyEvent.VK_SPACE:
				if (wave == 0)
					break;
				if (shots > 2)
					break;
				Laser L = new Laser((int) Ship.getBounds().getCenterX());
				contentPane.add(L);
				Thread t = new Thread(L);
				t.start();
				shots++;
				break;
			}

		};
		this.setTitle("Press enter to begin!");
		this.addKeyListener(cl);
	}

	// Here we're using default methods to turn this into a functional
	// interface.
	@FunctionalInterface
	interface ControlListener extends KeyListener {

		// default public void keyPressed(KeyEvent e){}
		default public void keyReleased(KeyEvent e) {
		}

		default public void keyTyped(KeyEvent e) {
		}

	}

	public static void spawnWave1() {
		
		for (int i = 0; i < 10; i++)
			ships.add(new Xwing(i * 40, 20));
		ships.stream().forEach(x -> {
			new Thread(x).start();
			Ippwn.contentPane.add(x);
		});
	}

	public static void spawnBoss() {
		boss = new BossShip(0, 0);
		ships.add(boss);
		
		new Thread(boss).start();
		contentPane.add(boss);
	}

	public static void spawnWave2() {
		for (int i = 0; i < 10; i++)
			ships.add(new YWing(i * 40, 30));
		ships.stream().forEach(x -> {
			new Thread(x).start();
			Ippwn.contentPane.add(x);
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	class PlayField extends JPanel {
		public PlayField() {
			Ship = new ship();
			this.add(Ship);
		}
	}
}