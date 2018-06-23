import java.awt.Color;

public class ProjectileB extends Projectile {

	public ProjectileB(){
		this.radius = 2.0;
	}

	@Override
	public void draw() {
		if (state == ACTIVE) {
			GameLib.setColor(Color.WHITE);
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
