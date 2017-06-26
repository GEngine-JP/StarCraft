package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.NetWorkManager;
import tile.Mine;
import tile.Scv;
import tile.Sprite;
import tile.Tile;
import util.path.AStarNode;
import util.path.AStarSearch;

public class FocusManager {

	GridMapRender gm;

	Tile tile;
	
	NetWorkManager netWorkManager;
	
	private LinkedList<Sprite> sprites = new LinkedList<Sprite>();

	public FocusManager(GridMapRender gm) {
		super();
		this.gm = gm;
	}

	public void focus(int x, int y) {

		int tileX = GridMapRender.pxToTileX(x);
		int tileY = GridMapRender.pxToTileY(y);
		focus(gm.gridMap.getGrid(tileX, tileY));
	}

	public void focus(int fx, int fy, int tx, int ty) {

		Point fromTile = GridMapRender.pxTolTile(fx, fy);
		Point toTile = GridMapRender.pxTolTile(tx, ty);
		List<Tile> tiles = getTiles(fromTile, toTile, gm.getCurrentType());
		focus(tiles);

	}
	
	public void operate(int x,int y){
		 
		Tile tile = getCurrentTile();
		String newTileUUID = UUID.randomUUID().toString();
		if(tile==null||!tile.operate(x,y,newTileUUID)){
			focus(x, y); 
		}
		//netWorkManager
		netWorkManager.operate(tile, x, y,newTileUUID);
	}
	
	public void operate(Tile tile,int x,int y,String newTileUUID){
		if(tile!=null)
			tile.operate(x,y,newTileUUID);
	}

	public void move(LinkedList<Sprite> sprites, int tx, int ty) {

		if (sprites.size() > 0) {

			Point target = new Point(tx, ty);
			Tile tile = gm.gridMap.getTile(GridMapRender.pxToTileX(tx),GridMapRender.pxToTileY(ty));
//			System.out.println(sprites.getFirst());
//			double distance = sprites.getFirst().getDistance(target);
//			System.out.println("sprte.move:"+distance);
//			if (distance > 1400 && sprites.size() > 3) {
//				LinkedList<AStarNode> space = AStarSearch.findSpace(gm.gridMap,
//						target.x / 2, target.y / 2, sprites.size());
//				for (int i = 0; i < sprites.size(); ++i) {
//
//					AStarNode node = space.get(i);
//					Sprite sprite = sprites.get(i);
//					// 如果不是当前type则跳过
////					if (sprite.getType() != gm.getCurrentType())
////						continue;
//					sprite.move(node.getX(), node.getY(), target.x, target.y);
//
//				}
//
//			} else {

				if (tile != null) {
					LinkedList<AStarNode> fightNodes = AStarSearch.findFightPath(gm.gridMap, tile);

					for (int i = 0; i < sprites.size(); ++i) {

						Sprite sprite = sprites.get(i);
						if(sprite==null){
							continue;
						}
						
						// 如果不是当前type则跳过
//						if (sprite.getType() != gm.getCurrentType())
//							continue;

						// 如果是采矿
						if (tile instanceof Mine && sprite instanceof Scv) {
							Scv scv = (Scv) sprite;
							if (!fightNodes.isEmpty()) {
								AStarNode node = fightNodes.removeFirst();
								scv.readMining(tile, node.getX(), node.getY());
							} else {
								scv.move(tx, ty);
							}

						}
						// 如果是攻击 
						//else if (tile.getType() != gm.getCurrentType()) {
						else if (tile.getType() != sprite.getType()) {

							if (!fightNodes.isEmpty()) {
								AStarNode node = fightNodes.removeFirst();
								sprite.fight(tile, node.getX(), node.getY());
							} else {
								sprite.move(tx, ty);
							}
						}
					}
				} else if (tile == null) {
				
					LinkedList<AStarNode> space = AStarSearch.findSpace(gm.gridMap, tx, ty, sprites.size());
					for (int i = 0; i < sprites.size() && i < space.size(); ++i) {
						Sprite sprite = sprites.get(i);
						AStarNode node = space.get(i);
						if(sprite !=null)
							sprite.move(node.getX(), node.getY());
					}
				}
			
		}
	}
	
	public void move(int tx, int ty) {
	
		move(sprites, tx, ty);
		//System.out.println("orginMove:"+sprites.size()+","+tx+","+ty);
		netWorkManager.move(sprites, tx, ty);
	}
	

	
	private void focus(List<Tile> tiles) {
		
		if (tiles.size() == 1) {
			blur();
			tile = tiles.get(0);
			tile.focus();
			
			if(tile.getType()==gm.getCurrentType()){
				
				gm.getConsolePanel().work_panel.dispatch(tile);
				if (tile instanceof Sprite) 
					sprites.add((Sprite) tile);
				
			}
			
			
		} else if(tiles.size()>1){
			
			blur();
			for (Tile t : tiles) {
				if (t instanceof Sprite) {
					t.focus();
					sprites.add((Sprite) t);
				}
			}
		}
	}

	/**
	 * 从一个坐标范围里面获取Tile列表
	 * 
	 * @param fromTile
	 * @param toTile
	 * @param type
	 * @return
	 */
	private List<Tile> getTiles(Point fromTile, Point toTile, int type) {

		List<Tile> result = new ArrayList();
		for (int y = fromTile.y; y <= toTile.y; ++y) {
			for (int x = fromTile.x; x < toTile.x; ++x) {
				for (Tile tile : gm.gridMap.getGrid(x, y)) {
					if (tile.getType() == type)
						result.add(tile);
				}
			}
		}
		return result;
	}
	
	/**
	 * 取消选中地图上的Tile,包括单个Tile(此tile可以是house或sprite) 和已经选中的sprites
	 */
	public void blur() {
		gm.getConsolePanel().work_panel.dispatch(null);
		if (tile != null) {
			tile.blur();
			tile = null;
		}
		for (Tile t : sprites) {
			t.blur();
		}
		sprites.clear();

	}
	
	public Tile getCurrentTile(){
		return sprites.isEmpty()?tile:sprites.getFirst();
	}

	public LinkedList<Sprite> getSprites() {
		return sprites;
	}
	
	public void addFocusSprite(Sprite sprite){
		sprites.add(sprite);
	}
	
}
