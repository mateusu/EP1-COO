import java.awt.Color;

public class ProjectileE extends Projectile {

	public ProjectileE(){
		X = 0;
		Y = 0;
		VX = 0;
		VY = 0;
		this.radius = 2.0;
	}

	@Override
	public void draw() {
		if (state == ACTIVE) {
			GameLib.setColor(Color.RED);
			GameLib.drawCircle(X, Y, radius);
		}
	}

	@Override
	public void movement(long delta) {
		if (state == ACTIVE) {
			if (Y > GameLib.HEIGHT) {
				state = INACTIVE;
			} else {
				X += VX * delta;
				Y += VY * delta;
			}
		}
	}
}
