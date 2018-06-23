import java.awt.Color;
import java.util.ArrayList;

public class Enemy1 extends Ship {
	long nextShoot = 0;

	public Enemy1() {
		angle = 0;
		RV = 0;
		this.radius = 9.0;
	}

	public void movement(long currentTime, long delta, ArrayList<Projectile> e_projectile, double player_Y) {
		if (state == EXPLODING) {
			if (currentTime > explosion_end) {
				state = INACTIVE;
			}
		}

		if (state == ACTIVE) {
			if (Y > GameLib.HEIGHT + 10) {
				state = INACTIVE;
			} else {
				X += V * Math.cos(angle) * delta;
				Y += V * Math.sin(angle) * delta * (-1.0);
				angle += RV * delta;
				if (currentTime > nextShoot && Y < player_Y) {
					int free = findFreeIndex(e_projectile);
					if (free < 200) {
						e_projectile.get(free).X = X;
						e_projectile.get(free).Y = Y;
						e_projectile.get(free).VX = Math.cos(angle) * 0.45;
						e_projectile.get(free).VY = Math.sin(angle) * 0.45 * (-1.0);
						e_projectile.get(free).state = 1;
						nextShoot = (long) (currentTime + 200 + Math.random() * 500);
					}
				}
			}
		}

	}
	@Override
	public void draw(long currentTime) {
		if (state == EXPLODING) {
			double alpha = (currentTime - explosion_start) / (explosion_end - explosion_start);
			GameLib.drawExplosion(X, Y, alpha);
		}

		if (state == ACTIVE) {
			GameLib.enemy1(X, Y-20, radius, "enemies3.png");
		}

	}

	@Override
	public void collision(long currentTime, double dist) {
		state = EXPLODING;
		explosion_start = currentTime;
		explosion_end = currentTime + 500;

	}

}


