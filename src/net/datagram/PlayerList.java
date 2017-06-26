package net.datagram;

import java.util.List;

@SuppressWarnings("serial")
public class PlayerList implements java.io.Serializable{
 

	private int type;
	
	private List<Integer> players;
	
	
	public PlayerList(int type, List<Integer> players) {
		super();
		this.type = type;
		this.players = players;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Integer> getPlayers() {
		return players;
	}

	public void setPlayers(List<Integer> players) {
		this.players = players;
	}
	
	
	
}

