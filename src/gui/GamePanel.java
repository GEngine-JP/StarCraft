package gui;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import tile.Marine;
import tile.Scv;
import tile.Sprite;
import tile.Tile;
import core.Control;
import core.GridMap;
import core.GridMapRender;
import core.ResourceManager;
import core.Control.DragListener;
import core.Control.KeyPressListener;
import core.Control.LeftPressListener;
import core.Control.MoveListener;
import core.Control.RightPressListener;

/**
 * 游戏面板
 * @author jiangyp
 *
 */
public class GamePanel extends Abstractpanel{
	
	
	GridMap gridmap;

	GridMapRender gridMapRender;
	
	Control control;

	ConsolePanel controlPanel;
	
	Robot robot; 
	 
	public void init(){
		
		initControl();
		gridmap = ResourceManager.resourceManager.getGridMap();
		gridMapRender = gridmap.getTileMapRender();
		gridMapRender.screenWidth = getWidth();
		gridMapRender.screenHeight = getHeight();
		controlPanel = new ConsolePanel(gridMapRender);
		controlPanel.setLocation(getWidth() - controlPanel.getWidth(), 0);
		add(controlPanel);
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		gridMapRender.addMsg("温馨提示：按数字键1选中所有农民");
		gridMapRender.addMsg("温馨提示：按数字键2选中所有机枪兵");
	}
	
	private void initControl(){
		
		GameGUI  gui = (GameGUI) this.getParent();
		control = new Control(this,	gui.frame);
		
		control.addKeyPressListener(new KeyPressListener(){

			public void press(int keyCode) {
				
				if(keyCode==KeyEvent.VK_1){
					gridMapRender.blur();
					for(Tile tile:gridmap.getTiles()){
						
						boolean isScv = (tile.getType()==gridMapRender.getCurrentType())
									&&(tile instanceof Scv);
						if(isScv){
							tile.focus();
							gridMapRender.addFocusSprite((Sprite)tile);
						}
						
					}
				}
				else if(keyCode==KeyEvent.VK_2){
					gridMapRender.blur();
					for(Tile tile:gridmap.getTiles()){
						
						boolean isMarine = tile.getType()==gridMapRender.getCurrentType()
									&&tile instanceof Marine;
						if(isMarine){
							tile.focus();
							gridMapRender.addFocusSprite((Sprite)tile);
						}
						
					}
				}
			}
			
		});
		control.addDragListener(new DragListener(){
			public void drag(int x, int y, int dx, int dy) {
				gridMapRender.focus(x, y, dx, dy);
			}
		});
		control.addLeftPressListener(new LeftPressListener(){
			public void press(int x, int y) {
				//gridMapRender.focus(x, y);
				gridMapRender.operate(x, y);
			}
			
		});
		control.addRightPressListener(new RightPressListener(){
			public void press(int x, int y) {
				gridMapRender.move(x, y);
			}
		});	
		
		control.addMoveListener(new MoveListener(){

			public void move(int x, int y) {
				 
				int offsetX = gridMapRender.offsetX;
				int screenX = getWidth()-GridMapRender.TILE_WIDTH;
				 
				//如果鼠标位于屏幕宽度前一个TILE宽度
				if(x>screenX&&offsetX<GridMapRender.tileXToPx(107)){
					//向右移动一个tile宽度
					gridMapRender.offsetX=offsetX+GridMapRender.TILE_WIDTH;
					robot.mouseMove(screenX, y);
					++controlPanel.map_panel.x;
				}
				//如果鼠标在一个TILE宽度
				if(x<GridMapRender.TILE_WIDTH&&offsetX>0){
					gridMapRender.offsetX=offsetX-GridMapRender.TILE_WIDTH;
					robot.mouseMove(GridMapRender.TILE_WIDTH, y);
					--controlPanel.map_panel.x;
				}
				
				//纵坐标和水平坐标逻辑相同
				int offsetY = gridMapRender.offsetY;
				int screenY = getHeight()-GridMapRender.TILE_HEIGHT;
				
				if(y>screenY&&offsetY<GridMapRender.tileXToPx(110)){
					//向右移动一个tile宽度
					gridMapRender.offsetY=offsetY+GridMapRender.TILE_HEIGHT;
					robot.mouseMove(x, screenY);
					++controlPanel.map_panel.y;
				}
				
				if(y<GridMapRender.TILE_HEIGHT&&offsetY>0){
					gridMapRender.offsetY=offsetY-GridMapRender.TILE_WIDTH;
					robot.mouseMove(x, GridMapRender.TILE_HEIGHT);
					--controlPanel.map_panel.y;
				}
				
				gridMapRender.moveX = x;
				gridMapRender.moveY = y;
				
			}
			
		});
		
		
	}

	public GamePanel(GameGUI parent, String name) {
		super(parent, name);
	}
	
	public void paintComponent(Graphics g) {
		
		gridMapRender.draw((Graphics2D)g);
		//画控制线
		control.drag(g);
		
	}
	
	public void update(long elapsedTime) {

		gridMapRender.update(elapsedTime);
		 
	}
}
