import java.awt.Color;
import java.util.ArrayList;

public class Enemy2 extends Ship {
	public Enemy2() {
		X = 0;
		Y = 0;
		V = 0;
		angle = 0;
		RV = 0;
		this.radius = 12.0;
	}

	public void movement(long currentTime, long delta, ArrayList<Projectile> e_projectile, double player_Y) {
		boolean shootNow = false;
		if (state == EXPLODING) {
			if (currentTime > explosion_end) {
				state = INACTIVE;
			}
		}

		if (state == ACTIVE) {
			if (X < -10 || X > GameLib.WIDTH + 10) {
				state = INACTIVE;
			} else {
				shootNow = false;
				double previousY = Y;
				X += V * Math.cos(angle) * delta;
				Y += V * Math.sin(angle) * delta * (-1.0);
				angle += RV * delta;
				double threshold = GameLib.HEIGHT * 0.30;
				if (previousY < threshold && Y >= threshold) {
					if (X < GameLib.WIDTH / 2)
						RV = 0.003;
					else
						RV = -0.003;
				}
				if (RV > 0 && Math.abs(angle - 3 * Math.PI) < 0.05) {
					RV = 0.0;
					angle = 3 * Math.PI;
					shootNow = true;
				}

				if (RV < 0 && Math.abs(angle) < 0.05) {
					RV = 0.0;
					angle = 0.0;
					shootNow = true;
				}
			}
		}

		if (shootNow) {
			double[] angles = { Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8 };
			int[] freeArray = findFreeIndex(e_projectile, angles.length);
			for (int k = 0; k < freeArray.length; k++) {
				int free = freeArray[k];
				if (free < 200) {
					double a = angles[k] + Math.random() * Math.PI / 6 - Math.PI / 12;
					double vx = Math.cos(a);
					double vy = Math.sin(a);
					e_projectile.get(free).X = X;
					e_projectile.get(free).Y = Y;
					e_projectile.get(free).VX = vx * 0.30;
					e_projectile.get(free).VY = vy * 0.30;
					e_projectile.get(free).state = 1;
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
			GameLib.enemy1(X, Y, radius, "enemies2.png");
		}

	}

	@Override
	public void collision (long currentTime, double dist) {
		state = EXPLODING;
		explosion_start = currentTime;
		explosion_end = currentTime + 500;
	}

}
