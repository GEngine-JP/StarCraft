package net;

import java.util.ArrayList;
import java.util.List;

import net.datagram.IconInfo;
import net.datagram.PlayerInfo;
import net.datagram.PlayerList;
import net.datagram.SpriteInfo;
import net.socket.ClientListener;
import net.udp.PlayerListener;
import tile.Sprite;
import tile.Tile;
import util.RandomSequence;

public class MockNetWorkManager implements NetWorkManager{
	
	ArrayList<String> computers = new ArrayList<String>();
	 
	public void select(String name, int index) {
		if(name.equals("Computer")){
			computers.add(name);
		}
	}
	
	 
	public PlayerList startGame() {
		if(computers.size()<1)
			return null;
		List<Integer> players = RandomSequence.generate(computers.size()+1);
		return new PlayerList(players.get(computers.size()-1),players);
	}

	
	public void build(IconInfo iconInfo){
		
	}

	public void addClientListener(ClientListener clientListener) {
		// TODO Auto-generated method stub
		
	}

	public void addPlayerListener(PlayerListener playerListener) {
		// TODO Auto-generated method stub
		
	}

	public void closeListen() {
		// TODO Auto-generated method stub
		
	}

	public void establishServer(String serverName) {
		// TODO Auto-generated method stub
		
	}

	public void join(PlayerInfo clientInfo, PlayerInfo serverInfo) {
		// TODO Auto-generated method stub
		
	}

	public void listen() {
		// TODO Auto-generated method stub
		
	}

	public void move(List<Sprite> sprites, int tx, int ty) {
		// TODO Auto-generated method stub
		
	}

	public void readyBuild(IconInfo iconInfo) {
		// TODO Auto-generated method stub
		
	}


	public void close() {
		// TODO Auto-generated method stub
		
	}


	public void operate(Tile tile, int tx, int ty, String newTileUUID) {
		// TODO Auto-generated method stub
		
	}


	public void remove(SpriteInfo spriteInfo) {
		// TODO Auto-generated method stub
		
	}
	
}

