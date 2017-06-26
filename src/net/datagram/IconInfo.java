package net.datagram;

public class IconInfo implements java.io.Serializable{
	
	String keyName;
	
	/**
	 * builder 的UUID
	 */
	String tileUUId;
	
	/**
	 * Builded 的UUID
	 */
	String newTileUUId;
	//1是build,0是readBuild
	int action;
 
	public IconInfo(String tileUUId, String newTileUUId,String keyName) {
		super();
		this.keyName = keyName;
		this.tileUUId = tileUUId;
		this.newTileUUId = newTileUUId;
	}
	
	public IconInfo(String tileUUId, String keyName) {
		super();
		this.keyName = keyName;
		this.tileUUId = tileUUId;
	}

	public String getTileUUId() {
		return tileUUId;
	}

	public void setTileUUId(String tileUUId) {
		this.tileUUId = tileUUId;
	}

	public String getName() {
		return keyName;
	}

	public void setName(String name) {
		this.keyName = name;
	}
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getNewTileUUId() {
		return newTileUUId;
	}

	public void setNewTileUUId(String newTileUUId) {
		this.newTileUUId = newTileUUId;
	}
	
	

	
	
}
