package gui;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.MockNetWorkManager;
import net.NetWorkManager;

import util.Elastic;
import core.ResourceManager;
import core.ResourceManager.Constant;

@SuppressWarnings("serial")
public class SingleGamePanel extends Abstractpanel{
	
	LeftPanel leftPanel;
	RightPanel rightPanel;
	NetWorkManager netWorkManager;
	
	public SingleGamePanel(GameGUI parent, String name) {
		super(parent, name);
		leftPanel= new LeftPanel(this);
		rightPanel= new RightPanel(this);
		this.add(leftPanel);
		this.add(rightPanel);
		netWorkManager = new MockNetWorkManager();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(main, 0, 0, null);
	}
	
	public void update(long elapsedTime){
		leftPanel.update(elapsedTime);
		rightPanel.update(elapsedTime);
	}
	
	public void reset(){
		leftPanel.elastic.slide(-50, 50);
		rightPanel.elastic.slide(800,-180);
		gameGUI.netWorkManager = netWorkManager;
		System.out.println("SingleGamePanel.reset() "+gameGUI.netWorkManager);
	}
	
	
	private class LeftPanel extends JPanel{
		private int x,y=20;
		Elastic elastic = new Elastic(-50,50);
		Image image = ResourceManager.loadImage("left.png");
		private JButton newbtn = new JButton("New Game");
		private JButton loadbtn = new JButton("Load Game");
		
		public LeftPanel(JPanel parent) {
			setLayout(null);
			setSize(230, 400);
			setLocation(x,y);
			initButton(newbtn, newbtnLis);
			newbtn.setLocation(40, 130);
			newbtn.setSize(150, 28);
			add(newbtn);
			
			initButton(loadbtn, loadbtnLis);
			loadbtn.setLocation(40, 200);
			loadbtn.setSize(150, 28);
			add(loadbtn);
		}
	
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
			g.setColor(fillColor);
			g.fillRect(20, 45, getWidth()-25, getHeight()-50);
			g.setColor(borderColor);
			g.drawRect(20, 45, getWidth()-25, getHeight()-50);
		}
	
		public void update(long elapsedTime){
			x=elastic.update(elapsedTime);
			setLocation(x, y);
		}
		
		/**
		 *New Game listener 
		 */
		ActionListener newbtnLis = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				GameGUI gamePanel = (GameGUI) SingleGamePanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600,200);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				gamePanel.switchNewGame();
			}
		};
		ActionListener loadbtnLis = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				GameGUI gamePanel = (GameGUI) SingleGamePanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600,200);
				try {
					Thread.sleep(250);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				gamePanel.switchSingleGame();
			}
		};
	}
	
	/**
	 *右边Panel
	 */
	private class RightPanel extends JPanel{
		
		private int x,y;
		
		private Elastic elastic = new Elastic(800,-180);
		
		private Image image=ResourceManager.loadImage("right.png");
		
		private JButton returnbtn = new JButton("Return");
		
		public RightPanel(JPanel parent) {
			 
			setLayout(null);
			setSize(180, 80);
			x = (parent.getWidth() - getWidth());
			y = 460;
			setLocation(x,y);
			initButton(returnbtn, listenner);
			returnbtn.setLocation(0, 45);
			returnbtn.setSize(150, 28);
			add(returnbtn);
		}
		
		
		public void paintComponent(Graphics g) {
			 
			g.drawImage(image, getWidth()-image.getWidth(null), 0, null);
		}
		
		public void update(long elapsedTime){
			x=elastic.update(elapsedTime);
			setLocation(x, y);
		}
		
		ActionListener listenner = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GameGUI gamePanel = (GameGUI) SingleGamePanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600,200);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				gamePanel.switchMainMenu();
			}
		};
	}
}
