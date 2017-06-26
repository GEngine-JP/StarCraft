package tile;

import java.awt.Image;
import java.awt.Point;

import util.Resource;

public class Supply extends House{

	
	private static final float BUILD_SPEED = 0.0005f;
	
	private static final Resource RESOURCE = new Resource(100,0);
	
	public Supply(Image[] images,int id) {
		super(images,id);
		this.currentImage = this.images[2]; 
	}
	
	public boolean build(long elapsedTime){
		
		health = Math.min(1, health += elapsedTime*BUILD_SPEED);
		return health>=1;
	}
	
	private final static Point SIZE = new Point(2,2);
	public Point getSize(){
		return SIZE;
	}

	@Override
	public Resource getResource() {
		return RESOURCE;
	}

	@Override
	public float getDefence() {
		return 0.0007f;
	}
	
}
