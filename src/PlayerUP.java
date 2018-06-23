import java.awt.Color;
import java.util.ArrayList;

public class PlayerUP extends Player{
	int life_ini = 300;
	int life_atual = life_ini;
	Ship ship;

	public PlayerUP(Ship s){
		this.radius = s.radius*4;
		this.ship = s;
		this.life_ini = ship.life;
	}


	double percent = life_atual/life_ini;

	@Override
	public void draw(long currentTime) {
		ship.draw(currentTime);
		GameLib.lifeEsc(percent, 20, 650, 150, 20);
		GameLib.g.drawOval((int)ship.X-50,(int)ship.Y-35, 100, 100);
		GameLib.Escudo(1.0);
		GameLib.lifeEscInt(life_atual);
	}

	@Override
	public void collision(long currentTime, double dist) {
	}

	@Override
	public void movement(long currentTime, long delta, ArrayList<Projectile> e_projectile, double player_Y) {
		ship.movement(currentTime, delta, e_projectile, player_Y);

		if (GameLib.iskeyPressed(GameLib.KEY_UP))
			this.Y -= delta * VY;
		if (GameLib.iskeyPressed(GameLib.KEY_DOWN))
			this.Y += delta * VY;
		if (GameLib.iskeyPressed(GameLib.KEY_LEFT))
			this.X -= delta * VX;
		if (GameLib.iskeyPressed(GameLib.KEY_RIGHT))
			this.X += delta * VY;
	}


	protected int GetEscudo(){
		return life_atual;
	}

	public void losing_shield(){
		this.life_atual--;
	}

}


