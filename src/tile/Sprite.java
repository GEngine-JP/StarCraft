package tile;

import icon.BaseIcon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import particles.PSExplosion;

import util.Resource;
import util.path.AStarNode;
import util.path.AStarSearch;
import core.GridMap;
import core.GridMapRender;
import core.ResourceManager.Constant;

public abstract class Sprite extends AbstractTile {
	
	//位置(屏幕坐标)
	protected float x, y;
	//速度 
	protected float velocityX, velocityY;
	//当前动画
	protected Animation currentAnim;
	//路径
	protected LinkedList<Point> path;
	//方向
	protected Animation[][] animations;
	//状态(0普通，1采矿,2采气,3准备build,4buiding,5fight,9返回基地)
	protected int status = 0;
	//控制动画
	protected int ainmStatus=0;
	
	protected BaseIcon icon;
	
	protected Tile buildTile;
	
	int NOTH = 0;
	int NOTH_EAST =1;
	int EAST = 2;
	int SOUTH_EAST = 3;
	int SOUTH = 4;
	int SOUTH_WEST = 5;
	int WEST = 6;
	int NOTH_WEST = 7;
	
	//地图中的位置(地图坐标)
	public int tileX,tileY;
	
	//建筑物的坐标
	int buildX,buildY;
	
	public Sprite(Animation[][] animations,int id) {
		super(id);
		this.animations = animations;
		currentAnim = this.animations[ainmStatus][0];
	}
	
	public LinkedList<Point>  move(float dx, float dy) {
		//System.out.println("------------------------------------");
		//System.out.println("sou:"+GridMapRender.pxTolTile(x, y)+" , tar:"+GridMapRender.pxTolTile(dx, dy));
		path = AStarSearch.findPath(gridMap, tileX,tileY,dx,dy);
		//System.out.println("path:"+path);
		//System.out.println("------------------------------------");
		continueMove=false;
		return path;
	}
	
	boolean continueMove = false;
	float dx2,dy2;
	/**
	 * 二分move
	 * @param dx1 
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @return
	 */
	public LinkedList<Point>  move(float dx1, float dy1,float dx2,float dy2) {
		stop();
		path = AStarSearch.findPath(gridMap, x,y,dx1,dy1);
		this.dx2 = dx2;
		this.dy2 = dy2;
		continueMove=true;
		return path;
	}
	
	private float speed;
	public abstract float getSpeed(); 
	
	/**
	 * 取得下一次路径的坐标
	 * @return
	 */
	private  synchronized  Point getNextPath() {
		 
		
		Point location = path.getFirst();
//		System.out.println("next:"+location);
		Point position = GridMapRender.tileToPx(location.x, location.y);
		
		//转换成屏幕坐标
		float tx = position.x;
		float ty = position.y;
		
		if (tx != x) {
			velocityX = (tx > x ? getSpeed() : -getSpeed()) * 7;
		}
		if (ty != y) {
			velocityY = (ty > y ? getSpeed() : -getSpeed()) * 6;
		}
		
		if (velocityX == 0 && velocityY == 0 && !path.isEmpty() ) {
			path.removeFirst();
		
		} 
		//System.out.println("getNextPath cost:"+(System.nanoTime()-begin));
		return position;
	}
	
	/**
	 * 移动Map
	 * @param dx
	 * @param dy
	 * @param nx
	 * @param ny
	 */
	private void moveTile(float dx,float dy,float nx,float ny){
		
	
		
//		boolean checkX = (velocityX > 0 && dx > nx)||(velocityX < 0 &&dx<nx+getWidth());    
//		boolean checkY = (velocityY > 0 && dy > ny)||(velocityY < 0 &&dy<ny+getHeight());
//		
//		if(checkX || checkY){
//			int nextTileX = GridMapRender.pxToTileX(nx);
//			int nextTileY = GridMapRender.pxToTileY(ny);
//			gridMap.move(this,nextTileX,nextTileY);
//			this.tileX = nextTileX;
//			this.tileY = nextTileY;
//		}
		
		
	} 
	
	protected void adjustFight(){
		 
		int targetX = this.target.getTileX();
		int targetY = this.target.getTileY();
		
		if(targetX==tileX || targetY==tileY){
			
			if(tileX<targetX){
				currentAnim = animations[ainmStatus][EAST];
			}else if(tileX>targetX){
				currentAnim = animations[ainmStatus][WEST];
			}
			if(tileY<targetY){
				currentAnim = animations[ainmStatus][SOUTH];
			}else if(tileY>targetY){
				currentAnim = animations[ainmStatus][NOTH];
			}
			
		}else{
			
			if(tileX<targetX){
				currentAnim = tileY<targetY?animations[ainmStatus][SOUTH_EAST]:animations[ainmStatus][NOTH_EAST];
			}else{
				currentAnim = tileY<targetY?animations[ainmStatus][SOUTH_WEST]:animations[ainmStatus][NOTH_WEST];
			}
		}
	}
	
