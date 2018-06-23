import java.awt.Color;
import java.util.ArrayList;

public class Boss1 extends Ship {
	int lifeboss1;
	int lifeboss1_ini;
	boolean flash;
	int direction = 0;
	long nextShoot = 0;

	public Boss1() {
		X = 0;
		Y = 0;
		V = 0;
		angle = 0;
		RV = 0;
		flash = false;
		this.radius = 70.0;
	}

	public void setlife(int life) {
		this.lifeboss1 = life;
		this.lifeboss1_ini = life;
	}

	public int getlife() {
		return this.lifeboss1_ini;
	}


	@Override
	public void draw(long currentTime) {
		if (state == EXPLODING) {
			double alpha = (currentTime - explosion_start) / (explosion_end - explosion_start);
			GameLib.drawExplosion(X, Y, alpha);
		}
		long itsHurting = currentTime + 500;
		if (state == ACTIVE) {
			if (!flash)
				GameLib.boss(X, Y, radius, "boss1.png", lifeboss1, lifeboss1_ini);
			if (flash) {
				if (currentTime < itsHurting)
					GameLib.boss(X, Y, radius, "boss1flash.png", lifeboss1, lifeboss1_ini);
				if (currentTime > itsHurting)
					GameLib.boss(X, Y, radius, "boss1.png", lifeboss1, lifeboss1_ini);
				flash = false;
			}
		}

	}

	@Override
	public void collision(long currentTime, double dist) {
		lifeboss1--;
		flash = true;
		if (lifeboss1 <= 0) {
			state = EXPLODING;
			explosion_start = currentTime;
			explosion_end = currentTime + 500;
		}

	}

	@Override
	public void movement(long currentTime, long delta, ArrayList<Projectile> b_projectile, double player_Y) {
		if (state == EXPLODING) {

			if (currentTime > explosion_end) {
				state = INACTIVE;
			}
		}

		if (state == ACTIVE) {
			if (Y > GameLib.HEIGHT + 10) {
				state = INACTIVE;
			} else {
				double previousY = Y;
				double threshold = GameLib.HEIGHT * 0.25;

				X += V * Math.cos(angle) * delta * (-0.3); // movimento X
				Y += V * Math.sin(angle) * delta * (-1.1); // movimento Y
				angle += RV * delta;

				if (previousY < threshold && Y >= threshold) {
					if (X < GameLib.WIDTH / 2)
						if (direction <= 0) {
							RV = 0.004;
							direction++;
						} else if (direction > 0) {
							RV = -0.004;
							direction--;
						}

				}

				if (currentTime > nextShoot && Y < player_Y) {
					int free = findFreeIndex(b_projectile);

					if (free < 200) {

						b_projectile.get(free).X = X;
						b_projectile.get(free).Y = Y;
						b_projectile.get(free).VX = 0.0;
						b_projectile.get(free).VY = 1.0;
						b_projectile.get(free).state = 1;

						nextShoot = (long) (currentTime + 200);
					}

				}

			}
		}
	}
}