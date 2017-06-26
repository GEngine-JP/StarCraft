package particles;

public final class Vector {

	public static Vector Zero = new Vector(0.0f, 0.0f, 0.0f);

	private final float x;

	private final float y;

	private final float z;

	public Vector(float inX, float inY, float inZ) {
		this.x = inX;
		this.y = inY;
		this.z = inZ;
	}

	public Vector add(Vector vector) {
		if (vector == null)
			return this;

		return new Vector(x + vector.getX(), y + vector.getY(), z
				+ vector.getZ());
	}

	public Vector multiply(float i) {

		return new Vector(x * i, y * i, z * i);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public String toString() {
		return x + "," + y;
	}

	public static void main(String[] args) {

		Vector test1 = new Vector(10, 10, 10);
		Vector test2 = new Vector(20, 20, 20);
		System.err.println(test1.add(test1.multiply(0.1f)));
	}
}
