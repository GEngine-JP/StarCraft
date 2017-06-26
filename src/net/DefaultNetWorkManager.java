package net;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import tile.Sprite;
import tile.Tile;

import net.datagram.IconInfo;
import net.datagram.MoveInfo;
import net.datagram.PlayerInfo;
import net.datagram.PlayerList;
import net.datagram.SpriteInfo;
import net.socket.Client;
import net.socket.ClientListener;
import net.socket.Server;
import net.udp.Player;
import net.udp.PlayerListener;

public class DefaultNetWorkManager implements NetWorkManager {
	
	private static final int SOCKET_PORT = 8389;
	
	private static final int DATAGRAM_PORT = 8380;
	
	private static final int DETECTOR_PORT = 8381;
	
	Player player;

	Server server;

	Client client;
	
	 
	public DefaultNetWorkManager(String ip) throws SocketException {

		player = new Player(ip,DATAGRAM_PORT,DETECTOR_PORT);
		server = new Server(SOCKET_PORT);
		client = new Client(SOCKET_PORT);

	}

	public void establishServer(String serverName) {

		player.establishServer(serverName);
		server.establish(serverName);
	}

	public void join(PlayerInfo clientInfo, PlayerInfo serverInfo) {
	 
		client.start(clientInfo, serverInfo);

	}
 
	public void listen() {
		player.listen();

	}

	public void closeListen(){
		player.close();
	}

	public void addPlayerListener(PlayerListener playerListener) {
		player.addPlayerListener(playerListener);
	}

	public void addClientListener(ClientListener clientListener) {
		server.addClientListener(clientListener);
		client.addClientListener(clientListener);
	}

	public void move(List<Sprite> sprites, int tx, int ty) {
		 
		List<SpriteInfo> spriteInfos = new ArrayList<SpriteInfo>();
		for(Sprite sprite:sprites){
			 
			spriteInfos.add(new SpriteInfo(sprite.getUUID()));
		}
		MoveInfo moveInfo = new MoveInfo(spriteInfos,tx,ty);
		if (server.isEstablish()) {
			server.move(moveInfo);
		} else {
			client.move(moveInfo);
		}
	}
	
	public void operate(Tile tile, int tx, int ty,String newTileUUID) {
		
		if(tile==null)
			return; 
		MoveInfo info = new MoveInfo(new SpriteInfo(tile.getUUID()),tx,ty,newTileUUID);
		if (server.isEstablish()) {
			server.move(info);
		} else {
			client.move(info);
		}
	}
	public void remove(SpriteInfo spriteInfo) {
		spriteInfo.setAction(1);
		if (server.isEstablish()) {
			server.remove(spriteInfo);
		} else {
			client.remove(spriteInfo);
		}
		
		
	}
	public void build(IconInfo iconInfo){
		iconInfo.setAction(1);
		if (server.isEstablish()) {
			server.build(iconInfo);
		} else {
			client.build(iconInfo);
		}
	}
 
	
	public void readyBuild(IconInfo iconInfo){
		
		if (server.isEstablish()) {
			server.readyBuild(iconInfo);
		} else {
			client.readyBuild(iconInfo);
		}
	}
	 

	public void close(){
		closeListen();
		if(server.isEstablish()){
			server.close();
		}else{
			client.close();
		}
	}
	
	public void select(String name, int index) {

		if (server.isEstablish())
			server.select(name, index);
	}
	
	public PlayerList startGame() {
		PlayerList playerList = server.startGame();
		return playerList.getPlayers().size()==1?null:playerList;
	}

}
