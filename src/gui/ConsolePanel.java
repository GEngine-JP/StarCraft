package gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import core.GridMapRender;
import core.ResourceManager;

public class ConsolePanel extends JPanel {

	Image main = ResourceManager.loadImage("console.gif");
	
	GridMapRender tileMap;
	
	public MapPanel   map_panel ;
	public WorkPanel  work_panel  ;

	public ConsolePanel(GridMapRender gridMapRender) {
		super();
		setFocusable(false);
		setLayout(null);
		setSize(main.getWidth(null), main.getHeight(null));
	
		//初始化map_panel和work_panel
		this.tileMap = gridMapRender;
		map_panel = new MapPanel(gridMapRender);
		work_panel = new WorkPanel(gridMapRender);
		map_panel.setLocation(10, 20);
		work_panel.setLocation(10, 158);
		add(map_panel);
		add(work_panel);
		gridMapRender.setConsolePanel(this);

		 
	}
 
	
	public void paintComponent(Graphics g) {
		 g.drawImage(main, 0, 0, null);
 
	 }
	
}
