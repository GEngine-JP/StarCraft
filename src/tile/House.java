package tile;

import gui.WorkPanel;
import icon.BaseIcon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Constructor;

import net.datagram.IconInfo;

import util.Resource;
import util.path.AStarSearch;
import core.GridMap;
import core.GridMapRender;
import core.ResourceManager.Constant;

public abstract class House extends AbstractTile implements Builder {

	protected Image currentImage;
	
	protected Image[] images;
	
	protected int status = 0;
	
	protected BaseIcon icon;
	
	protected float complete;
	
	public House(Image[] images,int id) {
		super(id);
		this.images = images;
	}

	public void draw(Graphics2D g,int offsetX,int offsetY) {

		int x = Math.round(this.x-offsetX);
		int y = Math.round(this.y-offsetY);
		if(isSelected()){
		 
			if(getType()!=gridMap.getTileMapRender().getCurrentType()){
				g.setColor(Color.RED);
				g.drawArc(x-Math.round(getWidth()*0.27f), y , Math.round(getWidth()*1.5f), getHeight() , 0, 360);
			}else{
				g.setColor(Constant.GREEN);
				g.drawArc(x-Math.round(getWidth()*0.27f), y , Math.round(getWidth()*1.5f), getHeight() , 0, 360);
			}
			
			g.fillRect(x, y + getHeight()+5,  Math.round(getWidth()*(health)), 3);
			g.setColor(Color.black);
			g.drawRect(x, y + getHeight()+5, getWidth(), 3);
		}
		
		g.drawImage(currentImage, x, y, null);
	}
	
	protected void buiding(long elapsedTime){
		//如果在building
		if(status==1){
			complete+= elapsedTime*icon.getBuildSpeed();
			//当build完毕
			if(complete>=1){
				Tile tile = icon.getTile(getType());
				GridMapRender gm = gridMap.getTileMapRender();
				Point location = AStarSearch.findNeighborNode(gridMap, x, y);
				tile = tile.clone(location.x, location.y, gridMap);
				tile.setUUID(java.util.UUID.randomUUID().toString());
				gm.addBuildTile(tile);
				complete=0;
				status=0;
				tile.setHealth(1.0f);
				//最后通知work_panel已经build完毕
				gm.getConsolePanel().work_panel.build(null);
				//通知网络build 需要ICON,TYP
				gm.getNetWorkManager().build(new IconInfo(getUUID(),tile.getUUID(),gridMap.getIconKey(icon)));
			}
		}
	} 
	
	public void update(long elapsedTime) {
		if(health<0.6){
			this.currentImage = this.images[2]; 
		}
		else if(health<0.98){
			this.currentImage = this.images[1]; 
		}else{
			this.currentImage = this.images[0];
		}
		buiding(elapsedTime);
	}
	
 
	
	public Tile clone(int x,int y,GridMap gridMap) {
		Constructor constructor = getClass().getConstructors()[0];
		try {
			House house = (House) constructor.newInstance(new Object[]{images,id});
			house.x = x* GridMapRender.TILE_WIDTH;
			house.y = y* GridMapRender.TILE_HEIGHT;
			house.tileX = x;
			house.tileY = y;
			house.gridMap = gridMap;
			house.gm = gridMap.getTileMapRender();
			return house;
		} catch (Exception e) {
			e.printStackTrace();
	 
		}
		return null;
	}

	public int getHeight() {
		return currentImage.getHeight(null);
	}

	public int getWidth() {
		return currentImage.getWidth(null);
	}

	
	public void readyBuild(BaseIcon icon){
		WorkPanel workPanel = gridMap.getTileMapRender().getConsolePanel().work_panel;
		workPanel.build(this);
		this.status = 1;
		this.icon = icon;
	}
	
	public boolean isBuilding(){
		return status==1;
	}
	
	public float getComplete() {
		return complete;
	}
	
	
	public abstract boolean build(long elapsedTime);
	
	public abstract Point getSize();
	
	public abstract Resource getResource();
	
	/**
	 * 防御
	 */
	public  abstract float getDefence();
}
