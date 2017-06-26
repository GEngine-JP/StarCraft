package test;

import java.awt.Graphics2D;

import core.GameCore;

@SuppressWarnings("serial")
public class MyTestGameCore extends GameCore {

	@Override
	public void draw(Graphics2D g) {

	}

	public void update(long elapsedTime) {
	 
	}

	public static void main(String[] args) {

		new MyTestGameCore().run();
	}

}
