package net.udp;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;

import net.datagram.PlayerInfo;


public class Player {

	private static final byte[] FIND_DATA = "request".getBytes();
	
	private Broadcaster broadcaster;

	private Detector detector;

	private Receiver receiver;
	
	private PlayerContext context;
	
	private DatagramSocket datagramSocket;
	
	private DatagramSocket detectorSocket;
	
	private String ip;
	
	private int datagramPort;
	
	private int  detectorPort;
	
	public Player(String ip,int datagramPort,int  detectorPort) throws SocketException {
	
		this.ip = ip;
		this.datagramPort = datagramPort;  
		this.detectorPort = detectorPort;  
		context = new PlayerContext();

	}

	public void listen() {
		try {
			datagramSocket = new DatagramSocket(new InetSocketAddress(ip, datagramPort));
			detectorSocket = new DatagramSocket(new InetSocketAddress(ip, detectorPort));
			detectorSocket.setSoTimeout(3000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		context.setDatagramSocket(datagramSocket);
		context.setDetectorSocket(detectorSocket);
		receiver = new Receiver(context);
		broadcaster = new Broadcaster(context);
		detector = new Detector(context);
		broadcaster.start(FIND_DATA);
		receiver.start();
		detector.start();
	}

	public void close(){
		
		datagramSocket.close();
		detectorSocket.close();
		receiver.close();
		detector.close();
		
		List<PlayerInfo> serverList = context.getPlayerInfos();
		synchronized (serverList) {
			Iterator<PlayerInfo> i = serverList.iterator();
			while(i.hasNext()){
				context.getPlayerListener().quit(new PlayerEvent(i.next()));
				i.remove();
				 
			}
		}
		 
	}
	
	public void addPlayerListener(PlayerListener clientListener){
		context.addPlayerListener(clientListener);
	}
	
	public void establishServer(String serverName) {
		 
		context.setServerName(serverName);
		broadcaster.start(serverName.getBytes());
	}
	
	 
}
