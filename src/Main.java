import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

/*Projeto de COO
 * Matheus de Oliveira Leu - 8802621 (T04)
 * Henrique Feo - 9424090 (T04)
 * Lucas Borelli - 9360951 (T04)
 * Silas Rocha - 9424079 (T94)
 * Lucas Oliveira - 9424016 (T94)
 * 
 */
public class Main {

	/* Constantes relacionadas aos estados que os elementos */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */

	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	static int nEnemy1 = 10;
	static int nEnemy2 = 10;
	static int nEProjectiles = 200;
	static int nPProjectiles = 10;
	static int nPowerUP1 = 5;
	static int nPowerUP2 = 5;
	static int stageatual;
	static int nextstage;
	static int lifePlayer;
	static int nstages;

	public static void busyWait(long time) {

		while (System.currentTimeMillis() < time)
			Thread.yield();
	}

	public static int findFreeIndex(ArrayList<Ship> enemies) {
		int i;
		for (i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).state == INACTIVE)
				break;
		}

		return i;
	}

	/* Método principal */

	public static void main(String[] args) throws IOException {
		/* Inicializações de stages */
		stageatual = 1;
		nextstage = 1;
		Config config = new Config();
		nstages = config.nstages;

		/* Indica se a fase tem tal boss */
		boolean boss1 = false;
		boolean boss2 = false;

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variáveis do player */
		Ship player;
		player = new Player();
		((Player) player).life_ini = config.life;
		player.collision(0, 0);

		((Player) player).setNextShot(currentTime); // instante a partir do qual
		// pode haver um próximo tiro

		/* variáveis dos projéteis disparados pelo player */
		ArrayList<Projectile> p_projectile = new ArrayList<Projectile>();

		/* Variáveis dos PowerUps */
		ArrayList<Ship> PowerUp = new ArrayList<Ship>();
		long nextPowerUp = 0;
		ArrayList<Ship> PowerUp2 = new ArrayList<Ship>();
		long nextPowerUp2 = 0;

		/* Variáveis dos Bosses */
		ArrayList<Ship> Boss1 = new ArrayList<Ship>();
		long nextBoss1 = 0;
		ArrayList<Ship> Boss2 = new ArrayList<Ship>();
		long nextBoss2 = 0;

		/* variáveis dos inimigos tipo 1 */

		ArrayList<Ship> Enemies1 = new ArrayList<Ship>();
		long nextEnemy1 = 0;

		/* variáveis dos inimigos tipo 2 */
		ArrayList<Ship> Enemies2 = new ArrayList<Ship>();
		long nextEnemy2 = 0;
		// inimigo
		double enemy2_spawnX = GameLib.WIDTH * 0.20; // coordenada x do próximo
		// inimigo tipo 2 a
		// aparecer
		int enemy2_count = 0; // contagem de inimigos tipo 2 (usada na formação
								// de voo")

		/*
		 * variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto
		 * tipo 2)
		 */

		ArrayList<Projectile> e_projectile = new ArrayList<Projectile>();

		/* Variáveis dos projéteis lançados pelos bosses */
		ArrayList<Projectile> b_projectile = new ArrayList<Projectile>();

		/* estrelas que formam o fundo de primeiro plano */
		double[] background1_X = new double[20];
		double[] background1_Y = new double[20];
		double background1_speed = 0.070;
		double background1_count = 0.0;

		/* estrelas que formam o fundo de segundo plano */
		double[] background2_X = new double[50];
		double[] background2_Y = new double[50];
		double background2_speed = 0.045;
		double background2_count = 0.0;

		/* inicializações */
		int aux = 0;
		for (int i : config.nPowerUP1)
			aux += i;
		nPowerUP1 = aux;

		for (int i = 0; i < nPowerUP1; i++) {
			PowerUp.add(new PowerUp());
		}
		aux = 0;

		for (int i : config.nPowerUP2)
			aux += i;

		nPowerUP2 = aux;
		for (int i = 0; i < nPowerUP2; i++) {
			PowerUp2.add(new PowerUp2());

		}

		Boss1.add(new Boss1());
		Boss2.add(new Boss2());
		Ship message = new Message();

		/* Inicializações dos PowerUps */
		boolean laser = false;

		/* Inicializações dos Projéteis */

		for (int i = 0; i < nPProjectiles; i++) {
			p_projectile.add(new ProjectileP());
			p_projectile.get(i).state = INACTIVE;
		}
		for (int i = 0; i < nEProjectiles; i++) {
			e_projectile.add(new ProjectileE());
			e_projectile.get(i).state = INACTIVE;
		}
		for (int i = 0; i < nEProjectiles; i++) {
			b_projectile.add(new ProjectileB());
			b_projectile.get(i).state = INACTIVE;
		}

		/* Inicializações dos inimigos */
		aux = 0;

		for (int i : config.nEnemy1)
			aux += i;
		nEnemy1 = aux;

		for (int i = 0; i < nEnemy1; i++) {
			Enemies1.add(new Enemy1());
			Enemies1.get(i).state = INACTIVE;
		}

		aux = 0;
		for (int i : config.nEnemy2)
			aux += i;
		nEnemy2 = aux;

		for (int i = 0; i < nEnemy2; i++) {
			Enemies2.add(new Enemy2());
			Enemies2.get(i).state = INACTIVE;
		}

		/* Inicialização do background */

		for (int i = 0; i < background1_X.length; i++) {
			background1_X[i] = Math.random() * GameLib.WIDTH;
			background1_Y[i] = Math.random() * GameLib.HEIGHT;
		}

		for (int i = 0; i < background2_X.length; i++) {
			background2_X[i] = Math.random() * GameLib.WIDTH;
			background2_Y[i] = Math.random() * GameLib.HEIGHT;
		}

		/* iniciado interface gráfica */

		/*for (int i : config.nPowerUP2) {
			System.out.println(config.nPowerUP2);
		}
		*/
		GameLib.initGraphics();

		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo */
		/*                                                                                               */
		/* O main loop do jogo possui executa as seguintes operações: */
		/*                                                                                               */
		/*
		 * 1) Verifica se há colisões e atualiza estados dos elementos conforme
		 * a necessidade.
		 */
		/*                                                                                               */
		/*
		 * 2) Atualiza estados dos elementos baseados no tempo que correu desde
		 * a última atualização
		 */
		/*
		 * e no timestamp atual: posição e orientação, execução de disparos de
		 * projéteis, etc.
		 */
		/*                                                                                               */
		/*
		 * 3) Processa entrada do usuário (teclado) e atualiza estados do player
		 * conforme a necessidade.
		 */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos. */
		/*                                                                                               */
		/*
		 * 5) Espera um período de tempo (de modo que delta seja anextimadamente
		 * sempre constante).
		 */
		/*                                                                                               */
		/*************************************************************************************************/

		while (running) {
			/* Inicia fase seguinte */
			if (stageatual == nextstage) {
				if (stageatual > nstages) {
					System.out.println("Parabéns, você zerou o jogo!");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
				if (config.lifeboss.get(stageatual - 1) != -1) {
					boss1 = true;
					nextBoss1 = currentTime + config.spawnboss1.get(stageatual - 1);
					((Boss1) Boss1.get(0)).setlife(config.lifeboss.get(stageatual - 1));
				}
				if (config.lifeboss2.get(stageatual - 1) != -1) {
					boss2 = true;
					nextBoss2 = currentTime + config.spawnboss2.get(stageatual - 1);
					((Boss2) Boss2.get(0)).setlife(config.lifeboss2.get(stageatual - 1));
				}

				if (stageatual > 1) {
					for (int i = 0; i < nEnemy1; i++) {
						config.enemy1X.remove(0);
						config.enemy1Y.remove(0);
						config.spawn1.remove(0);
						Enemies1.remove(0);
					}
					for (int i = 0; i < nEnemy2; i++) {
						config.enemy2X.remove(0);
						config.enemy2Y.remove(0);
						config.spawn2.remove(0);
						Enemies2.remove(0);

					}
					for (int i = 0; i < nPowerUP1; i++) {
						config.powerup1X.remove(0);
						config.powerup1Y.remove(0);
						config.powerup1.remove(0);
						PowerUp.remove(0);

					}
					for (int i = 0; i < nPowerUP2; i++) {
						config.powerup2X.remove(0);
						config.powerup2Y.remove(0);
						config.powerup2.remove(0);
						PowerUp2.remove(0);
					}

				}

				nEnemy1 = config.nEnemy1.get(stageatual - 1);
				nEnemy2 = config.nEnemy2.get(stageatual - 1);
				nPowerUP1 = config.nPowerUP1.get(stageatual - 1);
				nPowerUP2 = config.nPowerUP2.get(stageatual - 1);
				/*
				 * nextPowerUp = currentTime + config.powerup1.get(stageatual -
				 * 1); nextPowerUp2 = currentTime +
				 * config.powerup2.get(stageatual - 1); nextEnemy1 = currentTime
				 * + config.spawn1.get(stageatual - 1); nextEnemy2 = currentTime
				 * + config.spawn2.get(stageatual - 1);
				 */
				nextstage++;

			}

			/* Usada para atualizar o estado dos elementos do jogo */
			/* (player, projéteis e inimigos) "delta" indica quantos */
			/* ms se passaram desde a última atualização. */

			delta = System.currentTimeMillis() - currentTime;

			/* Já a variável "currentTime" nos dá o timestamp atual. */

			currentTime = System.currentTimeMillis();

			/***************************/
			/* Verificação de colisões */
			/***************************/

			if (player.state == EXPLODING) {
				player.VX = 0.25;
				player.VY = 0.25;
				player.radius = 19.0;
				laser = false;
			}
			if (player.state == ACTIVE) {

				// Colisao do Player com projeteis inimigos
				for (int i = 0; i < nEProjectiles; i++) {
					double dx = e_projectile.get(i).X - player.X;
					double dy = e_projectile.get(i).Y - player.Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					if (player instanceof PlayerUP) {
						if (dist < (((PlayerUP) player).radius + e_projectile.get(0).radius) * 0.8) {
							if (((PlayerUP) player).life_atual > 0) {
								((PlayerUP) player).losing_shield();
							} else {
								player = new Player();
								((Player) player).life_ini = config.life;
								player.collision(0, 0);
							}
						}
					} else

					if (dist < (player.radius + e_projectile.get(0).radius) * 0.8) {
						if (((Player) player).life_atual > 0) {
							((Player) player).losing_life();
						} else {
							player.collision(currentTime, dist);

						}
					}
				}

			}

			// Colisão com projetil dos bosses
			
			int aux2 = nEProjectiles;
			for (int i = 0; i < aux2; i++) {
				double dx = b_projectile.get(i).X - player.X;
				double dy = b_projectile.get(i).Y - player.Y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				if (player instanceof PlayerUP) {
					if (dist < (((PlayerUP) player).radius + b_projectile.get(0).radius) * 0.8) {
						if (((PlayerUP) player).life_atual > 0) {
							((PlayerUP) player).losing_shield();
						} else {
							player = new Player();
							((Player) player).life_ini = config.life;
							player.collision(0, 0);
						}
					}
				} else

				if (dist < (player.radius + b_projectile.get(0).radius) * 0.8) {
					if (((Player) player).life_atual > 0) {
						((Player) player).losing_life();
					} else {
						player.collision(currentTime, dist);

					}
				}
			}

			// colisao com power ups
			for (int i = 0; i < nPowerUP1; i++) {
				double dx = PowerUp.get(i).X - player.X;
				double dy = PowerUp.get(i).Y - player.Y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				if (dist < (player.radius + PowerUp.get(i).radius) * 0.8) {
					if (player instanceof PlayerUP) {

					} else
						player = new PlayerUP(player);
					((PlayerUP) player).life_atual = config.life * 2;
					PowerUp.get(i).collision(currentTime, dist);
					PowerUp.get(i).state = INACTIVE;
					PowerUp.get(i).state = ACTIVE;
				}
			}

			for (int i = 0; i < nPowerUP2; i++) {
				double dx = PowerUp2.get(i).X - player.X;
				double dy = PowerUp2.get(i).Y - player.Y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				if (dist < (player.radius + PowerUp2.get(i).radius) * 0.8) {
					PowerUp2.get(i).collision(currentTime, dist);
					player.powerUp(player);
					laser = true;

				}
			}

			/* colisões player - inimigos */

			// Colisão com boss1
			for (int i = 0; i < Boss1.size(); i++) {
				if (boss1) {
					double dx = Boss1.get(0).X - player.X;
					double dy = Boss1.get(0).Y - player.Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					if (dist < (player.radius + Boss1.get(0).radius) * 0.8) {
						player.collision(currentTime, dist);
					}
				}
			}
			// Colisão com boss2
			for (int i = 0; i < Boss2.size(); i++) {
				if (boss2) {
					double dx = Boss2.get(0).X - player.X;
					double dy = Boss2.get(0).Y - player.Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					if (dist < (player.radius + Boss2.get(0).radius) * 0.8) {
						player.collision(currentTime, dist);
					}
				}
			}

			// colisão com inimigo 1
			for (int i = 0; i < nEnemy1; i++) {
				double dx = Enemies1.get(i).X - player.X;
				double dy = Enemies1.get(i).Y - player.Y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				if (player instanceof PlayerUP) {
					if (dist < (((PlayerUP) player).radius * 2 + Enemies1.get(i).radius) * 0.8) {
						Enemies1.get(i).state = INACTIVE;
					}
				} else {
					if (dist < ((player).radius + Enemies1.get(i).radius) * 0.8) {
						player.collision(currentTime, dist);
					}
				}

			}

			// colisao com inimigo 2
			for (int i = 0; i < nEnemy2; i++) {
				double dx = Enemies2.get(i).X - player.X;
				double dy = Enemies2.get(i).Y - player.Y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				if (player instanceof PlayerUP) {
					if (dist < (((PlayerUP) player).radius * 2 + Enemies2.get(i).radius) * 0.8) {
						Enemies2.get(i).state = INACTIVE;
					}
				} else {
					if (dist < ((player).radius + Enemies2.get(i).radius) * 0.8) {
						player.collision(currentTime, dist);
					}
				}
			}

			/* colisões projeteis (player) - inimigos */

			for (int k = 0; k < nPProjectiles; k++) {
				if (boss1) {
					if (Boss1.get(0).state == ACTIVE) {
						double dx = Boss1.get(0).X - p_projectile.get(k).X;
						double dy = Boss1.get(0).Y - p_projectile.get(k).Y;
						double dist = Math.sqrt(dx * dx + dy * dy);
						if (!laser) {
							if (dist < Boss1.get(0).radius) {
								Boss1.get(0).collision(currentTime, dist);
								if (Boss1.get(0).state == EXPLODING) {
									boss1 = false;
									stageatual++;
									message = new Message();
									message.state = ACTIVE;
								}
							}
						} else {
							if (dist < Boss1.get(0).radius * 2.2) {
								Boss1.get(0).collision(currentTime, dist);
								if (Boss1.get(0).state == EXPLODING) {
									boss1 = false;
									stageatual++;
									message = new Message();
									message.state = ACTIVE;

								}
							}
						}
					}
				}
				if (boss2) {
					if (Boss2.get(0).state == ACTIVE) {
						double dx = Boss2.get(0).X - p_projectile.get(k).X;
						double dy = Boss2.get(0).Y - p_projectile.get(k).Y;
						double dist = Math.sqrt(dx * dx + dy * dy);

						if (!laser) {
							if (dist < Boss2.get(0).radius) {
								Boss2.get(0).collision(currentTime, dist);
								if (Boss2.get(0).state == EXPLODING) {
									boss2 = false;
									stageatual++;
									message = new Message();
									message.state = ACTIVE;

								}
							}
						} else {
							if (dist < Boss2.get(0).radius * 2.2) {
								Boss2.get(0).collision(currentTime, dist);
								if (Boss2.get(0).state == EXPLODING) {
									boss2 = false;
									stageatual++;
									message = new Message();
									message.state = ACTIVE;

								}
							}
						}

					}
				}
				for (int i = 0; i < nEnemy1; i++) {

					if (Enemies1.get(i).state == ACTIVE) {
						double dx = Enemies1.get(i).X - p_projectile.get(k).X;
						double dy = Enemies1.get(i).Y - p_projectile.get(k).Y;
						double dist = Math.sqrt(dx * dx + dy * dy);
						if (!laser) {
							if (dist < Enemies1.get(i).radius) {
								Enemies1.get(i).collision(currentTime, dist);
							}
						} else {
							if (dist < Enemies1.get(i).radius * 3) {
								Enemies1.get(i).collision(currentTime, dist);
							}
						}
					}
				}

				for (int i = 0; i < nEnemy2; i++) {
					if (Enemies2.get(i).state == ACTIVE) {
						double dx = Enemies2.get(i).X - p_projectile.get(k).X;
						double dy = Enemies2.get(i).Y - p_projectile.get(k).Y;
						double dist = Math.sqrt(dx * dx + dy * dy);
						if (!laser) {
							if (dist < Enemies2.get(i).radius) {
								Enemies2.get(i).collision(currentTime, dist);
							}
						} else {
							if (dist < Enemies2.get(i).radius * 3) {
								Enemies2.get(i).collision(currentTime, dist);
							}
						}
					}
				}
			}

			/***************************/
			/* Atualizações de estados */
			/***************************/

			/* projeteis (player) */

			for (int i = 0; i < nPProjectiles; i++) {
				p_projectile.get(i).movement(delta);
			}

			/* projeteis (inimigos) */

			for (int i = 0; i < nEProjectiles; i++) {
				e_projectile.get(i).movement(delta);
			}

			/* Projeteis bosses */

			for (int i = 0; i < nEProjectiles; i++) {
				b_projectile.get(i).movement(delta);
			}

			/* Mensagem */
			message.movement(currentTime, delta, e_projectile, player.Y);

			/* PowerUps */
			for (int i = 0; i < PowerUp.size(); i++) {
				PowerUp.get(i).movement(currentTime, delta, e_projectile, player.Y);
			}

			for (int i = 0; i < PowerUp2.size(); i++) {
				PowerUp2.get(i).movement(currentTime, delta, e_projectile, player.Y);
			}

			/* Bosses */
			Boss1.get(0).movement(currentTime, delta, b_projectile, player.Y);
			Boss2.get(0).movement(currentTime, delta, b_projectile, player.Y);

			/* inimigos tipo 1 */
			for (int i = 0; i < nEnemy1; i++) {
				Enemies1.get(i).movement(currentTime, delta, e_projectile, player.Y);
			}

			/* inimigos tipo 2 */
			for (int i = 0; i < nEnemy2; i++) {
				Enemies2.get(i).movement(currentTime, delta, e_projectile, player.Y);
			}

			/* Lançamento de PowerUps */
			if (currentTime > nextPowerUp) {
				int free = findFreeIndex(PowerUp);
				if (free < nPowerUP1) {
					PowerUp.get(free).X = config.powerup1X.get(free);
					PowerUp.get(free).Y = config.powerup1Y.get(free);
					PowerUp.get(free).V = 0.20 + Math.random() * 0.15;
					PowerUp.get(free).angle = 3 * Math.PI / 2;
					PowerUp.get(free).RV = 0.0;
					PowerUp.get(free).state = ACTIVE;
					nextPowerUp = currentTime + config.powerup1.get(free);
				}
			}

			if (currentTime > nextPowerUp2) {

				int free = findFreeIndex(PowerUp2);

				if (free < nPowerUP2) {

					PowerUp2.get(free).X = config.powerup2X.get(free);
					PowerUp2.get(free).Y = config.powerup2Y.get(free);
					PowerUp2.get(free).V = 0.20 + Math.random() * 0.15;
					PowerUp2.get(free).angle = 3 * Math.PI / 2;
					PowerUp2.get(free).RV = 0.0;
					PowerUp2.get(free).state = ACTIVE;
					nextPowerUp2 = currentTime + config.powerup2.get(free);
				}
			}

			/* Lançamento de Bosses */
			if (currentTime > nextBoss1 && boss1) {

				int free = findFreeIndex(Boss1);
				if (free < Boss1.size()) {
					if (config.X1.get(stageatual - 1) == 0)
						Boss1.get(free).X = 220;
					else
						Boss1.get(free).X = config.X1.get(stageatual - 1);

					if (config.Y1.get(stageatual - 1) == 0)
						Boss1.get(free).Y = -10.0;
					else
						Boss1.get(free).Y = config.Y1.get(stageatual - 1);

					Boss1.get(free).V = 0.20 + Math.random() * 0.15;
					Boss1.get(free).angle = 3 * Math.PI / 2;
					Boss1.get(free).RV = 0.0;
					Boss1.get(free).state = ACTIVE;
				}
			}

			if (currentTime > nextBoss2 && boss2) {

				int free = findFreeIndex(Boss2);
				if (free < Boss2.size()) {
					if (config.X2.get(stageatual - 1) == 0)
						Boss2.get(free).X = 220;
					else
						Boss2.get(free).X = config.X2.get(stageatual - 1);
					if (config.Y2.get(stageatual - 1) == 0)
						Boss2.get(free).Y = -10.0;
					else
						Boss2.get(free).Y = config.Y2.get(stageatual - 1);
					Boss2.get(free).V = 0.20 + Math.random() * 0.15;
					Boss2.get(free).angle = 3 * Math.PI / 2;
					Boss2.get(free).RV = 0.0;
					Boss2.get(free).state = ACTIVE;
				}
			}

			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */

			if (currentTime > nextEnemy1) {

				int free = findFreeIndex(Enemies1);

				if (free < nEnemy1) {

					Enemies1.get(free).X = config.enemy1X.get(free);
					Enemies1.get(free).Y = config.enemy1Y.get(free);
					Enemies1.get(free).V = 0.20 + Math.random() * 0.15;
					Enemies1.get(free).angle = 3 * Math.PI / 2;
					Enemies1.get(free).RV = 0.0;
					Enemies1.get(free).state = ACTIVE;
					nextEnemy1 = currentTime + config.spawn1.get(free);
				}
			}

			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */

			if (currentTime > nextEnemy2) {

				int free = findFreeIndex(Enemies2);

				if (free < nEnemy2) {

					Enemies2.get(free).X = config.enemy2X.get(free);
					Enemies2.get(free).Y = config.enemy2Y.get(free);
					Enemies2.get(free).V = 0.42;
					Enemies2.get(free).angle = (3 * Math.PI) / 2;
					Enemies2.get(free).RV = 0.0;
					Enemies2.get(free).state = ACTIVE;

					enemy2_count++;

					if (enemy2_count < 10) {

						nextEnemy2 = currentTime + 120;
					} else {

						enemy2_count = 0;
						enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
						// nextEnemy2 = (long) (currentTime + 3000 +
						// Math.random() * 3000);
						nextEnemy2 = currentTime + config.spawn2.get(free);
					}
				}
			}

			/* Verificando se a explosão do player já acabou. */
			/* Ao final da explosão, o player volta a ser controlável */
			if (player.state == EXPLODING) {
				if (currentTime > player.explosion_end) {

					player.state = ACTIVE;
				}
			}

			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/

			if (player.state == ACTIVE) {
				player.movement(currentTime, delta, p_projectile, nPProjectiles);
			}

			if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE))
				running = false;

			/* Verificando se coordenadas do player ainda estão dentro */
			/* da tela de jogo após processar entrada do usuário. */

			if (player.X < 0.0)
				player.X = 0.0;
			if (player.X >= GameLib.WIDTH)
				player.X = GameLib.WIDTH - 1;
			if (player.Y < 25.0)
				player.Y = 25.0;
			if (player.Y >= GameLib.HEIGHT)
				player.Y = GameLib.HEIGHT - 1;

			/*******************/
			/* Desenho da cena */
			/*******************/

			/* desenhando plano fundo distante */

			GameLib.setColor(Color.DARK_GRAY);
			background2_count += background2_speed * delta;

			for (int i = 0; i < background2_X.length; i++) {

				GameLib.fillRect(background2_X[i], (background2_Y[i] + background2_count) % GameLib.HEIGHT, 2, 2);
			}

			/* desenhando plano de fundo próximo */

			GameLib.setColor(Color.GRAY);
			background1_count += background1_speed * delta;

			for (int i = 0; i < background1_X.length; i++) {

				GameLib.fillRect(background1_X[i], (background1_Y[i] + background1_count) % GameLib.HEIGHT, 3, 3);
			}

			/* desenhando player */
			player.draw(currentTime);

			/* deenhando projeteis (player) */
			for (int i = 0; i < nPProjectiles; i++) {
				if (laser == true) {
					p_projectile.get(i).drawBoosted(); // nave diferente
					p_projectile.get(i).radius = 20.0; // aqui aumenta o raio
				} else {
					p_projectile.get(i).draw();
				}

			}
			/* desenhando projeteis (inimigos) */

			for (int i = 0; i < nEProjectiles; i++) {
				e_projectile.get(i).draw();

			}
			/* desenhando projeteis (bosses) */
			for (int i = 0; i < nEProjectiles; i++) {
				b_projectile.get(i).draw();

			}

			/* desenhando mensagem */
			message.draw(currentTime);
			/* desenhando PowerUps */
			for (int i = 0; i < nPowerUP1; i++) {
				PowerUp.get(i).draw(currentTime);
			}
			for (int i = 0; i < nPowerUP2; i++) {
				PowerUp2.get(i).draw(currentTime);
			}

			/* Desenhando bosses */
			Boss1.get(0).draw(currentTime);
			Boss2.get(0).draw(currentTime);

			/* desenhando inimigos (tipo 1) */

			for (int i = 0; i < nEnemy1; i++) {
				Enemies1.get(i).draw(currentTime);
			}

			/* desenhando inimigos (tipo 2) */

			for (int i = 0; i < nEnemy2; i++) {
				Enemies2.get(i).draw(currentTime);
			}

			/*
			 * chamama a display() da classe GameLib atualiza o desenho exibido
			 * pela interface do jogo.
			 */

			GameLib.display();

			/*
			 * faz uma pausa de modo que cada execução do laço do main loop
			 * demore anextimadamente 5 ms.
			 */

			busyWait(currentTime + 5);
		}

		System.exit(0);
	}
}
