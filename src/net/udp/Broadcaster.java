package net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 广播
 * 
 * @author jiangyp
 */
public class Broadcaster extends PlayProcessor {

	private byte[] broadData;

	public Broadcaster(PlayerContext context) {
		super(context);
	}

	@Override
	protected void process() {
		
		DatagramSocket datagramSocket = context.getDatagramSocket();
		DatagramPacket sendPacket = new DatagramPacket(broadData,broadData.length);
		sendPacket.setSocketAddress(new InetSocketAddress("255.255.255.255",datagramSocket.getLocalPort()));
		 
		try {
			datagramSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	 
	public void start(byte[] broadData){
		this.broadData = broadData;
		new Thread(this).start();
	}
	
}
