package core;

import gui.ConsolePanel;
import icon.BaseIcon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.NetWorkManager;
import net.datagram.SpriteInfo;
import particles.PSExplosion;
import particles.Vector;
import tile.Headquarter;
import tile.Sprite;
import tile.Tile;
import util.Resource;
import core.ResourceManager.Constant;

public class GridMapRender {

	public static int TILE_WIDTH = 42; 

	public static int TILE_HEIGHT = 36; 
	
	public static int BG_HEIGHT = 32; 
	
	public static int BG_WIDTH = 64; 
	
	public int screenWidth,screenHeight;
	
	//屏幕滚动的差距(屏幕坐标)
	public int offsetX,offsetY;
	
	//鼠标移动的坐标
	public int moveX,moveY;
	
	int type = 0;
	
	GridMap gridMap;
	
	LinkedList<Tile> buildlist = new LinkedList();
	
	LinkedList<Tile> removelist = new LinkedList();
	
	LinkedList<PSExplosion> explosions = new LinkedList<PSExplosion>();
	
	ConsolePanel consolePanel;
	
	int state = 0;
	
	BaseIcon icon;
	
	FocusManager fm = new FocusManager(this);
	//矿产
	public int mine=50;
	//人口
	public int man=4;
	//房子
	public int manSpace=10;
	//信息
	String msg;
	int msgCount = 0;
	List<String> msglist = Collections.synchronizedList(new LinkedList<String>());
	
	//particles for goomba guts
	int p[][] = null; 
	final Random rand = new Random();
	
	private NetWorkManager netWorkManager;
	
	public GridMapRender(GridMap map) {
		this.gridMap = map;
		//init guts
		p = new int[100][5];
		for (int j = 0;j < p.length;j++) {
			p[j][4] = 0;
		}
		
		//addExplosions(200, 200);
		
	}
	
	/**
	 * 分拣Tile
	 * @param tile
	 */
	public void splitTile(Tile tile){
		
		for (int n = 0;n < 20;n++) {
			int z = rand.nextInt(p.length-1);
			p[z][0] = tileXToPx(tile.getTileX())-offsetX+16;
			p[z][1] = tileYToPx(tile.getTileY())-offsetY+16;
			p[z][2] = -25+rand.nextInt(50);
			p[z][3] = -10+rand.nextInt(20);
			p[z][4] = 25+rand.nextInt(25);
		}
	}
	
	public void draw(Graphics2D g) {
		//背景
		for (int y = 0; y < this.screenHeight/BG_HEIGHT+1; ++y) {
			for (int x = 0; x < this.screenWidth/BG_WIDTH+1; ++x) {
				g.drawImage(Constant.IMAGE_BG, x * BG_WIDTH, y * BG_HEIGHT, null);
			}
		}
		//Grid 只画一个屏幕的tile对于在scv做building操作的时候有BUG
		for(Tile s:gridMap.getTiles()){
			s.draw(g, offsetX, offsetY);
		}
		//info
		g.setColor(Constant.GREEN);
		g.drawImage(Constant.ICON_MINE, 14, 14, null);
		g.drawString(String.valueOf(mine), 14 + 18, 14 + 10);
		g.drawImage(Constant.ICON_MAN, 16 + 50, 14, null);
		g.drawString(man+"/"+manSpace, 16 + 50 + 18, 14 + 10);
		
		if(msg!=null){
			g.drawString(msg, (screenWidth-100)/2, (screenHeight-100));
		}
		
		//fight
		for (int j = 0;j < p.length;j++) {
			if (p[j][4] > 0) {
				g.fillRect(p[j][0],p[j][1],5,5);
			}
		}
		
		for(PSExplosion explosion:explosions){
			explosion.draw(g,offsetX,offsetY);
		}
		
		
		
		
		//test
		
		/*for (int y = 0; y < this.screenHeight/GridMapRender.TILE_HEIGHT; ++y) {
			
			for(int x = 0;x<this.screenHeight/GridMapRender.TILE_WIDTH;++x){
				
				g.setColor(new Color(51,51,51));
				g.drawRect(GridMapRender.tileXToPx(x), GridMapRender.tileYToPx(y), GridMapRender.TILE_WIDTH, GridMapRender.TILE_HEIGHT);
				
				Grid grid = gridMap.getGrid(x, y);
				if(!grid.isEmpty())
				{	
					g.setColor(Color.white);
					g.drawString("("+x+","+y+")", GridMapRender.tileXToPx(x), GridMapRender.tileYToPx(y));
				 }
				else
				{
					g.setColor(Color.GRAY);
					 g.drawString("("+x+","+y+")", GridMapRender.tileXToPx(x), GridMapRender.tileYToPx(y));
				 }
			}
		}*/
		 
	}
	public void update(long elapsedTime) {
		 
		for (Tile s : gridMap.getTiles()) {
			s.update(elapsedTime);
		}
		
		while(!buildlist.isEmpty()){
			Tile tile = buildlist.removeFirst();
			gridMap.add(tile);
		}
		
		while(!removelist.isEmpty()){
			Tile tile = removelist.removeFirst();
			gridMap.remove(tile);
		}
		//msg
		if(msgCount<=0){
			synchronized (msglist) {
				msg= !msglist.isEmpty()?msglist.remove(0):null;
				msgCount = msg!=null? 150:0; 
			}
			
		}else{
			--msgCount;
		}
		//split tile
		for (int j = 0;j < p.length;j++) {
			if (p[j][4] > 0) {
				p[j][0]+=p[j][2];
				p[j][1]+=p[j][3];
				p[j][4]--;
			}
		}
		
		for(int i=0;i<explosions.size();++i){
			PSExplosion explosion = explosions.get(i);
			if(!explosion.update(elapsedTime)){
				
				explosions.remove(explosion);
			}
		}
	}
	
