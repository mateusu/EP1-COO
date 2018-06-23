import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Config {
	int life;
	ArrayList<Integer> lifeboss, lifeboss2, spawnboss1, spawnboss2, spawn1, spawn2, powerup1, powerup2,
	nEnemy1, nEnemy2, nPowerUP1, nPowerUP2;
	int nstages, nE1, nE2, nP1, nP2;
	ArrayList<Double> X1, X2, Y1, Y2;
	ArrayList<Double> enemy1X, enemy1Y, enemy2X, enemy2Y, powerup1X, powerup1Y, powerup2X, powerup2Y;
	int nElements = 5;

	public Config() throws IOException {
		leArq();
	}

	public void leArq() throws IOException {
		String linha = "";
		String linha2 = "";
		int i = 0, j = 0;
		String[] config = null;
		String arquivo = "Config.txt";
		BufferedReader buffRead = new BufferedReader(new FileReader(arquivo));

		linha = buffRead.readLine();
		life = Integer.parseInt(linha);
		linha = buffRead.readLine();
		nstages = Integer.parseInt(linha);
		linha = buffRead.readLine();

		init();

		String[] names = new String[nstages];
		for (int k = 0; k < nstages; k++) {
			if (linha != null) {
				names[k] = linha;
				linha = buffRead.readLine();

			} else
				break;
		}

		while (i < nstages) {
			buffRead = new BufferedReader(new FileReader(names[i]));
			while(buffRead.ready()) {
				if (linha2 != null) {
					linha2 = buffRead.readLine();
					config = linha2.split(" ");
					leEntrada(config);
				} else
					break;
			}
			nEnemy1.add(nE1);
			nEnemy2.add(nE2);
			nPowerUP1.add(nP1);
			nPowerUP2.add(nP2);
			nE1 = nE2 = nP1 = nP2 = 0;
			i++;
		}

		/*
		 * while (true) { if (linha != null) { config = linha.split(" ");
		 * leEntrada(i, config); } else break;
		 * 
		 * linha = buffRead.readLine(); j++; if (j == nElements) i++; }
		 * buffRead.close();
		 */
	}

	private void init() {
		X1 = new ArrayList<>();
		X2 = new ArrayList<>();
		Y1 = new ArrayList<>();
		Y2 = new ArrayList<>();
		enemy1X = new ArrayList<>();
		enemy1Y = new ArrayList<>();
		enemy2X = new ArrayList<>();
		enemy2Y = new ArrayList<>();
		powerup1X = new ArrayList<>();
		powerup1Y = new ArrayList<>();
		powerup2X = new ArrayList<>();
		powerup2Y = new ArrayList<>();
		lifeboss = new ArrayList<>();
		lifeboss2 = new ArrayList<>();
		spawnboss1 = new ArrayList<>();
		spawnboss2 = new ArrayList<>();
		spawn1 = new ArrayList<>();
		spawn2 = new ArrayList<>();
		powerup1 = new ArrayList<>();
		powerup2 = new ArrayList<>();
		nEnemy1 = new ArrayList<>();
		nEnemy2 = new ArrayList<>();
		nPowerUP1 = new ArrayList<>();
		nPowerUP2 = new ArrayList<>();
		nE1 = nE2 = nP1 = nP2 = 0;
		
	}

	public void leEntrada(String[] config) {
		String enemy = config[0];
		int type = Integer.parseInt(config[1]);
		if (enemy.contains("CHEFE")) {
			if (type == 1) {
				lifeboss.add(Integer.parseInt(config[2]));
				lifeboss2.add(-1);
				spawnboss1.add(Integer.parseInt(config[3]));
				spawnboss2.add(-1);

				X1.add(Double.parseDouble(config[4]));
				X2.add(-0.1);
				Y1.add(Double.parseDouble(config[5]));
				Y2.add(-0.1);
			} else {
				lifeboss2.add(Integer.parseInt(config[2]));
				lifeboss.add(-1);
				spawnboss2.add(Integer.parseInt(config[3]));
				spawnboss1.add(-1);

				X2.add(Double.parseDouble(config[4]));
				X1.add(-0.1);
				Y2.add(Double.parseDouble(config[5]));
				Y1.add(-0.1);
			}
		} else if (enemy.contains("POWERUP")) {
			if (type == 1) {
				powerup1.add(Integer.parseInt(config[2]));
				powerup1X.add(Double.parseDouble(config[3]));
				powerup1Y.add(Double.parseDouble(config[4]));
				nP1++;
			} else {
				powerup2.add(Integer.parseInt(config[2]));
				powerup2X.add(Double.parseDouble(config[3]));
				powerup2Y.add(Double.parseDouble(config[4]));
				nP2++;
			}

		} else {
			if (type == 1) {
				spawn1.add(Integer.parseInt(config[2]));
				enemy1X.add(Double.parseDouble(config[3]));
				enemy1Y.add(Double.parseDouble(config[4]));
				nE1++;
			} else {
				spawn2.add(Integer.parseInt(config[2]));
				enemy2X.add(Double.parseDouble(config[3]));
				enemy2Y.add(Double.parseDouble(config[4]));
				nE2++;
			}
		}
	}

}
