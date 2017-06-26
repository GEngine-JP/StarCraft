package net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import net.datagram.PlayerInfo;

public class Receiver extends PlayProcessor{

	private DatagramPacket dataPacket = new DatagramPacket(new byte[255],255);
	private boolean loop =true;
	public Receiver(PlayerContext context) {
		super(context);
	}

	private void listen(){
		
		try {
			
			DatagramSocket datagramSocket = context.getDatagramSocket();
			datagramSocket.receive(dataPacket);
			String msg = new String(dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());
			String serverName = context.getServerName();
			//System.out.println("request:"+msg+"\t"+dataPacket.getSocketAddress());
			if (msg.equals("request")) {
				
				if (serverName != null) {
					DatagramPacket send = new DatagramPacket(serverName.getBytes(), serverName.length());
					send.setSocketAddress(dataPacket.getSocketAddress());
					datagramSocket.send(send);
				
				}
				
			} else {
				
				PlayerInfo serverInfo = new PlayerInfo(msg, dataPacket.getSocketAddress(),null);
				if (!msg.equals(serverName)&& !context.contains(serverInfo)) {
					context.add(serverInfo);
				} else if (!dataPacket.getSocketAddress().equals(
						datagramSocket.getLocalSocketAddress())) {
					datagramSocket.send(dataPacket);
				}
			}

		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	 

	@Override
	protected void process() {
		while (loop) {
			
			listen();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
 
	public void close(){
		loop=false;
	}

}
