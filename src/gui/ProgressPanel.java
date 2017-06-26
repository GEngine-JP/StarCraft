package gui;

import java.awt.Graphics;
import java.awt.Image;

import core.ResourceManager;
import core.ResourceManager.Constant;

public class ProgressPanel extends Abstractpanel {

	Image main = ResourceManager.loadImage("background.jpg");
	public ProgressPanel(GameGUI parent, String name) {
		super(parent, name);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(main, 0, 0, null);
		g.drawRect(100, getHeight()-50, 600, 15);
		g.fillRect(100,getHeight()-50, Constant.getProgress(), 15);
		//如果进度完成 
		if(Constant.getProgress()>=590){
			GameGUI parent = (GameGUI) this.getParent();
			parent.switchGame();
		}
	}
}
