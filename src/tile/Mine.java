package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Constructor;

import core.GridMap;
import core.GridMapRender;

public class Mine extends AbstractTile{
	
	Image currentImage;
	int width;
	int height;
	
	public Mine(Image image,int id) {
		super(id);
		this.currentImage = image;
		width = currentImage.getWidth(null);
		height =currentImage.getHeight(null); 
	}

	public void draw(Graphics2D g,int offsetX,int offsetY) {
		
		int x = Math.round(this.x-offsetX);
		int y = Math.round(this.y-offsetY);
		
		if(isSelected()){
			g.setColor(Color.yellow);
			//g.drawArc(x-Math.round(width*0.27f), y , Math.round(width*1.5f), height , 0, 360);
			g.drawArc(x-Math.round(width*0.18f), y+10 , Math.round(width*1.3f), Math.round(height*0.7f) , 0, 360);
			 
		}
		g.drawImage(currentImage, x, y, null);
	}
	
	public Tile clone(int x, int y, GridMap gridMap) {
		Constructor constructor = getClass().getConstructors()[0];
		try {
			Mine mine = (Mine) constructor.newInstance(new Object[]{currentImage,id});
			mine.x = x* GridMapRender.TILE_WIDTH;
			mine.y = y* GridMapRender.TILE_HEIGHT;
			mine.tileX = x;
			mine.tileY = y;
			 
			return mine;
		} catch (Exception e) {
			e.printStackTrace();
	 
		}
		return null;
	}
	private static final float MINE_SPEED = 0.0008f;
	private float mine;
	public boolean mining(long elapsedTime) {
		mine = mine += elapsedTime*MINE_SPEED;
		return mine>=1;
	}
	
	public void reset(){
		mine=0;
	}
	
	private final static Point SIZE = new Point(1,1);
	public Point getSize(){
		return SIZE;
	}
	
	public int getType() {
		return -1;
	}

	public float getDefence() {
		// TODO Auto-generated method stub
		return 0;
	}

}
