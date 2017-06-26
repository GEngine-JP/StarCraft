package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JPanel;

import core.ResourceManager;

public class NetworkPanel extends Abstractpanel {

	private MenuItem menuItem;
	
	private Image main = ResourceManager.loadImage("background3.jpg");
	
	private Color fillColor = new Color(32, 32, 100, 80);
	
	public NetworkPanel(GameGUI parent, String name) {
		super(parent, name);
		menuItem = new MenuItem(this);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(main, 0, 0, null);
	

	}

	private class MenuItem extends JPanel {

		public MenuItem(NetworkPanel parent) {
			this.setLayout(new GridLayout(4, 1));
			setSize(250, 400);
			setLocation((parent.getWidth() - getWidth()) / 2, (parent
					.getHeight() - getHeight()) / 2);
			parent.add(this);
		}
		
		public void paintComponent(Graphics g) {
			g.setColor(fillColor);
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
			g.setColor(Color.red);
			g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 10, 10);
			g.drawString("Not Supported", (getWidth()-80) / 2, getHeight() / 2);
		}
	}
	
	
}
