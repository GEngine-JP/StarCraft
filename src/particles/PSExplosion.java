package particles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;


public class PSExplosion {

	private static int DEFAULT_NUM_PARTICLES = 2;

	private static int PARTICLES_MAX_LIFE = 15;

	private static Color color = new Color(230, 100, 30);
	private static Color color2 = new Color(230, 200, 30);
	
	private Random rand = new Random();

	protected ArrayList particles = new ArrayList();

	protected Vector pos;

	public PSExplosion(float x, float y) {

		this.pos = new Vector(x, y, 0);

		for (int i = 0; i < DEFAULT_NUM_PARTICLES; i++) {

			particles.add(generateParticle());

		}
	}

	protected Particle generateParticle() {

		Vector volicity = new Vector(0.2f * (rand.nextFloat() - 0.5f), 0.2f* (rand
				.nextFloat() - 0.5f), 0);
		Vector pos = this.pos.add(new Vector(rand.nextInt(8)-4,rand.nextInt(8)-4,0));
		
		int life = 0;//rand.nextInt(PARTICLES_MAX_LIFE);
		
		Particle part = new Particle(pos, volicity, color,life );

		return part;
	}

	public void draw(Graphics2D g, int offsetX, int offsetY) {

		for (int i = 0; i < particles.size(); i++) {

			Particle part = (Particle) particles.get(i);
			
			if(part.getLife()>10){
				g.setColor(color);
			}else if(part.getLife()>5){
				g.setColor(Color.RED);
			}else{
				g.setColor(color2);
			}
			
			 
			g.fill(new Ellipse2D.Float(part.getPosition().getX() - offsetX,
					part.getPosition().getY() - offsetY, Math.max(1, 1.5f
							* part.getLife() / PARTICLES_MAX_LIFE), Math.max(1,
							1.5f * part.getLife() / PARTICLES_MAX_LIFE)));

		}
	}

	public boolean update(long elapsedTime) {

		Particle part;

		int count = particles.size();

		for (int i = 0; i < count; i++) {

			part = (Particle) particles.get(i);

			part.update(elapsedTime);
			if (part.getLife() > PARTICLES_MAX_LIFE) {
				particles.remove(i);
				i--;
				count = particles.size();

			}
		}

		if (particles.size() <= 0) {
//			 for (int i = 0; i < DEFAULT_NUM_PARTICLES; i++) {
//			 particles.add(generateParticle());
//			 }
			return false;
		}

		return true;
	}

}
