import java.awt.Color;

public class ProjectileP extends Projectile {

	public void drawBoosted() {
		if (state == ACTIVE) {
			GameLib.setColor(Color.RED);
			GameLib.drawLine(X-5, Y-20, X-5, Y+20);
			GameLib.drawLine(X, Y-20, X, Y+20);
			GameLib.drawLine(X+5, Y-20 , X+5, Y+20);
		}
	}

	@Override
	public void draw() {
		if (state == ACTIVE) {
			GameLib.setColor(Color.GREEN);
			GameLib.drawLine(X, Y - 5, X, Y + 5);
			GameLib.drawLine(X - 1, Y - 3, X - 1, Y + 3);
			GameLib.drawLine(X + 1, Y - 3, X + 1,Y + 3);
		}
	}

	@Override
	public void movement(long delta) {
		if (state == ACTIVE) {
			if (Y < -100) {
				state = INACTIVE;
			} else {
				X += VX * delta;
				Y += VY * delta;
			}
		}
	}
}
