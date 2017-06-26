package test;

import gui.GameGUI;

import java.awt.Graphics2D;
import java.net.InetAddress;

import core.FullGameCore;
import core.ResourceManager.Constant;

/**
 * @author jiangyp
 * 遗留问题:
 * 当使用二分搜索时，没有对move方法进行控制，这样会有问题。
 */
@SuppressWarnings("serial")
public class TestGameCore extends FullGameCore {

	GameGUI gamePanel;


	public void init() {
		super.init();
		gamePanel = new GameGUI(getWindow());
	}

	@Override
	public void draw(Graphics2D g) {
		
		getWindow().getLayeredPane().paintComponents(g);

	}

	public void update(long elapsedTime) {
		 
		gamePanel.update(elapsedTime);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	
		if(args.length>0){
			Constant.IP = args[0];
		}else{
			Constant.IP = InetAddress.getLocalHost().getHostAddress();
		}
		new TestGameCore().run();
	}

}