	public void addBuildTile(Tile tile){
		buildlist.add(tile);
	}
	
	public void addRemoveTile(Tile tile){
		removelist.add(tile);
		netWorkManager.remove(new SpriteInfo(tile.getUUID()));
	}
	/**
	 * 网络回调,onRemoveTile触发
	 * @param tile
	 */
	public void addRemoveTileNetCallback(Tile tile){
		removelist.add(tile);
	}
	
	
	public void move(int tx, int ty) {
		fm.move(tx+offsetX, ty+offsetY);
	}
	
	public void operate(int x,int y){
		fm.operate(x+offsetX, y+offsetY); 
		 
	}
	
	public void focus(int fx, int fy, int tx, int ty) {

		fm.focus(fx + offsetX, fy + offsetY,tx + offsetX, ty + offsetY);
		
	}
	
	/**
	 * 选中一个地图上的Tile，可能是House,也可能是Sprite
	 * @param x
	 * @param y
	 */
	public void focus(int x, int y) {
		
		fm.focus(x + offsetX, y + offsetY);
		
	}
	
	public void addMsg(String msg){
		msglist.add(msg);
	}
	 
	public boolean checkResource(Resource resource){
		
		//检查资源
		if(mine<resource.getMine()){
			addMsg(Constant.MINE_ERROR);
			return false;
		}else{
			//检查人口
			if(manSpace<(resource.getMan()+man)){
				addMsg(Constant.MAN_ERROR);
				return false;
			}else{
				this.man+=resource.getMan();
			}
			this.mine-=resource.getMine();
		}
		
		
		
			
		return true;
	}
	
	/**
	 * 助手方法
	 */
	public static Point tileToPx(int mapx, int mapy) {

		int screenX = TILE_WIDTH * mapx;
		int screenY = TILE_HEIGHT * mapy;
		return new Point(screenX, screenY);
	}

	public static int tileXToPx(int x){
		return TILE_WIDTH * x;
	}
	
	public static int tileYToPx(int y){
		return TILE_HEIGHT * y;
	}
	
	public static int pxToTileX(float x){
		return  Math.round(x) / TILE_WIDTH;
	}
	
	public static int pxToTileY(float y){
		return Math.round(y) / TILE_HEIGHT;
	}
	public static Point pxTolTile(float x, float y) {

		int mapx = Math.round(x) / TILE_WIDTH;
		int mapy = Math.round(y) / TILE_HEIGHT;
		return new Point(mapx, mapy);
	}
	

	public GridMap getGridMap() {
		return gridMap;
	}

	public void setConsolePanel(ConsolePanel consolePanel) {
		this.consolePanel = consolePanel;
		int x=GridMapRender.pxToTileX(offsetX);
		int y=GridMapRender.pxToTileY(offsetY);
		this.getConsolePanel().map_panel.setConsoleRectLocation(x, y);
		  
	}
	
	public int getCurrentType(){
		return type;
	}
	
	/**
	 * 获取当前选中的Tile
	 * @return
	 */
	public Tile getCurrentTile(){
		return fm.getCurrentTile();
	}

	public ConsolePanel getConsolePanel() {
		return consolePanel;
	}
	
	public Headquarter getHeadquarter(int type){
		
		for(Tile tile:gridMap.getTiles()){
			if(tile instanceof Headquarter&&((Headquarter)tile).getType()==type){
				return (Headquarter) tile;
			}
		}
		return null;
	}

	public void setNetWorkManager(NetWorkManager client) {
		this.netWorkManager = client;
		fm.netWorkManager = this.netWorkManager;
	}

	public FocusManager getFm() {
		return fm;
	}

	public void setOffset(int offsetX,int offsetY) {
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public NetWorkManager getNetWorkManager() {
		return netWorkManager;
	}
	
	public void addFocusSprite(Sprite sprite){
		fm.addFocusSprite(sprite);
	}
	
	public void blur() {
		fm.blur();
	}

	public void addExplosions(float x,float y){
		this.explosions.add(new PSExplosion(x,y));
	}
}

