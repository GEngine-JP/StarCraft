package gui;

import icon.BaseIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JPanel;

import tile.Builder;
import tile.Scv;
import tile.Tile;
import core.GridMapRender;
import core.ResourceManager;
import core.ResourceManager.Constant;

@SuppressWarnings("serial")
public class WorkPanel extends JPanel {

	private static final BaseIcon[][] DEFAULT_ICON = new BaseIcon[2][3];
	
	private static final Color color = new Color(0x0c48cc);
	
	private static final int GRID_SIZE = 41;

	private static final int GRID_W = 38;

	private static final int GRID_H = 38;

	private MouseListener listener = new MouseListener();
	
	private GridMapRender gridMapRender;
	 
	private BaseIcon[][] currentIcon = DEFAULT_ICON;

	private static final Map<Integer,BaseIcon[][]> ICONS_TABLE = Constant.ICON_TABLE;
	
	private Image progress = ResourceManager.loadImage("build.gif");
	
	private Builder builder;
	
	public WorkPanel(GridMapRender gridMapRender) {
		super();
		setSize(128, 82);
		setFocusable(false);
		this.addMouseListener(listener);
		this.gridMapRender = gridMapRender;
	}

	public void dispatch(Tile tile) {
		this.builder = null;
		if (tile != null) {
			
			currentIcon = ICONS_TABLE.get(tile.getId());
			currentIcon = currentIcon==null?DEFAULT_ICON:currentIcon;
			
			if (tile instanceof Builder) 
			{
				Builder builder = (Builder) tile;
				if (builder.isBuilding())
					this.builder = builder;
			}
			
			//如果是农民在修房子不能显示
			if(tile instanceof Scv){
				Scv scv = (Scv)tile;
				if(scv.isStartBuild()||scv.isBuilding()){
					currentIcon=DEFAULT_ICON;
				}
			}
			 
			
			
		} else {
			currentIcon = DEFAULT_ICON;
		}

	}
	
	public void build(Builder builder){
		this.builder = builder;
	}
	
	public void paintComponent(Graphics g) {

		g.setColor(color);
		for (int y = 0; y < currentIcon.length; ++y) {
			for (int x = 0; x < currentIcon[y].length; ++x) {
				BaseIcon icon = currentIcon[y][x];
				if(icon!=null){
					g.drawImage(icon.getIconImage(), x*(GRID_SIZE), y*(GRID_SIZE), null);
				}
				g.drawRect(x * (GRID_SIZE), y * (GRID_SIZE), GRID_W, GRID_H);
			}
		}
		
		if(builder!=null){
			 
			g.drawImage(progress, 0, 1*(GRID_SIZE), null);
			g.setColor(Color.black);
			g.fillRect(0 + 48 + Math.round(71 * builder.getComplete()), 1*(GRID_SIZE) + 28, 71 - Math
					.round(71 * builder.getComplete()), 5);
		}
		
	}
	
	private class MouseListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			BaseIcon icon = currentIcon[y / GRID_SIZE ][x / GRID_SIZE];
			if(icon!=null){
				icon.onClicked(gridMapRender);
			}
		}
	}
}
