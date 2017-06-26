package particles;
import java.awt.Color;

 

public class Particle {

	private Vector position;

	private Vector velocity;

	private int life;

	private Color color;


	public Particle(Vector pos, Vector velocity, Color col, int life) {
		
		this.position = pos;
		
		this.velocity = velocity;
		
		this.color = col;
		
		if (life > 0)
			this.life = life;
	}
	
	public  void update(long elapsedTime)
	{
		// Update particle's movement according to environment
//		velocity = velocity - Environment.GetInstance().getM_Gravity()+ Environment.GetInstance().getM_Wind();
		//System.out.println(velocity+"\t"+velocity.multiply(elapsedTime*0.1f));
		 
		velocity = velocity.add(velocity.multiply(0.2f));
		position = position.add(velocity);
		life++;
	}

	public int getLife() {
		return life;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
}
