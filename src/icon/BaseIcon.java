package icon;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import tile.Tile;
import util.Resource;
import core.GridMapRender;

public abstract class BaseIcon {
	
	protected Image iconImage;
	
	protected List<IconBean> tileImages = new ArrayList<IconBean>();
	
	protected Resource resource;
	
	 
	public BaseIcon(Image iconImage) {
		super();
		this.iconImage = iconImage;
	}

	public abstract void onClicked(GridMapRender gridMapRender);
	
	public void add(Tile house,Image tileImage,Resource resource){
		this.tileImages.add(new IconBean(house,tileImage));
		this.resource=resource;
	}
	
	public Image getTileImage(int type){
		return tileImages.get(type).image;
	}
	 

	/**
	 * 返回一个原始Tile
	 * @param type
	 * @return
	 */
	public Tile getTile(int type){
		return tileImages.get(type).tile;
	}
	
	public Resource getResource() {
		return resource;
	}

	public Image getIconImage() {
		return iconImage;
	}
	
	public float getBuildSpeed(){return 0.0f;}
	
	public static class IconBean{
		
		public Image image;
		public Tile tile;
		
		public IconBean(Tile tile,Image image) {
			super();
			this.image = image;
			this.tile = tile;
		}
		
	}
}
