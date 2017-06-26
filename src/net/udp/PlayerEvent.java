package net.udp;

import net.datagram.PlayerInfo;

public class PlayerEvent {

	PlayerInfo playerInfo;

	public PlayerEvent(PlayerInfo playerInfo) {
		super();
		this.playerInfo = playerInfo;
	}

	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}
	
	
	
}
