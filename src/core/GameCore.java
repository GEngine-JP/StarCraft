package core;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Simple abstract class used for testing. Subclasses should implement the
 * draw() method.
 */
public abstract class GameCore extends JFrame {

	protected static final int FONT_SIZE = 10;

	private boolean isRunning;

	protected JFrame window;

	public void stop() {

	}

	/**
	 * Calls init() and gameLoop()
	 */
	public void run() {
		init();
		gameLoop();
	}

	/**
	 * Sets full screen mode and initiates and objects.
	 */
	public void init() {

		setUndecorated(true);
		setTitle("JStarCraft");
		setIconImage(ResourceManager.loadImage("title.png"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
		setIgnoreRepaint(true);
		setResizable(false);
		setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		setBackground(Color.black);
		setForeground(Color.white);
		createBufferStrategy(2);
		isRunning = true;
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				ResourceManager.loadImage("cur.png"), new Point(0, 0), "cur"));
		window = getWindow();
		NullRepaintManager.install();
		window.setLayout(null);
		Container contentPane = getWindow().getContentPane();
		((JComponent) contentPane).setOpaque(false);

	}

	/**
	 * Runs through the game loop until stop() is called.
	 */
	public void gameLoop() {

		BufferStrategy strategy = getBufferStrategy();
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		while (isRunning) {

			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;

			// update
			update(elapsedTime);

			// draw the screen
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);		
			// g.drawImage(ResourceManager.loadImage("background3.jpg"), 0, 33,
			// null);
			draw(g);
			g.dispose();

			if (!strategy.contentsLost()) {
				strategy.show();
			}

			// take a nap
			try {
				Thread.sleep(5);
			} catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * Updates the state of the game/animation based on the amount of elapsed
	 * time that has passed.
	 */
	public void update(long elapsedTime) {
		// do nothing
	}

	/**
	 * Draws to the screen. Subclasses must override this method.
	 */
	public abstract void draw(Graphics2D g);

	public JFrame getWindow() {
		return this;
	}
}
