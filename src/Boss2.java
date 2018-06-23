import java.util.ArrayList;

public class Boss2 extends Ship {
	int lifeboss2;
	int lifeboss2_ini;
	boolean flash;
	int direction;
	int nProjectiles = 75;

	public Boss2() {
		flash = false;
		X = 0;
		Y = 0;
		V = 0;
		angle = 0;
		RV = 0;
		this.radius = 40.0;
	}

	public void setlife(int life) {
		this.lifeboss2 = life;
		this.lifeboss2_ini = life;
	}

	public int getlife() {
		return this.lifeboss2_ini;
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
				GameLib.boss(X, Y, radius, "boss2.png", lifeboss2, lifeboss2_ini);
			if (flash) {
				if (currentTime < itsHurting)
					GameLib.boss(X, Y, radius, "boss2flash.png", lifeboss2, lifeboss2_ini);
				if (currentTime > itsHurting)
					GameLib.boss(X, Y, radius, "boss2.png", lifeboss2, lifeboss2_ini);
				flash = false;
			}
		}
	}

	@Override
	public void collision(long currentTime, double dist) {
		lifeboss2--;
		flash = true;
		if (lifeboss2 <= 0) {
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

				X += V * Math.cos(angle) * delta * (-0.7); // movimento X
				Y += V * Math.sin(angle) * delta * (-0.1); // movimento Y

				angle += RV * delta;



				if (previousY < threshold && Y >= threshold) {
					if (direction == 0)
						direction--;
					if (X < GameLib.WIDTH / 2)
						if (direction < 0) {
							RV = 0.004;
							direction = 1;
						} else if (direction > 0) {
							RV = -0.004;
							direction = -1;
						}

				}
			}

			double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 };
			int[] freeArray = findFreeIndex(b_projectile, angles.length);

			for (int k = 0; k < freeArray.length; k++) {

				int free = freeArray[k];

				if (free < 75) {

					double a = angles[k] + Math.random() * Math.PI / 6 - Math.PI / 12;
					double vx = Math.cos(a)+0.2;
					double vy = Math.sin(a);
					if (direction == 0)
						b_projectile.get(free).X = X+5;
					else if (direction > 0)
						b_projectile.get(free).X = X+78;
					else
						b_projectile.get(free).X = X-70;

					b_projectile.get(free).Y = Y;
					b_projectile.get(free).VX = vx * 0.30;
					b_projectile.get(free).VY = vy * 0.30;
					b_projectile.get(free).state = 1;
				}
			}
		}
	}
}
