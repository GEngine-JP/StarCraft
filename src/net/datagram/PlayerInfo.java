package net.datagram;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Player信息
 * @author jiangyp
 */
public class PlayerInfo implements java.io.Serializable{
	
	private boolean isComputer;
	
	private boolean isRemove;
	
	private String serverName;

	private InetSocketAddress socketAddress;
	
	private Integer type;
	
	//行为:1表示删除,2表示选中
	private int action;
	
	public PlayerInfo(String serverName, SocketAddress socketAddress, Integer type) {
		super();
		this.serverName = serverName;
		this.socketAddress = (InetSocketAddress) socketAddress;
		this.type = type;
	}
	

	public PlayerInfo(String serverName, SocketAddress socketAddress) {
		this(serverName,socketAddress,null);
	}
	
	public PlayerInfo(String serverName) {
		this(serverName,null,null);
	}

	public PlayerInfo(String serverName, Integer type,boolean isComputer) {
		super();
		this.isComputer = isComputer;
		this.serverName = serverName;
		this.type = type;
	}

//	public PlayerInfo(boolean isComputer, Integer type) {
//		super();
//		this.isComputer = isComputer;
//		this.type = type;
//	}





	public boolean equals(Object obj){
		
		if(!(obj instanceof PlayerInfo)){
			return false;
		}else{
			PlayerInfo info = (PlayerInfo) obj;
			return serverName.equals(info.serverName)&&socketAddress.equals(info.socketAddress);
		}
	}
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}
	
	
	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = (InetSocketAddress) socketAddress;
	}
	
	public void setSocketAddress(InetAddress address,int port) {
		this.socketAddress = new InetSocketAddress(address,port);
	}


	public InetAddress getAddress() {
		return socketAddress.getAddress();
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


	public boolean isComputer() {
		return isComputer;
	}


	public boolean isRemove() {
		return isRemove;
	}


	public void setComputer(boolean isComputer) {
		this.isComputer = isComputer;
	}


	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}
	
	public String toString(){
		return serverName;
	}


	public int getAction() {
		return action;
	}


	public void setAction(int action) {
		this.action = action;
	}
	
}