	protected void checkFight(){
		
//		如果在攻击中，speed减少10％
		//this.speed = this.speed-this.speed*0.2f;
		if(isReadFight() && AStarSearch.isNeighbors(tileX,tileY, target)){
			 
			status=6;
			adjustFight();
			

		}else if(isFighting()){
			
			if(AStarSearch.isNeighbors(tileX,tileY, target))
				adjustFight();
			else
				status=5;
			
		}
		
	}
 
	
	public void onArrive(){
	
//		//如果准备fight
//		if(this.status==5){
//			 
//			status=6;
//			int targetX = this.target.getTileX();
//			int targetY = this.target.getTileY();
//			if(targetX==tileX || targetY==tileY){
//				
//				if(tileX<targetX){
//					currentAnim = animations[0][EAST];
//				}else if(tileX>targetX){
//					currentAnim = animations[0][WEST];
//				}
//				if(tileY<targetY){
//					currentAnim = animations[0][SOUTH];
//				}else if(tileY>targetY){
//					currentAnim = animations[0][NOTH];
//				}
//				
//			}else{
//				
//				if(tileX<targetX){
//					currentAnim = tileY<targetY?animations[0][SOUTH_EAST]:animations[0][NOTH_EAST];
//				}else{
//					currentAnim = tileY<targetY?animations[0][SOUTH_WEST]:animations[0][NOTH_WEST];
//				}
//			}
//		}
	}
	 
	protected void notifyFightSource(){
		
		for(int i=0;i<fightSourceList.size();++i){
			LinkedList<AStarNode> fightNodes = AStarSearch.findFightPath(gridMap, this);
			AStarNode node = fightNodes.removeFirst();
			fightSourceList.get(i).fight(this, node.getX(), node.getY());
		}
	}
	
	public synchronized  void update(long elapsedTime) {
		 
		speed = getSpeed();
		x += velocityX * elapsedTime;
		y += velocityY * elapsedTime;
		int tempX = GridMapRender.pxToTileX(x+getWidth()/2);
		int tempY = GridMapRender.pxToTileY(y+getHeight()/2);
		if(tempX!=tileX){
			gridMap.getGrid(tileX, tileY).remove(this);
			gridMap.getGrid(tempX, tileY).add(this);
			tileX = tempX;
		}
		if(tempY!=tileY){
			gridMap.getGrid(tileX, tileY).remove(this);
			gridMap.getGrid(tileX, tempY).add(this);
			tileY = tempY;
		}
		checkFight();
		  
		
		
		// 寻路
		if (path != null) {
			
			currentAnim.update(elapsedTime);
		
			// 如果还没有走完
			if (!path.isEmpty()) {
				
				notifyFightSource();
				//取得下一步位置
				Point next = getNextPath();
				float nx = next.x;
				float ny = next.y;
				//实际行走位置
				float dx = x + velocityX * elapsedTime;
				float dy = y + velocityY * elapsedTime;
				
				//移动Map
				moveTile(dx,dy,nx,ny);
			 
				if (velocityX > 0 && dx >= nx) {
					velocityX = 0;
					x = nx;
				} else if (velocityX < 0 && dx <= nx) {
					velocityX = 0;
					x = nx;
				}

				if (velocityY > 0 && dy >= ny) {
					velocityY = 0;
					y = ny;
				} else if (velocityY < 0 && dy <= ny) {
					velocityY = 0;
					y = ny;
				}
				
			} else {
				
				if(continueMove){
					path = move(dx2, dy2);
					continueMove=false;
				}else{
					path=null;
					currentAnim.currentIndex=0;
					onArrive();
				}
				
			}
			
		} 
		 
		updateAnim();
		
		
	}
	
	/**
	 *  控制8个不同方向
	 */
	protected void updateAnim(){
		
		Animation newAnim = currentAnim;
		// 判断左右
		if (velocityX != 0 && velocityY == 0) {
			 
			newAnim = velocityX > 0 ? animations[ainmStatus][EAST]: animations[ainmStatus][WEST];
		}
		// 判断上下
		else if (velocityY != 0 && velocityX == 0) {

			newAnim = velocityY > 0 ? animations[ainmStatus][SOUTH]: animations[ainmStatus][NOTH];
		}
		// 判断四个斜角
		else if (velocityX != 0 && velocityY != 0) {
			 
			if (velocityX > 0) {
				
				newAnim = velocityY > 0 ? animations[ainmStatus][SOUTH_EAST]: animations[ainmStatus][NOTH_EAST];
				
			} else {
				
				newAnim = velocityY > 0 ? animations[ainmStatus][SOUTH_WEST]:animations[ainmStatus][NOTH_WEST];
				
			}
		}

		if (currentAnim != newAnim) {
			currentAnim = newAnim;
			currentAnim.start();
		}
	}

	public int getHeight() {
		return currentAnim.getImage().getHeight(null);
	}

	public int getWidth() {
		return currentAnim.getImage().getWidth(null);
	}

