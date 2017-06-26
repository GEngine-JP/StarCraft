package util;

public class Elastic {

	private double totalElapsedTime;

	private int t;

	private int init;

	private int change;

	public Elastic() {
		super();
	}

	public Elastic(int init, int change) {
		super();
		this.init = init;
		this.change = change;
	}

	public void slide(int init, int change) {
		this.init = init;
		this.change = change;
		t = 0;
	}
	
	public int update(long elapsedTime) {

		this.totalElapsedTime += elapsedTime;
		if (this.totalElapsedTime + elapsedTime > 25&&t<100) {
			++t;
			this.totalElapsedTime = 0;
		}
		return (int) Math.round(easeOut(t, init, change, 100, 0, 0));
	}
	
	private double easeOut(float t, float b, float c, float d, float a, float p) {
		double s;
		if (t == 0)
			return b;
		if ((t /= d) == 1)
			return b + c;
		if (p == 0)
			p = d * 0.3f;
		if (a == 0 || a < Math.abs(c)) {
			a = c;
			s = p / 4;
		} else {
			s = p / (2 * Math.PI) * Math.asin(c / a);
		}
		return (a * Math.pow(2, -10 * t)
				* Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b);
	}

}
