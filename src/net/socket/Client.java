package net.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.datagram.IconInfo;
import net.datagram.MoveInfo;
import net.datagram.PlayerInfo;
import net.datagram.PlayerList;
import net.datagram.SpriteInfo;
 

public class Client implements Runnable{
	
	private Socket socket;
	
	private ClientListener clientListener;
	
	private PlayerInfo clientInfo;
	
	private int port;
	
	private boolean loop=true;
	
	public Client(int port) {
		super();
		this.port = port;
	}

	public void run() {
		
		try {
			listen();
			
		} catch (Exception e) {
			e.printStackTrace();
			this.clientListener.onClose(null);
		}
	}
	
	private void listen() throws Exception{
		
		//监听返回信息
		while(loop){
			
			//获取信息
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Object obj =in.readObject();
			System.out.println("Client.listen() "+obj);
			if(obj instanceof PlayerInfo){
				
				PlayerInfo player = (PlayerInfo) obj;
				//System.out.println(player.getServerName()+"\t"+this.clientInfo.getSocketAddress().equals(player.getSocketAddress()));
				
				if(player.getAction()==2){
					clientListener.selectPlayer(new ClientEvent(player));
				}
				if(player.isRemove()){
					clientListener.delPlayer(new ClientEvent(player));
				}else{
					clientListener.addPlayer(new ClientEvent(player));
				}
				//如果是当前返回的用户信息,包含了Server分配的index
				if(clientInfo.equals(player)){
					clientListener.onJoinServer(new ClientEvent(player));
				}
				
				
			}else if(obj instanceof PlayerList){
				
				PlayerList player = (PlayerList) obj;
				clientListener.onStartGame(new ClientEvent(player));
				
			}else if(obj instanceof MoveInfo){
				
				MoveInfo moveInfo = (MoveInfo) obj;
				clientListener.onMove(new ClientEvent(moveInfo));
				
			}else if(obj instanceof IconInfo){
			
				IconInfo moveInfo = (IconInfo) obj;
				if(moveInfo.getAction()==1){
					clientListener.onBuild(new ClientEvent(moveInfo));
				}else{
					clientListener.onReadyBuild(new ClientEvent(moveInfo));
				}
			
			}else if(obj instanceof SpriteInfo){
			
				SpriteInfo moveInfo = (SpriteInfo) obj;
				if(moveInfo.getAction()==1){
					clientListener.onRemoveTile(new ClientEvent(moveInfo));
				}
			
			}
			 
		}
	}
	
	public void move(MoveInfo moveInfo){
		send(moveInfo);
	}
	
	public void remove(SpriteInfo moveInfo){
		send(moveInfo);
	}
	
	public void build(IconInfo moveInfo){
		send(moveInfo);
	}
	
	public void readyBuild(IconInfo iconInfo){
		send(iconInfo);
	}
	
	public void send(Object info){
		
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(info);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(PlayerInfo clientInfo, PlayerInfo serverInfo){
		//绑定
		try {
			//clientInfo.setSocketAddress(InetAddress.getLocalHost(), port);
			loop=true;
			socket = new Socket(serverInfo.getAddress(),port);
			clientInfo.setSocketAddress(socket.getLocalSocketAddress());
			//请求
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(clientInfo);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.clientInfo = clientInfo;
		new Thread(this).start();
	}
	
	public void addClientListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}
	
	public void close(){
		System.out.println("Client.close()");
		try {
			
			socket.close();
			loop = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
