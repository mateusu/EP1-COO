import java.util.ArrayList;

public abstract class Ship {
	protected static final int INACTIVE = 0;
	protected static final int ACTIVE = 1;
	protected static final int EXPLODING = 2;
	protected int state, life;
	protected double X, Y, V, angle, RV, explosion_start, explosion_end, radius, VX, VY;

	public abstract void draw(long currentTime);

	public abstract void collision(long currentTime, double dist);

	public abstract void movement(long currentTime, long delta, ArrayList<Projectile> e_projectile, double player_Y);

	public void powerUp(Ship player){
		player.VX = 0.50;
		player.VY = 0.50;
	}

	public static int findFreeIndex(ArrayList<Projectile> e_projectile) {

		int i;

		for (i = 0; i < e_projectile.size(); i++) {
			if (e_projectile.get(i).state == INACTIVE)
				break;
		}
		return i;
	}

	public static int[] findFreeIndex(ArrayList<Projectile> e_projectile, int amount) {
		int i, k;
		int[] freeArray = { 200, 200, 200 };

		for (i = 0, k = 0; i < e_projectile.size() && k < amount; i++) {

			if (e_projectile.get(i).state == INACTIVE) {
				freeArray[k] = i;
				k++;
			}
		}

		return freeArray;
	}
}