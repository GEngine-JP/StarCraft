package tile;

import icon.BaseIcon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

import net.datagram.IconInfo;

import util.Resource;
import util.path.AStarSearch;
import core.GridMapRender;
import core.ResourceManager.Constant;


public class Scv extends Sprite {

	private static final Resource RESOURCE = new Resource(50,1);
	
	public Scv(Animation[][] animations, int id) {
		super(animations, id);
	}
	
	public void build(int tx,int ty){
		
		this.status = 3;
		this.buildX = tx;//gm.moveX+gm.offsetX;
		this.buildY = ty;//gm.moveY+gm.offsetY;
		path = AStarSearch.findBuildPath(gridMap, x,y,buildX,buildY,icon.getTile(getType()).getSize());
		
	}
	
 
	public void onArrive(){
		super.onArrive();
		
		//如果开始building
		if(isStartBuild()){
			Tile tile = icon.getTile(getType()).clone(GridMapRender.pxToTileX(buildX), GridMapRender.pxToTileY(buildY), gridMap);
			tile.setUUID(this.newTileuuid);
			this.gridMap.getTileMapRender().addBuildTile(tile);
			this.buildTile = tile;
			this.status=4;
			this.blur();
			//调整sprite站立的位置，需要面朝building
			if(tileX<GridMapRender.pxToTileX(buildX)){
				currentAnim = animations[0][EAST];
			}else if(tileX>GridMapRender.pxToTileX(buildX)){
				currentAnim = animations[0][WEST];
			}
			if(tileY<GridMapRender.pxToTileY(buildY)){
				currentAnim = animations[0][SOUTH];
			}else if(tileY>GridMapRender.pxToTileY(buildY)){
				currentAnim = animations[0][NOTH];
			}
			gm.getConsolePanel().work_panel.dispatch(this);
		}
		//如果准备采矿
		if(isReadMining()){
			mining();
			
			
		}
		if(isBackHQing()){
			//this.gridMap.getTileMapRender().mine+=8;
			if(getType()==gm.getCurrentType()){
				this.gridMap.getTileMapRender().mine+=8;
			}
			readMining(target,this.targetx,this.targety);
		}
	}
	
	
	public void draw(Graphics2D g,int offsetX,int offsetY) {
		super.draw(g, offsetX, offsetY);
		
		
		if(isFighting()){
			
			int x = Math.round(this.x-offsetX);
			int y = Math.round(this.y-offsetY);
			//调整光照效果的位置
			int targetX = target.getTileX();
			int targetY = target.getTileY();
			if(targetX==tileX || targetY==tileY){
				if(tileX<targetX){
					g.drawImage(Constant.SCV_SPARK, x+18, y,null);
				}else if(tileX>targetX){
					g.drawImage(Constant.SCV_SPARK, x-18, y,null);
				}
				if(tileY<targetY){
					g.drawImage(Constant.SCV_SPARK, x, y+18,null);
				}else if(tileY>targetY){
					g.drawImage(Constant.SCV_SPARK, x, y-18,null);
				}
			}else{
				if(tileX<targetX){
					if(tileY<targetY){
						g.drawImage(Constant.SCV_SPARK, x+15, y+18,null);	
					}else{
						g.drawImage(Constant.SCV_SPARK, x+15, y-18,null);
					}
					
				}else{
					if(tileY<targetY){
						g.drawImage(Constant.SCV_SPARK, x-15, y+18,null);	
					}else{
						g.drawImage(Constant.SCV_SPARK, x-15, y-18,null);
					}
				}
			}
		}
		
		int type = this.getType();
		if(isReadyBuild()&&type==gm.getCurrentType()){
			Point location = GridMapRender.pxTolTile(gm.moveX+gm.offsetX, gm.moveY+gm.offsetY);
			//int type = gm.getCurrentTile().getType();
			Point size =  icon.getTile(type).getSize();
			
			for(int y=location.y;y<location.y+size.y;++y){
				for(int x = location.x;x<location.x+size.x;++x){
					//check if is fit
					if(gm.getGridMap().contains(x, y)){
						g.setColor(Color.red);
						Point position = GridMapRender.tileToPx(x, y);
						g.fillRect(position.x-gm.offsetX, position.y-gm.offsetY, GridMapRender.TILE_WIDTH, GridMapRender.TILE_HEIGHT);
					}else{
						g.setColor(Color.green);
						Point position = GridMapRender.tileToPx(x, y);
						g.fillRect(position.x-gm.offsetX, position.y-gm.offsetY,GridMapRender.TILE_WIDTH,GridMapRender.TILE_HEIGHT);
					}
					
				}
			}
			
			Point position = GridMapRender.tileToPx(location.x, location.y);
			g.drawImage(icon.getTileImage(type), position.x-gm.offsetX, position.y-gm.offsetY, null);
		}
		
		
		if(isBuilding()){
			
			int x = Math.round(this.x-offsetX);
			int y = Math.round(this.y-offsetY);
			 //调整光照效果的位置
			 if(tileX<GridMapRender.pxToTileX(buildX)){
				 g.drawImage(Constant.SCV_SPARK, x+30, y,null);
			 }else if(tileX>GridMapRender.pxToTileX(buildX)){
				 g.drawImage(Constant.SCV_SPARK, x-30, y,null);
			 }
			 if(tileY<GridMapRender.pxToTileY(buildY)){
				 g.drawImage(Constant.SCV_SPARK, x, y+15,null);
			 }else if(tileY>GridMapRender.pxToTileY(buildY)){
				 g.drawImage(Constant.SCV_SPARK, x, y-15,null);
			 }
		}
		//如果在mining
		if(isMining()){
			int x = Math.round(this.x-offsetX);
			int y = Math.round(this.y-offsetY);
			int targetX = target.getTileX();
			int targetY = target.getTileY();
			if(targetX==tileX || targetY==tileY){
				if(tileX<targetX){
					g.drawImage(Constant.SCV_SPARK, x+18, y,null);
				}else if(tileX>targetX){
					g.drawImage(Constant.SCV_SPARK, x-18, y,null);
				}
				if(tileY<targetY){
					g.drawImage(Constant.SCV_SPARK, x, y+18,null);
				}else if(tileY>targetY){
					g.drawImage(Constant.SCV_SPARK, x, y-18,null);
				}
			}else{
				if(tileX<targetX){
					if(tileY<targetY){
						g.drawImage(Constant.SCV_SPARK, x+15, y+18,null);	
					}else{
						g.drawImage(Constant.SCV_SPARK, x+15, y-18,null);
					}
					
				}else{
					if(tileY<targetY){
						g.drawImage(Constant.SCV_SPARK, x-15, y+18,null);	
					}else{
						g.drawImage(Constant.SCV_SPARK, x-15, y-18,null);
					}
				}
			}
		}
		
	}
	
