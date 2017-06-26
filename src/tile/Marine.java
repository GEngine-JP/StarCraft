package tile;

import java.awt.Point;
import java.util.LinkedList;

import tile.Sprite.Animation;
import util.Resource;
import util.path.AStarSearch;
import core.GridMapRender;

/**
 * 小兵
 * @author jiangyp
 *
 */
public class Marine extends Sprite {

	private static final Resource RESOURCE = new Resource(50, 1);

	public Marine(Animation[][] animations, int id) {
		super(animations, id);
		 
	}

	@Override
	public Resource getResource() {
		return RESOURCE;
	}
	
	public LinkedList<Point>  move(float dx, float dy) {
		 
		//如果在攻击
		if(isReadFight()||isFighting()){
			stop();
			return super.move(dx, dy);
		}	
		return super.move(dx, dy);
	}
	
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		ainmStatus=0;
		if(isFighting()){
			//打开动态效果
			currentAnim.update(elapsedTime);
			target.setHealth(target.getHealth()-0.001f+target.getDefence());
			if(target.getHealth()<=0){
				currentAnim.currentIndex=1;
				stop();
				gm.splitTile(target);
				gm.addRemoveTile(target);
			}
			
		}
	}
	
	 
	
	@Override
	protected void adjustFight(){
		//先改变动画状态
		ainmStatus=1;
		super.adjustFight();
		
		int targetX = this.target.getTileX();
		int targetY = this.target.getTileY();
		
		int width = Math.round(GridMapRender.TILE_WIDTH*target.getSize().x /2.0f); ;
		int height = Math.round(GridMapRender.TILE_HEIGHT*target.getSize().y /2.0f);
		super.gm.addExplosions(GridMapRender.tileXToPx(targetX)+width,GridMapRender.tileYToPx(targetY)+height);
	}


	@Override
	public float getSpeed() {
		return 0.02f;
		//return 0.3f;
	}
	
	
	
}
