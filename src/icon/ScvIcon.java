package icon;

import java.awt.Image;

import net.datagram.IconInfo;
import tile.Scv;
import core.GridMapRender;

public class ScvIcon extends BaseIcon{

	public ScvIcon(Image iconImage) {
		super(iconImage);
	}
	
	public void onClicked(GridMapRender gridMapRender){
		 
		Scv tile = (Scv)gridMapRender.getCurrentTile();
		if(gridMapRender.checkResource(resource)){
			tile.readyBuild(this);
			gridMapRender.getNetWorkManager().readyBuild(new IconInfo(tile.getUUID(),gridMapRender.getGridMap().getIconKey(this)));
		}
	}
	
}
