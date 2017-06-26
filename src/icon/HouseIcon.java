package icon;

import java.awt.Image;

import tile.House;
import core.GridMapRender;

public class HouseIcon extends BaseIcon{

	public HouseIcon(Image iconImage) {
		super(iconImage);
	}

	public void onClicked(GridMapRender gridMapRender){
	
		House house = (House)gridMapRender.getCurrentTile();
		if(gridMapRender.checkResource(resource)){
			house.readyBuild(this);
		}
	 
		
	}
	
	public float getBuildSpeed(){
		return BUILD_SPEED;
	}
	
	private static final float BUILD_SPEED = 0.0003f;
}
