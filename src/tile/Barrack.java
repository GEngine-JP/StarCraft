package tile;

import java.awt.Image;
import java.awt.Point;

import util.Resource;

/**
 * 兵营
 * @author jiangyp
 */
public class Barrack extends House{

	private static final float BUILD_SPEED = 0.0002f;
	
	private static final Resource RESOURCE = new Resource(150,0);
	
	public Barrack(Image[] images, int id) {
		super(images, id);
		this.currentImage = this.images[2]; 
	}

	@Override
	public boolean build(long elapsedTime) {
		health = Math.min(1, health += elapsedTime*BUILD_SPEED);
		return health>=1;
	}
	private final static Point SIZE = new Point(3,3);
	@Override
	public Point getSize() {
		return SIZE;
	}

	@Override
	public Resource getResource() {
		return RESOURCE;
	}

	@Override
	public float getDefence() {
	 
		return 0.0008f;
	}


}
