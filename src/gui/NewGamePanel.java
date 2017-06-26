package gui;

import gui.ui.combobox.RoundBorder;
import gui.ui.combobox.StarComboBoxRender;
import gui.ui.combobox.StarComboBoxUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.ComboBoxUI;

import net.MockNetWorkManager;
import net.datagram.PlayerList;
import util.Elastic;
import core.ResourceManager;

public class NewGamePanel extends Abstractpanel {

	LeftPanel leftPanel;

	RightPanel rightPanel;
	 
	
	public NewGamePanel(GameGUI gameGUI, String name) {
		super(gameGUI, name);
		leftPanel = new LeftPanel(this);
		rightPanel = new RightPanel(this);
		this.add(leftPanel);
		this.add(rightPanel);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(main, 0, 0, null);
	}

	public void update(long elapsedTime) {
		leftPanel.update(elapsedTime);
		rightPanel.update(elapsedTime);
	}

	public void reset() {
		leftPanel.elastic.slide(-50, 50);
		rightPanel.elastic.slide(800, -180);
		if(gameGUI.netWorkManager instanceof MockNetWorkManager){
			leftPanel.players.get(0).removeAllElements();
			leftPanel.players.get(0).addElement("Player");
			for(int i=1;i<leftPanel.players.size();++i){
				DefaultComboBoxModel cbm = leftPanel.players.get(i);
				cbm.setSelectedItem("Computer");
			}
		}else{
			for(int i=1;i<leftPanel.players.size();++i){
				DefaultComboBoxModel cbm = leftPanel.players.get(i);
				if(cbm.getSelectedItem().equals("Computer"))
					cbm.setSelectedItem("Open");
			}
		}
		 
	}
	
	class TextPanel extends JPanel {

		private int x, y = 300;

		Elastic elastic = new Elastic(-50, 50);
		
		Image image = ResourceManager.loadImage("left.png");
		
		public TextPanel() {
			setLayout(null);
			setSize(400, 270);
			setLocation(x, y);
		}
		
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
			g.setColor(fillColor);
			g.fillRect(20, 345, getWidth() - 25, getHeight() - 50);
			g.setColor(borderColor);
			g.drawRect(20, 345, getWidth() - 25, getHeight() - 50);
			 
		}

