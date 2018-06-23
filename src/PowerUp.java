import java.awt.Color;
import java.util.ArrayList;

public class PowerUp extends Ship {
	public PowerUp() {
		X = 0;
		Y = 0;
		V = 0;
		angle = 0;
		RV = 0;
		this.radius = 9.0;
	}

	@Override	
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
			GameLib.setColor(Color.RED);
			GameLib.drawBoost(X, Y, radius, "power_item3.png");
		}

	}

	@Override
	public void collision(long currentTime, double dist) {
		state = EXPLODING;
		explosion_start = currentTime;
		explosion_end = currentTime + 1;
	}
}
