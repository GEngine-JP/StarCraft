package core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.KeyStroke;
/**
 * 用户交互控制
 * @author Administrator
 */
public class Control {
	
	private int x, y, dx, dy, width, height;

	private boolean dragged, moveing;

	private Component component;

	private ControlDragListener dragLister = new ControlDragListener();

	private ControlMoveListener moveLister = new ControlMoveListener();
	
	private ControlKeyListener keyLister = new ControlKeyListener();

	private DragListener dragListener;
	
	private MoveListener moveListener;
	
	private LeftPressListener leftPressListener;
	
	private RightPressListener rightPressListener;
	
	private KeyPressListener keyPressListener;
	
	
	public static final int LEFT_MOUSE = 1;
	
	public static final int RIGHT_MOUSE = 3;
	  
	public Control(Component mouseComponent,Component keyComponent) {
		
		this.component = mouseComponent;
		component.addMouseListener(dragLister);
		component.addMouseMotionListener(moveLister);
		keyComponent.addKeyListener(keyLister);
	}

	
	public void drag(Graphics g) {

		if (dragged && moveing) {
			g.setColor(Color.red);
			width = Math.abs(dx - x);
			height = Math.abs(dy - y);
			g.drawRect(Math.min(x, dx), Math.min(y, dy), width, height);
		}
	}
	/**
	 * 新增鼠标拖放回调
	 * @param dragCallBack
	 */
	public Control addDragListener(DragListener dragCallBack){
		this.dragListener = dragCallBack;
		return this;
	}
	/**
	 * 新增鼠标移动回调
	 * @param moveListener
	 * @return
	 */
	public Control addMoveListener(MoveListener moveListener){
		this.moveListener = moveListener;
		return this;
	}
	
	public Control addLeftPressListener(LeftPressListener selectCallBack){
		this.leftPressListener = selectCallBack;
		return this;
	}
	
	public Control addRightPressListener(RightPressListener selectCallBack){
		this.rightPressListener = selectCallBack;
		return this;
	}
	
	public Control addKeyPressListener(KeyPressListener keyPressListener){
		this.keyPressListener = keyPressListener;
		return this;
	}
	
	 
	/**
	 * 内部类，处理鼠标点击
	 */
	private class ControlDragListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			
			//如果是左键
			if(e.getButton()==LEFT_MOUSE){
				x = e.getX();
				y = e.getY();
				dragged = true;
				
				if(leftPressListener!=null){
					leftPressListener.press(x, y);
				}
			//如果是右键	
			} else if(e.getButton()==RIGHT_MOUSE){
				
				if(rightPressListener!=null){
//					System.out.println(e.getX()+","+e.getY());
					rightPressListener.press(e.getX(), e.getY());
				}
			}
		}
		

		public void mouseReleased(MouseEvent e) {
			
			//如果需要有回调
			if(dragListener!=null&&dragged&&moveing){
				 int tx = Math.min(x, dx);
				 int ty = Math.min(y, dy);
				 int tdx = Math.max(x, dx);
				 int tdy = Math.max(y, dy);
				dragListener.drag(tx, ty, tdx, tdy);
			}
			x = 0;
			y = 0;
			dx = 0;
			dy = 0;
			dragged = false;
			moveing = false;
		}
	}
	/**
	 * 内部类，处理鼠标移动
	 */
	private class ControlMoveListener extends MouseMotionAdapter {

		public void mouseDragged(MouseEvent e) {
		
			if (dragged) {
				dx = Math.min(e.getX(), component.getWidth());
				dy = Math.min(e.getY(), component.getHeight());
				moveing = true;
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			
			if(moveListener!=null){
				moveListener.move(e.getX(), e.getY());
			}
				
		}
	}
	
	private class ControlKeyListener implements KeyListener{
		
		public void keyPressed(KeyEvent e) {
			
			
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				 System.exit(-1);
			}
			
			if(keyPressListener!=null){
				keyPressListener.press(e.getKeyCode());
			}
			
		}

		public void keyReleased(KeyEvent e) {
			//System.out.println(e.getKeyCode());
			
		}

		public void keyTyped(KeyEvent e) {
			  
			  KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK);
             

		}
	}
	
	/**
	 * 内部接口，鼠标拖动回调
	 */ 
	public static interface DragListener{
		
		public void drag(int x,int y,int dx,int dy);
	}
	/**
	 * 内部接口,鼠标移动回调
	 */
	public static interface MoveListener{
		
		public void move(int x,int y);
	}
	/**
	 * 
	 *内部接口，左键回调
	 */ 
	public static interface LeftPressListener{
		
		public void press(int x,int y);
	}
	/**
	 *  内部接口，右键回调
	 */
	public static interface RightPressListener{
		
		public void press(int x,int y);
	}
	
	/**
	 * 
	 * @author jiangyp
	 */
	public static interface KeyPressListener{
		
		public void press(int keyCode);
	}
	
}
