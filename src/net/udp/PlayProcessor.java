package net.udp;

public abstract class PlayProcessor implements Runnable{

	protected PlayerContext context;
	
	protected boolean loop = true;
	
	public PlayProcessor(PlayerContext context) {
		super();
		this.context = context;
	}
	
	public void start(){
		loop = true;
		new Thread(this).start();
	}

	public void run() {
		process();
	}
	
	protected abstract void process();
	
	
}