		public void update(long elapsedTime) {
			x = elastic.update(elapsedTime);
			setLocation(x, y);
		}

		
	}

	class LeftPanel extends JPanel {
		private int x, y = 20;

		Elastic elastic = new Elastic(-50, 50);

		Image image = ResourceManager.loadImage("left.png");
		
		String labels[] = { "Open", "Closed","Computer"};
		
		
		LinkedList<DefaultComboBoxModel> players = new LinkedList<DefaultComboBoxModel>(); 
		
		String type[] = { "Terran"};
		
		JComboBox[][] jcbs = new JComboBox[4][2];
		 
		 
		
		public LeftPanel(JPanel parenPanel) {
			setLayout(null);
			setSize(400, 270);
			setLocation(x, y);
			int location=120;
			
			final DefaultComboBoxModel mode1 = new DefaultComboBoxModel(labels);
			mode1.addListDataListener(new ListDataListener(){

				public void contentsChanged(ListDataEvent e) {
					DefaultComboBoxModel model = (DefaultComboBoxModel) e.getSource();
					Object item = model.getSelectedItem();
					//System.out.println(item+ ".contentsChanged()  1");
					if(item.equals("Computer") || item.equals("Open") || item.equals("Closed")){
						gameGUI.netWorkManager.select(item.toString(), 1);
						//System.out.println(item);
					}
				}
				public void intervalAdded(ListDataEvent e) {}
				public void intervalRemoved(ListDataEvent e) {}
				
			});
			
			final DefaultComboBoxModel mode2 = new DefaultComboBoxModel(labels);
			mode2.addListDataListener(new ListDataListener(){

				public void contentsChanged(ListDataEvent e) {
					String value = mode2.getSelectedItem().toString();
					if(value.equals("Computer") || value.equals("Open") || value.equals("Closed")){
						gameGUI.netWorkManager.select(value,2);
					}
				}

				public void intervalAdded(ListDataEvent e) {}

				public void intervalRemoved(ListDataEvent e) {}
				
			});
			final DefaultComboBoxModel mode3 = new DefaultComboBoxModel(labels);
			mode3.addListDataListener(new ListDataListener(){

				public void contentsChanged(ListDataEvent e) {
					String value = mode3.getSelectedItem().toString();
					if(value.equals("Computer") || value.equals("Open") || value.equals("Closed")){
						gameGUI.netWorkManager.select(value,3);
					}
				}

				public void intervalAdded(ListDataEvent e) {}

				public void intervalRemoved(ListDataEvent e) {}
				
			});
			
			//如果是SingleGame
//			if(gameGUI.netWorkManager instanceof MockNetWorkManager){
//				players.add(new DefaultComboBoxModel(new String[]{"Player"}));
//			}else{
//				players.add(new DefaultComboBoxModel());
//			}
			//System.out.println("gameGUI.netWorkManager:"+gameGUI.netWorkManager);
			players.add(new DefaultComboBoxModel());
			players.add(mode1);
			players.add(mode2);
			players.add(mode3);
			
			
			for(int y=0;y<jcbs.length;++y){
				
				jcbs[y][0] = new JComboBox();
				jcbs[y][0].setModel(players.get(y));
				jcbs[y][0].addItemListener(new GameItemListener(y));
				jcbs[y][0].setLocation(50, location);
				initJComboBox(jcbs[y][0]);
				
				jcbs[y][1] = new JComboBox(type);
				jcbs[y][1].setLocation(200, location);
				jcbs[y][1].setVisible(false);
				initJComboBox(jcbs[y][1]);
				
				location+=30;
			}
		}
		
		public void addPlayer(int index,String name){
			System.out.println("addplayer:"+index+","+name);
			players.get(index).addElement(name);
			players.get(index).setSelectedItem(name);
			
		}
		
		public void selectPlayer(int index,String name){
			 
			players.get(index).setSelectedItem(name);
			
		}
		
		public void delPlayer(int index,String oldName){
 
			players.get(index).removeElement(oldName);
//			for(int i=0;i<players.get(index).getSize();++i){
//				Object item = players.get(index).getElementAt(i);
//				if(item.equals(newName)){
//					players.get(index).setSelectedItem(newName);
//					return;
//				}
//			}
//			players.get(index).addElement(newName);
		}
		 
		
		 
		
		private  class GameItemListener implements ItemListener{

			int index;
			
			public GameItemListener(int index) {
				this.index = index;
			}

			public void itemStateChanged(ItemEvent e) {
				
				String value = e.getItem().toString();
				if(value.equals("Closed")||e.getItem().toString().equals("Open")){
					jcbs[index][1].setVisible(false);
				}else{
					jcbs[index][1].setVisible(true);
				}
			}
			
		}
		private void initJComboBox(JComboBox jcb){
			jcb.setSize(120, 20);
			jcb.setOpaque(false);
			jcb.setFocusable(false);
			jcb.setRenderer(new StarComboBoxRender());
			jcb.setUI((ComboBoxUI) StarComboBoxUI.createUI(jcb));
			jcb.setBorder(new RoundBorder(Color.red));
			add(jcb);
		}
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
			g.setColor(fillColor);
			g.fillRect(20, 45, getWidth() - 25, getHeight() - 50);
			g.setColor(borderColor);
			g.drawRect(20, 45, getWidth() - 25, getHeight() - 50);
			g.setColor(titleColor);
			g.drawString("Create Game:", 50, 100);
		}

		public void update(long elapsedTime) {
			x = elastic.update(elapsedTime);
			setLocation(x, y);
		}

		/**
		 * New Game listener
		 */
		ActionListener newbtnLis = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				GameGUI gamePanel = (GameGUI) NewGamePanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600, 200);
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

			}
		};
	}

	/**
	 * 右边Panel
	 */
	class RightPanel extends JPanel {

		private int x, y;

		Elastic elastic = new Elastic(800, -180);

		Image image = ResourceManager.loadImage("right.png");

		JButton startbtn = new JButton("Start");
		
		JButton returnbtn = new JButton("Return");

		public RightPanel(JPanel parent) {

			setLayout(null);
			setSize(180, 180);
			x = (parent.getWidth() - getWidth());
			y = 460;
			setLocation(x, y);
			
			initButton(startbtn, startListen);
			startbtn.setLocation(0, 45);
			startbtn.setSize(150, 28);
			add(startbtn);
			
			initButton(returnbtn, returnListen);
			returnbtn.setLocation(0, 75);
			returnbtn.setSize(150, 28);
			add(returnbtn);
		}

		public void paintComponent(Graphics g) {

			g.drawImage(image, getWidth() - image.getWidth(null), 0, null);
		}

		public void update(long elapsedTime) {
			x = elastic.update(elapsedTime);
			setLocation(x, y);
		}

		ActionListener startListen = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlayerList playerList = gameGUI.netWorkManager.startGame();
				if(playerList!=null){
					leftPanel.elastic.slide(50, -300);
					rightPanel.elastic.slide(600, 200);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					gameGUI.switchProgress(playerList.getType(),playerList.getPlayers());
				}
			}
		};

		 
		ActionListener returnListen = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				leftPanel.players.get(0).removeAllElements();
				gameGUI.netWorkManager.close();
				GameGUI gamePanel = (GameGUI) NewGamePanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600, 200);
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
