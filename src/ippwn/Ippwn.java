package ippwn;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
	@SuppressWarnings("unchecked")
	public static Optional<Xwing>[] ships = new Optional[10];
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
		e.setLocation(-40, -40);
		contentPane.remove(e);
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
		AtomicInteger ai = new AtomicInteger();
		Arrays.setAll(ships, x -> Optional.ofNullable(new Xwing(ai
				.incrementAndGet() * 40, 20)));
		Arrays.stream(ships).filter(ship -> ship.isPresent()).forEach(x -> {
			new Thread(x.get()).start();
			Ippwn.contentPane.add(x.get());
		});
	}

	public static void spawnBoss() {
		boss = new BossShip(0, 0);
		Arrays.setAll(ships, x -> Optional.ofNullable(null));
		ships[0] = Optional.of(boss);
		new Thread(ships[0].get()).start();
		contentPane.add(ships[0].get());
	}

	public static void spawnWave2() {
		AtomicInteger ai = new AtomicInteger();
		Arrays.setAll(ships, x -> Optional.ofNullable(new YWing(ai
				.incrementAndGet() * 40, 30)));
		Arrays.stream(ships).filter(ship -> ship.isPresent()).forEach(x -> {
			new Thread(x.get()).start();
			Ippwn.contentPane.add(x.get());
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