	public  void update(long elapsedTime) {
		super.update(elapsedTime);
		//如果需要修房子
		if(isBuilding()){
			House house = (House)buildTile;
			if(house.build(elapsedTime)){
				this.status =0;
				//如果是房子的话就增加人口
				if(house instanceof Supply){
					this.gm.manSpace+=8;
				}
			}
		}	
		
		//如果需要采矿
		if(isMining()){
			Mine mine = (Mine)target;
			
			if(mine.mining(elapsedTime)){
				mine.reset();
				ainmStatus = 1;
				backHQ();
			}
		}
		
		if(isFighting()){
			target.setHealth(target.getHealth()-0.00095f+target.getDefence());
			if(target.getHealth()<=0){
				stop();
				gm.splitTile(target);
				gm.addRemoveTile(target);
			}
			
		}
		
	}
	
	/**
	 * 返回基地
	 */
	protected void backHQ(){
		this.status=9;
		Headquarter hq = gm.getHeadquarter(getType());
		if(hq!=null){
			int dx = GridMapRender.tileXToPx(hq.getTileX());
			int dy = GridMapRender.tileYToPx(hq.getTileY());
			super.move(dx, dy);
		}
	}
	
	 
	protected boolean isBackHQing(){
		return status==9;
	}
	
	/**
	 * 是否是在ready Building
	 * @return
	 */
	public boolean isReadyBuild(){
		return status == 2;
	}
	
