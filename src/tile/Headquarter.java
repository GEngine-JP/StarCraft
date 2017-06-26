package tile;

import java.awt.Image;
import java.awt.Point;
import util.Resource;

public class Headquarter extends House {

	private static final float BUILD_SPEED = 0.0003f;
	
	private static final Resource RESOURCE = new Resource(400,0);
	
	public Headquarter(Image[] images, int id) {
		super(images, id);
		this.currentImage = this.images[0];
	}
	
	public void update(long elapsedTime) {
		super.buiding(elapsedTime);
	}
	@Override
	public boolean build(long elapsedTime) {
		health = Math.min(1, health += elapsedTime*BUILD_SPEED);
		return health>=1;
	}
	 
	
	private final static Point SIZE = new Point(3,3);
	public Point getSize(){
		return SIZE;
	}

	@Override
	public Resource getResource() {
		return RESOURCE;
	}

	@Override
	public float getDefence() {
		return 0.0009f;
	}

	
}