	public void draw(Graphics2D g,int offsetX,int offsetY) {

		int x = Math.round(this.x-offsetX);
		int y = Math.round(this.y-offsetY);
	 
		
		if(isSelected()){
			
			if(getType()!=gridMap.getTileMapRender().getCurrentType()){
				g.setColor(Constant.RED);
				g.drawArc(x+getWidth()/4, y + getHeight() / 2, getWidth()/2, getHeight() / 4, 0, 360);
			}else{
				g.setColor(Constant.GREEN);
				g.drawArc(x+getWidth()/4, y + getHeight() / 2, getWidth()/2, getHeight() / 4, 0, 360);
			}
			g.fillRect(x, y + getHeight()-5,  Math.round(getWidth()*(health)), 3);
			g.setColor(Color.black);
			g.drawRect(x, y + getHeight()-5, getWidth(), 3);
			
		}

		g.drawImage(currentAnim.getImage(), x, y, null);
		
	
		
	}
	
	public Tile clone(int x,int y,GridMap map) {

		Constructor constructor = getClass().getConstructors()[0];
		Animation[][] ans = new Animation[this.animations.length][this.animations[0].length];
		for(int k=0;k<this.animations.length;++k){
			
			for(int i = 0;i<this.animations[k].length;++i){
				ans[k][i] = (Animation) this.animations[k][i].clone();
			}
		}
		try {
			Sprite sprite = (Sprite) constructor.newInstance(new Object[]{ans,id});
			sprite.x = x* GridMapRender.TILE_WIDTH;
			sprite.y = y* GridMapRender.TILE_HEIGHT;
			sprite.tileX = x;
			sprite.tileY = y;
			sprite.gridMap = map;
			sprite.gm = sprite.gridMap.getTileMapRender();
			return sprite;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 动画
	 * 
	 * @author jiangyp
	 * 
	 */
	public static class Animation{
		
		List<Frame> frames = new ArrayList<Frame>();

		int currentIndex;

		long totalDuration;

		long animTime;
		
		
		public Animation() {
		}
		
		public Animation(Image image,int endTime) {
			this.addFrame(image, endTime);
		}
		
		
		public Animation(List<Frame> frames, long totalDuration) {
			super();
			this.frames = frames;
			this.totalDuration = totalDuration;
		}

		public void addFrame(Image image, int endTime) {

			totalDuration += endTime;
			frames.add(new Frame(image, totalDuration));
		}

		public synchronized void start() {
			animTime = 0;
			currentIndex = 0;
		}

		public void update(long costTime) {

			animTime += costTime;

			if (animTime >= totalDuration) {
				animTime = animTime % totalDuration;
				currentIndex = 0;
			}

			// System.out.println(costTime);
			if (animTime > frames.get(currentIndex).endTime) {
				++currentIndex;
			}

		}

		public Image getImage() {
			return frames.get(currentIndex).image;
		}

		/**
		 * 帧
		 * @author jiangyp
		 *
		 */
		public static class Frame {

			Image image;

			long endTime;

			public Frame(Image image, long endTime) {
				super();
				this.image = image;
				this.endTime = endTime;
			}

		}
		
		 public Object clone() {
		        return new Animation(frames, totalDuration);
		 }
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setGridMap(GridMap tileMap) {
		this.gridMap = tileMap;
	}
	 
	public int getId(){
		return id;
	}

	public float getVelocityX() {
		return velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}
	
	public void collideHorizontal(){
		velocityX = 0;
	}
	public void collideVertical() {
//		setVelocityY(0);
	}
	

	private final static Point SIZE = new Point(1,1);
	public Point getSize(){
		return SIZE;
	}
	//被攻击的目标
	Tile target;
	public void fight(Tile target,int x,int y){
		stop();
		this.target = target;
		move(x, y);
		status=5;
		if(target instanceof Sprite){
			((Sprite)target).addFightSource(this);
		}
	}
	/**
	 * 此方法用于被攻击源锁定，当sprite行动时候，需要通知目标 
	 * @param sprite攻击源
	 */
	public void addFightSource(Sprite fightSource){
		if(!fightSourceList.contains(fightSource))
			fightSourceList.add(fightSource);
	}
	
	protected List<Sprite> fightSourceList = new ArrayList<Sprite>();
	
	public boolean isFighting(){
		return status==6;
	}
	
	public boolean isReadFight(){
		return status==5;
	}

	public double getDistance(Point target) {
		float tx = target.x - x;
		float ty = target.y - y;
		return Math.sqrt(tx * tx + ty * ty);
	}
	
	public void stop(){
		this.status = 0;
	}
	
	public String toString(){
		return "["+x+","+y+"]";
	}
	
	public abstract Resource getResource();
	
	/**
	 * 防御
	 */
	public float getDefence() {
		return 0.00005f;
	}
}

/*
数据结果：
sou:java.awt.Point[x=1,y=3] , tar:java.awt.Point[x=1,y=4]
path:[java.awt.Point[x=1,y=4]]
------------------------------------
sou:java.awt.Point[x=1,y=3],tar:java.awt.Point[x=1,y=3]
path:[java.awt.Point[x=1,y=4]]
------------------------------------
原因：因为是多个sprite先使用了findSpace查找空节点，先发现[x=1,y=3]已经有sprite
所以第二个被分到了x=1,y=4
------------------------------------
*/
