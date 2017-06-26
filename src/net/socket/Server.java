package net.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.NetWorkManager;
import net.datagram.IconInfo;
import net.datagram.MoveInfo;
import net.datagram.PlayerInfo;
import net.datagram.PlayerList;
import net.datagram.SpriteInfo;
import net.util.Stack;
import util.RandomSequence;

public class Server implements Runnable{

	private boolean loop=true;
	
	private ServerSocket server ;
	 
	private int port;
	
	private Stack stack;
	
	ClientListener clientListener;
	
	/**
	 * 保存每个连接进来的客户端信息
	 */
	LinkedList<PlayerInfo> playerList = new LinkedList<PlayerInfo>();
	
	List<ServerProcessor> processors = new LinkedList<ServerProcessor>();
	
	NetWorkManager netWorkManager;
	
	public Server(int port) {
		super();
		this.port = port;
		 
	}

	public void establish(String serverName) {
		
		try {
			loop=true;
			server = new ServerSocket(port);
			stack = new Stack(4);
			stack.use(0);
			playerList.add(new PlayerInfo(serverName,null,0));
			
			 
			clientListener.addPlayer(new ClientEvent(playerList.getFirst()));
			clientListener.onEstablishServer(new ClientEvent(playerList.getFirst()));
			 
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new Thread(this).start();
		
	}
	
	public boolean isEstablish(){
		return server!=null;
	}
	
	public void removeClient(ServerProcessor processor){
		
		processors.remove(processor);
		PlayerInfo playerInfo  = processor.getClientInfo();
		playerInfo.setRemove(true);
		for(ServerProcessor serverProcessor:processors){
			serverProcessor.send(playerInfo);
		}
		this.clientListener.delPlayer(new ClientEvent(playerInfo));
		
		PlayerInfo selectInfo  = processor.getClientInfo();
		selectInfo.setAction(2);
		selectInfo.setServerName("Open");
		this.clientListener.selectPlayer(new ClientEvent(selectInfo));
		
	}
	
	public void close(){
		try {
			loop=false;
			for(ServerProcessor processor:processors){
				processor.close();
				processor=null;
			}
			server.close();
			server = null;
			processors.clear();
			playerList.clear();
			stack=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void listen(){
		
		try {
			 
			Socket socket = server.accept();
			//System.out.println("server SocketAddress:"+socket.getLocalSocketAddress());
			ServerProcessor processor = new ServerProcessor(this,socket);
			processors.add(processor);
			processor.start();
			
		} catch (java.net.SocketException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		while(loop){
			
			listen();
			
		}
	}
	
	public void move(MoveInfo moveInfo){
		this.batchSend(null,moveInfo);
	}
	
	public void build(IconInfo moveInfo){
		this.batchSend(null,moveInfo);
	}
	
	public void remove(SpriteInfo moveInfo){
		this.batchSend(null,moveInfo);
	}
	
	public void readyBuild(IconInfo iconInfo){
		this.batchSend(null,iconInfo);
	}
	
	public boolean checkRestore(String name,int index){
		 
		Iterator it = playerList.iterator();
		while(it.hasNext()){
			PlayerInfo playerInfo = (PlayerInfo) it.next();
			//如果之前playerList是Computer或者用户,而当前是Open或者Closed需要restore.
			if(playerInfo.getType()==index){
				
				if(!playerInfo.getServerName().equals("Open")&&!playerInfo.getServerName().equals("Closed")){
					it.remove();
					return true;
				}
				//System.out.println("check: "+name+"\t"+playerInfo.getServerName());
				//if((name.equals("Open")||name.equals("Closed"))&&(!playerInfo.getServerName().equals("Open")&&!playerInfo.getServerName().equals("Closed"))){
				//	System.out.println("remove: "+name+"\t"+playerInfo.getServerName());
				//	it.remove();
				//	return true;
				//}
			}
		}
		 
		return false;
	}
	public void select(String name,int index){
		
		//System.out.println("select:"+index+"\t"+name);
		if(checkRestore(name, index)){
			System.out.println("restore:"+playerList.size());
			stack.restore(index);
		}
		PlayerInfo playerInfo = new PlayerInfo(name,index,true);
		playerInfo.setAction(2);
		if(!name.equals("Open")&&!name.equals("Closed")){
			playerList.add(playerInfo);
		}
		
		for(ServerProcessor processor:processors){
			processor.send(playerInfo);
		}
		
		if(name.endsWith("Computer")){
			stack.use(index);
		}
	}
	
	public void quitComputer(int index){
		
		PlayerInfo playerInfo = new PlayerInfo("Computer",index,true);
		playerInfo.setRemove(true);
		
		for(ServerProcessor processor:processors){
			
			processor.send(playerInfo);
		}
		stack.restore(index);
	}
	
	
	public void joinPlayer(PlayerInfo playerInfo){
		
		int index = stack.next();
		playerInfo.setType(index);
		this.clientListener.addPlayer(new ClientEvent(playerInfo));
		for(ServerProcessor processor:processors){
			
			processor.send(playerInfo);
		}
		 
	}
	
	
	public void addClientListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}
	
	public PlayerList startGame() {
		
		List<Integer> players = RandomSequence.generate(playerList.size());
		System.out.println("playerList="+playerList.size()+","+players);
		for(int i=0;i<processors.size();++i){
			ServerProcessor processor = processors.get(i);
			processor.send(new PlayerList(players.get(i),players));
		}
		
		return new PlayerList(players.get(playerList.size()-1),players);
		
		
//		PlayerList server = new PlayerList();
//		server.add(0);
//		server.setType(0);
//
//		for (int type : clients.keySet()) {
//			try {
//				PlayerList plays = new PlayerList();
//				plays.add(0);
//				Socket client = clients.get(type);
//				for (SocketInfo info : infos) {
//					plays.add(info.getIndex() + 1);
//					server.add(info.getIndex() + 1);
//
//				}
//				plays.setType(type + 1);
//				ObjectOutputStream out = new ObjectOutputStream(client
//						.getOutputStream());
//				out.writeObject(plays);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

	}
	
	void batchSend(ServerProcessor clientProcessor,Object info){
		
		for(ServerProcessor processor:processors){
			
			if(processor.equals(clientProcessor))
				continue;
			processor.send(info);
		}
		
	}
	
	public String getServerName() {
		return playerList.getFirst().getServerName();
	}
	
	 
	 
}
