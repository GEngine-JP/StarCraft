package net.datagram;

import java.util.List;

@SuppressWarnings("serial")
public class MoveInfo implements java.io.Serializable {

	List<SpriteInfo> sprites;

	int x;

	int y;
	
	SpriteInfo operator;
	
	String newTileUUID;

	public MoveInfo(List<SpriteInfo> sprites, int tx, int ty) {
		super();
		this.sprites = sprites;
		this.x = tx;
		this.y = ty;
	}
	
	public MoveInfo(SpriteInfo operator,int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.operator = operator;
	}
	
	public MoveInfo(SpriteInfo operator,int x, int y,String newTileUUID) {
		super();
		this.x = x;
		this.y = y;
		this.operator = operator;
		this.newTileUUID = newTileUUID;
	}

	public List<SpriteInfo> getSprites() {
		return sprites;
	}

	public void setSprites(List<SpriteInfo> sprites) {
		this.sprites = sprites;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public SpriteInfo getOperator() {
		return operator;
	}

	public void setOperator(SpriteInfo operator) {
		this.operator = operator;
	}

	public String getNewTileUUID() {
		return newTileUUID;
	}
	
	

	
}
