package net.udp;


/**
 * 对用户列表的事件监听
 * @author jiangyp
 *
 */
public interface PlayerListener {

	public void join(PlayerEvent e);
	
	public void quit(PlayerEvent e);
}
