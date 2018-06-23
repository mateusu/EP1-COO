
public abstract class Projectile {
	protected static final int INACTIVE = 0;
	protected static final int ACTIVE = 1;
	protected static final int EXPLODING = 2;
	protected int state;
	protected double X, Y, V, VX, VY, radius;

	public abstract void draw();
	public abstract void movement(long delta);
	public void drawBoosted() {

	}
}
