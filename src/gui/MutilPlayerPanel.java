package gui;

 
import gui.ui.combobox.RoundBorder;
import gui.ui.list.StarListCellRender;
import gui.ui.list.StarListUI;
import gui.ui.list.StarScrollBarUI;

import icon.BaseIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import net.DefaultNetWorkManager;
import net.NetWorkManager;
import net.datagram.IconInfo;
import net.datagram.MoveInfo;
import net.datagram.PlayerInfo;
import net.datagram.SpriteInfo;
import net.socket.ClientEvent;
import net.socket.ClientListener;
import net.udp.PlayerEvent;
import net.udp.PlayerListener;
import tile.Scv;
import tile.Sprite;
import tile.Tile;
import util.Elastic;
import core.FocusManager;
import core.GridMap;
import core.ResourceManager;
import core.ResourceManager.Constant;

@SuppressWarnings("serial")
public class MutilPlayerPanel extends Abstractpanel{
	
	LeftPanel leftPanel;
	RightPanel rightPanel;
	NetWorkManager netWorkManager;
	 
	public MutilPlayerPanel(final GameGUI gameGUI, String name) {
		super(gameGUI, name);
		leftPanel= new LeftPanel(this);
		rightPanel= new RightPanel(this);
		this.add(leftPanel);
		this.add(rightPanel);
		
		try {
			
			netWorkManager = new DefaultNetWorkManager(Constant.IP);
			//gameGUI.netWorkManager = netWorkManager; 
			
			netWorkManager.addPlayerListener(new PlayerListener(){

				public void join(PlayerEvent e) {
				 
					leftPanel.model.addElement(e.getPlayerInfo());
				}

				public void quit(PlayerEvent e) {
					
					leftPanel.model.removeElement(e.getPlayerInfo());
				}
				
			});
			
			netWorkManager.addClientListener(new ClientListener(){

				public void addPlayer(ClientEvent e) {
					
					PlayerInfo playerInfo = e.getPlayerInfo();
					gameGUI.newGamePanel.leftPanel.addPlayer(playerInfo.getType(), playerInfo.getServerName());
					 
				}
				
				public void selectPlayer(ClientEvent e) {
					PlayerInfo playerInfo = e.getPlayerInfo();
					gameGUI.newGamePanel.leftPanel.selectPlayer(playerInfo.getType(), playerInfo.getServerName());
				}
				
				public void delPlayer(ClientEvent e){
					PlayerInfo playerInfo = e.getPlayerInfo();
					gameGUI.newGamePanel.leftPanel.delPlayer(playerInfo.getType(), playerInfo.getServerName());
					 
					
				}
				
				public void onJoinServer(ClientEvent e) {

					JComboBox[][] jcbs = gameGUI.newGamePanel.leftPanel.jcbs;
					for (int y = 0; y < jcbs.length; ++y) {
						jcbs[y][0].setEnabled(false);
						jcbs[y][1].setEnabled(false);
					}
					jcbs[e.getPlayerInfo().getType()][1].setEnabled(true);
					gameGUI.newGamePanel.rightPanel.startbtn.setEnabled(false);
				}
				
				public void onEstablishServer(ClientEvent e) {
					 
					JComboBox[][] jcbs = gameGUI.newGamePanel.leftPanel.jcbs;
					for (int y = 0; y < jcbs.length; ++y) {
						jcbs[y][1].setEnabled(false);
					}
					jcbs[e.getPlayerInfo().getType()][1].setEnabled(true);
				}
			
				public void onStartGame(ClientEvent e) {
					
					gameGUI.switchProgress(e.getPlayerList().getType(),e.getPlayerList().getPlayers());
					
				}
			
				public void onMove(ClientEvent e) {
					
					
					FocusManager fm = gameGUI.mapRender.getFm();
					MoveInfo info = e.getMoveInfo();
					SpriteInfo operator = info.getOperator();
					
					if(info.getOperator()!=null){
						Tile tile = gameGUI.mapRender.getGridMap().getTile(operator.getUUID());
						fm.operate(tile, info.getX(), info.getY(),info.getNewTileUUID());
					}else{
						LinkedList<Sprite> sprites = new LinkedList<Sprite>();
						for(SpriteInfo s:info.getSprites()){
							//System.out.println("sprite:"+s.getTileX()+","+s.getTileY()+"\t"+(Sprite)mapRender.getGridMap().getTile(s.getTileX(), s.getTileY()));
							sprites.add((Sprite)gameGUI.mapRender.getGridMap().getTile(s.getUUID()));
						}
						System.out.println("OnMove,"+sprites.size()+","+info.getX()+","+info.getY());
						fm.move(sprites, info.getX(), info.getY());
					}
					
				}
				public void onReadyBuild(ClientEvent e) {
					IconInfo iconInfo = e.getIconInfo();
					Scv scv = (Scv) gameGUI.mapRender.getGridMap().getTile(iconInfo.getTileUUId());
					System.out.println(".onReadyBuild()");
					scv.readyBuild(gameGUI.mapRender.getGridMap().getIconValue(iconInfo.getName()));
				}
				public void onBuild(ClientEvent e){
					IconInfo iconInfo = e.getIconInfo();
					BaseIcon icon =  gameGUI.mapRender.getGridMap().getIconValue(iconInfo.getName());
					System.out.println(".onBuild():"+iconInfo.getTileUUId()+","+ gameGUI.mapRender.getGridMap().getTile(iconInfo.getTileUUId()));
					Tile house = gameGUI.mapRender.getGridMap().getTile(iconInfo.getTileUUId());
					String newTileUUId = iconInfo.getNewTileUUId();
					gameGUI.mapRender.getGridMap().buildSprite(house, icon,newTileUUId);
				}
				public void onRemoveTile(ClientEvent e){
					
					GridMap gm = gameGUI.mapRender.getGridMap();
					Tile tile = gm.getTile(e.getSpriteInfo().getUUID());
					gameGUI.mapRender.addRemoveTileNetCallback(tile);
				}
				public void onClose(ClientEvent e) {
					netWorkManager.close();
					gameGUI.newGamePanel.leftPanel.elastic.slide(50, -300);
					gameGUI.newGamePanel.rightPanel.elastic.slide(600, 200);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					gameGUI.switchMainMenu();
					
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
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
		netWorkManager.listen();
		System.out.println("MutilPlayerPanel.reset():"+gameGUI.netWorkManager);
	}
	
	
	 class LeftPanel extends JPanel{
		
		private int x,y=20;
		
		Elastic elastic = new Elastic(-50,50);
		
		Image image = ResourceManager.loadImage("left.png");
		
		JLabel label;
		
		JTextField textField;
		
		JButton createBtn = new JButton("Create");
		
		JButton joinBtn = new JButton("Join");
		
		JList list = new JList();
		
		DefaultListModel model = new DefaultListModel();
		
		public LeftPanel(JPanel parent) {
			setSize(320, 400);
			setLocation(x,y);
			setLayout(null);
			
			textField = new JTextField(Constant.IP);
			textField.setBorder(BorderFactory.createLineBorder(Color.red));
			textField.setLocation(30, 70);
			textField.setSize(270, 20);
			textField.setForeground(textColor);
			textField.setBackground(Color.BLACK);
			
			list.setModel(model);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setCellRenderer(new StarListCellRender());
			list.setBackground(Color.BLACK);
			list.setUI((StarListUI) StarListUI.createUI(list));
			list.setVisibleRowCount(5);
			list.setSelectedIndex(0);
			
			JScrollPane scrollPane = new JScrollPane(list);
			scrollPane.setLocation(30, 110);
			scrollPane.setSize(270, 200);
			scrollPane.getVerticalScrollBar().setUI((StarScrollBarUI)StarScrollBarUI.createUI(list));
			scrollPane.setBorder(new RoundBorder(Color.RED));
			
			add(textField);
			add(scrollPane);
			 
			
			initButton(createBtn, createBtnLis);
			createBtn.setLocation(30, 330);
			createBtn.setSize(100, 28);
			add(createBtn);
			
			initButton(joinBtn, joinBtnLis);
			joinBtn.setLocation(160, 330);
			joinBtn.setSize(100, 28);
			add(joinBtn);
			
			//initButton(newbtn, newbtnLis);
			//newbtn.setLocation(40, 130);
			//newbtn.setSize(150, 28);
			//add(newbtn);
			
			//initButton(loadbtn, loadbtnLis);
			//loadbtn.setLocation(40, 200);
			//loadbtn.setSize(150, 28);
			//add(loadbtn);
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
		ActionListener createBtnLis = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				String serverName = textField.getText();
				if(serverName.trim().length()==0)return;
				
				GameGUI gamePanel = (GameGUI) MutilPlayerPanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600,200);
				if(netWorkManager!=null){
					netWorkManager.establishServer(serverName);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				gamePanel.switchNewGame();
			}
		};
		ActionListener joinBtnLis = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if(list.getSelectedIndex()==-1)return;
				
				GameGUI gui = (GameGUI) MutilPlayerPanel.this.getParent();
				leftPanel.elastic.slide(50, -300);
				rightPanel.elastic.slide(600,200);
				if(netWorkManager!=null){
					//client.join(textField.getText(), list.getSelectedValue().toString());
					netWorkManager.join(new PlayerInfo(textField.getText()), (PlayerInfo) list.getSelectedValue());
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				gui.switchNewGame();
			}
		};
	}
	
	/**
	 *右边Panel
	 */
	 class RightPanel extends JPanel{
		
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
				
				netWorkManager.closeListen();
				
				GameGUI gamePanel = (GameGUI) MutilPlayerPanel.this.getParent();
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
