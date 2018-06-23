import java.util.ArrayList;

public class Message extends Ship {

	public Message(){
		X = GameLib.WIDTH /2;
		Y = -20.0;
		V = 0.10 + Math.random() * 0.15;
		angle = 3 * Math.PI / 2;
	}

	@Override
	public void draw(long currentTime) {
		if (state == ACTIVE) {
			GameLib.message(X, Y, radius, "stageclear.png");
		}
	}

	@Override
	public void collision(long currentTime, double dist) {
	}

	@Override
	public void movement(long currentTime, long delta, ArrayList<Projectile> e_projectile, double player_Y) {
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
}