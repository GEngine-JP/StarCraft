package net.datagram;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SpriteInfo implements Serializable {

	// 唯一的ID
	String UUID;
	// 1表示删除.GridMap.remove调用
	int action;
	
	public SpriteInfo(String uuid) {
		super();
		UUID = uuid;
	}


	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uuid) {
		UUID = uuid;
	}

	public int getAction() {
		return action;
	}


	public void setAction(int action) {
		this.action = action;
	}

	
}
