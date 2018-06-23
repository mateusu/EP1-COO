import java.awt.Color;
import java.util.ArrayList;

public class Player extends ShipDecorator {
	long player_nextShot;
	int life_ini;
	int life_atual = life_ini;

	public Player(double X, double Y){
		this.X = X;
		this.Y = Y;
	}

	public Player() {
		state = ACTIVE;
		X = GameLib.WIDTH / 2;
		Y = GameLib.HEIGHT * 0.90;
		VX = 0.25;
		VY = 0.25;
		RV = 0;
		this.radius = 12.0;
		double player_explosion_start = 0;
		double player_explosion_end = 0; 
	}


	public void setNextShot(long player_nextShot) {
		this.player_nextShot = player_nextShot;
	}

	public long getNextShot() {
		return this.player_nextShot;
	}

	@Override
	public void draw(long currentTime) {
		if (state == EXPLODING) {
			double alpha = (currentTime - explosion_start) / (explosion_end - explosion_start);
			GameLib.drawExplosion(X, Y, alpha);
		} else {
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(X, Y, radius, life_atual, life_ini);
			GameLib.lifeInt(life_atual);
		}
	}

	@Override
	public void movement(long currentTime, long delta, ArrayList<Projectile> p_projectile, double nPProjectiles) {
		if (GameLib.iskeyPressed(GameLib.KEY_UP))
			Y -= delta * VY;
		if (GameLib.iskeyPressed(GameLib.KEY_DOWN))
			Y += delta * VY;
		if (GameLib.iskeyPressed(GameLib.KEY_LEFT))
			X -= delta * VX;
		if (GameLib.iskeyPressed(GameLib.KEY_RIGHT))
			X += delta * VY;
		if (GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {

			if (currentTime > player_nextShot) {

				int free = findFreeIndexProjectile(p_projectile);

				if (free < nPProjectiles) {

					p_projectile.get(free).X = X;
					p_projectile.get(free).Y = Y - 2 * radius;
					p_projectile.get(free).VX = 0.0;
					p_projectile.get(free).VY = -1.0;
					p_projectile.get(free).state = 1;
					player_nextShot = (currentTime + 100);
				}
			}
		}

	}

	private int findFreeIndexProjectile(ArrayList<Projectile> p_projectile) {

		int i;

		for (i = 0; i < p_projectile.size(); i++) {

			if (p_projectile.get(i).state == INACTIVE)
				break;
		}

		return i;
	}

	@Override
	public void collision(long currentTime, double dist) {
		state = EXPLODING;
		explosion_start = currentTime;
		explosion_end = currentTime + 2000;
		life_atual = life_ini;
	}

	protected int Getlife() {
		return life_atual;
	}

	public void losing_life() {
		life_atual--;
	}

}
