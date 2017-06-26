package tile;

import java.awt.Point;
import java.util.LinkedList;

import util.Resource;

public class Tank extends Sprite {

	private static final Resource RESOURCE = new Resource(100, 4);

	private final static Point SIZE = new Point(1, 1);

	public Tank(Animation[][] animations, int id) {
		super(animations, id);
	}

	public LinkedList<Point> move(float dx, float dy) {
		// 如果在攻击
		if (isReadFight() || isFighting()) {
			stop();
			return super.move(dx, dy);
		}
		return super.move(dx, dy);
	}

	public void update(long elapsedTime) {
		super.update(elapsedTime);
		if (isFighting()) {
			target.setHealth(target.getHealth() - 0.00008f);
			if (target.getHealth() <= 0) {
				stop();
				gm.splitTile(target);
				gm.addRemoveTile(target);
			}

		}

	}

//	 public void draw(Graphics2D g, int offsetX, int offsetY) {
//		super.draw(g, offsetX, offsetY);
//		g.drawRect(Math.round(x), Math.round(y), getWidth(), getHeight());
//	}
	
	@Override
	public Resource getResource() {
		return RESOURCE;
	}

	@Override
	public Point getSize() {
		return SIZE;
	}

	@Override
	public float getSpeed() {

		return 0.012f;
	}
}
