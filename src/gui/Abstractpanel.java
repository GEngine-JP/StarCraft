package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import core.ResourceManager;

public class Abstractpanel extends JPanel {
 
	protected static final Color borderColor = new Color(255,0,0,100);	
	protected static final Color fillColor = new Color(0,0,0,70);
	protected static final Color textColor = new Color(76,196,40);
	protected static final Color titleColor = new Color(164,180,248);
	protected static final Font font = new Font("Tahoma",Font.BOLD,12);
	protected Image main = ResourceManager.loadImage("background3.jpg");
	protected GameGUI gameGUI;
	
	public Abstractpanel(GameGUI gameGUI, String name) {
		setLayout(null);
		setSize(gameGUI.getWidth(), gameGUI.getHeight());
		gameGUI.add(this, name);
		this.gameGUI = gameGUI;

	}
	protected void initButton(final JButton button,ActionListener listenner) {
		button.setBorder(BorderFactory.createLineBorder(borderColor));
		//button.setFont(new Font("Arial Bold",Font.PLAIN,14));
		button.setFont(font);
		button.setForeground(textColor);
		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		button.addActionListener(listenner);
		 
	}
	public void update(long elapsedTime) {

	}
	
}	


