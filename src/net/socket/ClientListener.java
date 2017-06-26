package net.socket;


public interface ClientListener {

	
	public void addPlayer(ClientEvent e);
	
	public void delPlayer(ClientEvent e);

	public void selectPlayer(ClientEvent e);
	
	public void onJoinServer(ClientEvent e);
	
	public void onEstablishServer(ClientEvent e);
	
//	public void onJoinToServer(ClientEvent e);
	
	public void onStartGame(ClientEvent e);
	/**
	 * Sprite移动触发
	 * @param e
	 */
	public void onMove(ClientEvent e);
	
	/**
	 * 准备修建House触发
	 * @param e
	 */
	public void onReadyBuild(ClientEvent e);
	/**
	 * 当修建完毕Sprite触发
	 * @param e
	 */
	public void onBuild(ClientEvent e);
	/**
	 * 当主机丢失连接关闭的时候触发
	 * @param e
	 */
	public void onClose(ClientEvent e);
	
	/**
	 * 删除一个Tile触发
	 * @param e
	 */
	public void onRemoveTile(ClientEvent e);
	
	
	
}
