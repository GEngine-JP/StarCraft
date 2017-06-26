package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import util.Elastic;
import core.ResourceManager;

public class MainMenuPanel extends Abstractpanel {

	private MenuItem item;

	Image main = ResourceManager.loadImage("background3.jpg");

	public MainMenuPanel(GameGUI parent, String name) {
		super(parent, name);
		item = new MenuItem(this);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(main, 0, 0, null);
	}

	public void update(long elapsedTime) {
		item.update(elapsedTime);
	}
	
	public void reset(){
		item.elastic.slide(0, 100);
	}
	
	private class MenuItem extends JPanel {

		JButton single = new JButton("Single Player");

		JButton network = new JButton("MutilPlayer");

		JButton options = new JButton("Options");

		JButton exit = new JButton("Exit");

		int x, y;

		Elastic elastic = new Elastic(0, 100);

		public MenuItem(MainMenuPanel parent) {
			this.setLayout(new GridLayout(4, 1));
			setSize(250, 400);
			x = (parent.getWidth() - getWidth()) / 2;
			y = (parent.getHeight() - getHeight()) / 2;
			setLocation(x, y);

			parent.add(this);
			this.initButton(single);
			this.initButton(network);
			this.initButton(options);
			this.initButton(exit);
			
			//add(new JTextField());
		}

		private void initButton(JButton button) {
			button.setBorder(null);
			button.setForeground(Color.WHITE);
			button.setIgnoreRepaint(true);
			button.setFocusable(false);
			button.setBorder(null);
			button.setContentAreaFilled(false);
			button.addActionListener(listenner);
			this.add(button);

		}

		public void paintComponent(Graphics g) {
			g.setColor(fillColor);
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
			g.setColor(Color.red);
			g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 10, 10);
		}

		public void update(long elapsedTime) {
			y = elastic.update(elapsedTime);
			setLocation(x, y);
		}

		ActionListener listenner = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JButton jb = (JButton) e.getSource();
				GameGUI gamePanel = (GameGUI) MenuItem.this.getParent()
						.getParent();
				String text = jb.getText();
				if (text.equals("Single Player")) {
					elastic.slide(y, -getHeight());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					gamePanel.switchSingleGame();
					//gamePanel.switchProgress();
				}
				if (text.equals("MutilPlayer")) {
					elastic.slide(y, -getHeight());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					gamePanel.switchMutilGame();
				}
				if (text.equals("Options")) {
					elastic.slide(y, -getHeight());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					gamePanel.switchMainMenu();
				}
				if (text.equals("Exit")) {
					System.exit(-1);
				}

			}
		};
	}
}