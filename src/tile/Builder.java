package tile;

import icon.BaseIcon;

public interface Builder {

	public void readyBuild(BaseIcon icon);

	public boolean isBuilding();
	
	public float getComplete();
	
}
