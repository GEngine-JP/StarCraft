package core;

import java.util.LinkedList;
import java.util.List;

import tile.Sprite;
import tile.Tile;

public class TileList {

	private LinkedList<Sprite> sprites = new LinkedList<Sprite>();

	private Tile tile;
	
	public int size(){
		return sprites.size();
	}
	
	public Sprite get(int i){
		return sprites.get(i);
	}
	
	
	public Tile add(List<Tile> tiles) {
		
		if(tiles.size()==1){
			
			Tile t = tiles.get(0);
			t.focus();
			tile = t;
			if(tile instanceof Sprite){
				sprites.add((Sprite) tile);
			} 
			return t;
			
		}else{
			
			for(Tile t:tiles){
				if(t instanceof Sprite){
					t.focus();
					sprites.add((Sprite)t);
				}
			}
			
			return null;
		}
		
	}
	
	public void blur() {
		
		if(tile!=null){
			tile.blur();
			tile=null;
		}
		for (Tile t : sprites) {
			t.blur();
		}
		sprites.clear();
		
	}

	
	public Tile getTile() {
		return tile;
	}

	public Sprite getSprite() {
		return (Sprite) getTile();
	}

	public int getType() {
		return getTile().getType();
	}
}