	/**
	 * 是否开始buiding，表示到达指定目标开始buiding
	 * @return
	 */
	public boolean isStartBuild() {
		return this.status==3;
	}
	
	/**
	 * 是否正在buiding
	 * @return
	 */
	 public boolean isBuilding() {
		return this.status==4;
	}
	 
	
	float targetx,targety; 
	public  void readMining(Tile tile,float x,float y){
		
		if(isBuilding()){
			 //....什么也不做
		}else{
			this.target = tile;
			ainmStatus = 0;
			this.status=1;
			this.targetx = x;
			this.targety = y;
			super.move(x,y);
		}
	}
	
	public void fight(Tile target,int x,int y){
		if(isBuilding()){
			 //....什么也不做
		}else
			super.fight(target, x, y);
		
	}
	
	protected void mining(){
		status=7;
		int targetX = this.target.getTileX();
		int targetY = this.target.getTileY();
		if(targetX==tileX || targetY==tileY){
			if(tileX<targetX){
				currentAnim = animations[0][EAST];
			}else if(tileX>targetX){
				currentAnim = animations[0][WEST];
			}
			if(tileY<targetY){
				currentAnim = animations[0][SOUTH];
			}else if(tileY>targetY){
				currentAnim = animations[0][NOTH];
			}
			
		}else{
			if(tileX<targetX){
				currentAnim = tileY<targetY?animations[0][SOUTH_EAST]:animations[0][NOTH_EAST];
			}else{
				currentAnim = tileY<targetY?animations[0][SOUTH_WEST]:animations[0][NOTH_WEST];
			}
		}
	}
	
	protected boolean isReadMining(){
		return status==1;
	}
	
	protected boolean isMining(){
		return status==7;
	}
	
	String newTileuuid;
	public boolean  operate(int tx,int ty,String newTileuuid ){
		
		if(isReadyBuild()){
			
			this.newTileuuid = newTileuuid;
			//int type = gm.getCurrentTile().getType();
			int type = getType();
			Point location = GridMapRender.pxTolTile(tx, ty);
			Point size =  icon.getTile(type).getSize();
			
			for(int y=location.y;y<location.y+size.y;++y){
				for(int x = location.x;x<location.x+size.x;++x){
					//check if is fit
					if(gm.getGridMap().contains(x, y)){
						gm.addMsg(Constant.BUILD_ERROR);
						return true;
					} 
				}
			}
			build(tx,ty);
			return true;
		}
		
		return false;
	}
	
	public void readyBuild(BaseIcon icon){
		this.status = 2;
		this.icon = icon;
	}
	
	public LinkedList<Point>  move(float dx, float dy) {
		
		if(isReadyBuild()){
			stop();
			gm.mine+=icon.getResource().getMine();
			return null;
		}
		
		//如果正在buiding，不理它
		if(isBuilding()){
			//
			return null;
		}
		//如果在路途中准备开始buiding,停止
		if(isStartBuild()){
			stop();
			return super.move(dx, dy);
		}
		
		//如果正在采矿 
		if(isReadMining()||isMining()||isBackHQing()){
			stop();
			return super.move(dx, dy);
		}
		//如果在攻击
		if(isReadFight()||isFighting()){
			stop();
			return super.move(dx, dy);
		}	
		return super.move(dx, dy);
	}
	
	
	@Override
	public Resource getResource() {
		return RESOURCE;
	}

	@Override
	public float getSpeed() {
		return 0.016f;
		 
	}
}
