import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

/***********************************************************************/
/*                                                                     */
/* Classe com métodos úteis para implementação de um jogo. Inclui:     */
/*                                                                     */
/* - Método para iniciar modo gráfico.                                 */
/*                                                                     */
/* - Métodos para desenhos de formas geométricas.                      */
/*                                                                     */
/* - Método para atualizar o display.                                  */
/*                                                                     */
/* - Método para verificar o estado (pressionada ou não pressionada)   */
/*   das teclas usadas no jogo:                                        */
/*                                                                     */
/*   	- up, down, left, right: movimentação do player.               */
/*		- control: disparo de projéteis.                               */
/*		- ESC: para sair do jogo.                                      */
/*                                                                     */
/***********************************************************************/

public class GameLib {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;
	public static final int KEY_UP = 0;
	public static final int KEY_DOWN = 1;
	public static final int KEY_LEFT = 2;
	public static final int KEY_RIGHT = 3;
	public static final int KEY_CONTROL = 4;
	public static final int KEY_ESCAPE = 5;

	private static MyFrame frame = null;
	protected static Graphics g = null;
	private static MyKeyAdapter keyboard = null;

	public static void initGraphics(){

		frame = new MyFrame("Projeto COO");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);

		keyboard = new MyKeyAdapter();
		frame.addKeyListener(keyboard);
		frame.requestFocus();

		frame.createBufferStrategy(2);		
		g = frame.getBufferStrategy().getDrawGraphics();
	}

	public static void setColor(Color c){

		g.setColor(c);
	}

	public static void drawLine(double x1, double y1, double x2, double y2){

		g.drawLine((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
	}

	public static void drawCircle(double cx, double cy, double radius){

		int x = (int) Math.round(cx - radius);
		int y = (int) Math.round(cy - radius);
		int width = (int) Math.round(2 * radius);
		int height = (int) Math.round(2 * radius);

		g.drawOval(x, y, width, height);
	}

	public static void message(double cx, double cy, double radius, String nome){
		Image img1 = Toolkit.getDefaultToolkit().getImage(nome);
		g.drawImage(img1, (int)cx - (img1.getWidth(frame)/2),(int)cy-10, frame);	
		int x = (int) Math.round(cx - radius);
		int y = (int) Math.round(cy - radius);
		int width = (int) Math.round(2 * radius);
		int height = (int) Math.round(2 * radius);
	}
	public static void enemy1(double cx, double cy, double radius, String nome){

		Image img1 = Toolkit.getDefaultToolkit().getImage(nome);
		g.drawImage(img1, (int)cx - (img1.getWidth(frame)/2),(int)cy-10, frame);

		int x = (int) Math.round(cx - radius);
		int y = (int) Math.round(cy - radius);
		int width = (int) Math.round(2 * radius);
		int height = (int) Math.round(2 * radius);
	}

	public static void boss(double cx, double cy, double radius, String nome,double life_atual, double life_ini){

		int w = 200;
		int h = 10;

		Image img1 = Toolkit.getDefaultToolkit().getImage(nome);
		double percent = life_atual/life_ini;
		GameLib.setColor(Color.gray);
		GameLib.drawSquarelife(percent,(int)cx-98,(int)cy-115, w, h);
		GameLib.life(percent,(int)cx-98,(int)cy-115, w, h);

		g.drawImage(img1, (int)cx-95 - (img1.getWidth(frame)/10),(int)cy-100, frame);

		int x = (int) Math.round(cx - radius);
		int y = (int) Math.round(cy - radius);
		int width = (int) Math.round(2 * radius);
		int height = (int) Math.round(2 * radius);

	}

	public static void drawDiamond(double x, double y, double radius){

		int x1 = (int) Math.round(x);
		int y1 = (int) Math.round(y - radius);
		int x2 = (int) Math.round(x + radius);
		int y2 = (int) Math.round(y);

		int x3 = (int) Math.round(x);
		int y3 = (int) Math.round(y + radius);

		int x4 = (int) Math.round(x - radius);
		int y4 = (int) Math.round(y);

		drawLine(x1, y1, x2, y2);
		drawLine(x2, y2, x3, y3);
		drawLine(x3, y3, x4, y4);
		drawLine(x4, y4, x1, y1);
	}

	public static void drawPlayer(double player_X, double player_Y, double player_size, double life_atual, double life_ini){
		double percent = life_atual/life_ini;
		int x = 20;
		int y = 680;
		int w = 150;
		int h = 20;
		GameLib.setColor(Color.gray);
		GameLib.drawSquarelife(percent, x, y, w, h);
		GameLib.life(percent, x, y, w, h);

		Image img1 = Toolkit.getDefaultToolkit().getImage("image_ajst2.png");
		g.drawImage(img1, (int)player_X - (img1.getWidth(frame)/2),(int)player_Y-21, frame);

	}

	public static void drawBoost(double player_X, double player_Y, double player_size, String nome){

		Image img_boost = Toolkit.getDefaultToolkit().getImage(nome);
		g.drawImage(img_boost, (int)player_X - (img_boost.getWidth(frame)/2),(int)player_Y, frame);

	}

	public static void drawExplosion(double x, double y, double alpha){

		int p = 5;
		int r = (int) (255 - Math.pow(alpha, p) * 255);
		int g = (int) (128 - Math.pow(alpha, p) * 128);
		int b = 0;

		GameLib.setColor(new Color(r, g, b));
		GameLib.drawCircle(x, y, alpha * alpha * 40);
		GameLib.drawCircle(x, y, alpha * alpha * 40 + 1);
	}

	//barra de life
	public static void life(double percent, int x, int y, int width,int height){
		if(percent>0.7){
			GameLib.setColor(Color.green);
		}else if(percent>0.5){
			GameLib.setColor(Color.YELLOW);
		}else if(percent>0.3){
			GameLib.setColor(Color.orange);
		}else{
			GameLib.setColor(Color.red);
		}

		width = (int) (width*(percent));
		g.fillRect(x, y, width, height);
	}

	public static void lifeEsc(double percent, int x, int y, int width,int height){
		if(percent>0.7){
			GameLib.setColor(Color.blue);
		}else if(percent>0.5){
			GameLib.setColor(Color.blue);
		}else if(percent>0.3){
			GameLib.setColor(Color.blue);
		}else{
			GameLib.setColor(Color.red);
		}
		width = (int) (width*(percent));
		g.fillRect(x, y, width, height);
	}

	public static void Escudo(double percent){
		GameLib.setColor(Color.BLUE);
		int x = 20;
		int y = 650;
		int width = (int) (150*(percent));
		int height = 20;
		g.fillRect(x, y, width, height);
	}

	public static void lifeInt(int x){
		String s = Integer.toString(x);
		GameLib.setColor(Color.blue);
		GameLib.g.drawString(s, 20, 695);
	}

	public static void lifeEscInt(int x){
		String s = Integer.toString(x);
		GameLib.setColor(Color.RED);
		GameLib.g.drawString(s, 25, 665);
	}


	public static void drawSquarelife(double percent,int x ,int y, int width, int height){
		g.drawRect(x, y, width, height);
		g.drawRect(x, y,(int) (width*(percent)), height);
	}



	public static void fillRect(double cx, double cy, double width, double height){

		int x = (int) Math.round(cx - width/2);
		int y = (int) Math.round(cy - height/2);

		g.fillRect(x, y, (int) Math.round(width), (int) Math.round(height));
	}

	public static void display(){

		g.dispose();
		frame.getBufferStrategy().show();
		Toolkit.getDefaultToolkit().sync();
		g = frame.getBufferStrategy().getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth() - 1, frame.getHeight() - 1);
		g.setColor(Color.WHITE);
	}

	public static boolean iskeyPressed(int index){

		return keyboard.isKeyPressed(index);
	}

	public static void debugKeys(){

		keyboard.debug();
	}
}

@SuppressWarnings("serial")
class MyFrame extends JFrame {

	public MyFrame(String title){

		super(title);
	}

	public void paint(Graphics g){ }

	public void update(Graphics g){ }

	public void repaint(){ }
}

class MyKeyAdapter extends KeyAdapter{

	private int [] codes = {	KeyEvent.VK_UP,
			KeyEvent.VK_DOWN,
			KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT, 
			KeyEvent.VK_CONTROL,
			KeyEvent.VK_ESCAPE
	};

	private boolean [] keyStates = null;
	private long [] releaseTimeStamps = null;

	public MyKeyAdapter(){

		keyStates = new boolean[codes.length];
		releaseTimeStamps = new long[codes.length];
	}

	public int getIndexFromKeyCode(int keyCode){

		for(int i = 0; i < codes.length; i++){

			if(codes[i] == keyCode) return i;
		}

		return -1;
	}

	public void keyPressed(KeyEvent e){

		int index = getIndexFromKeyCode(e.getKeyCode());

		if(index >= 0){

			keyStates[index] = true;
		}
	}

	public void keyReleased(KeyEvent e){

		int index = getIndexFromKeyCode(e.getKeyCode());

		if(index >= 0){

			keyStates[index] = false;
			releaseTimeStamps[index] = System.currentTimeMillis();
		}
	}

	public boolean isKeyPressed(int index){

		boolean keyState = keyStates[index];
		long keyReleaseTime = releaseTimeStamps[index];

		if(keyState == false){

			if(System.currentTimeMillis() - keyReleaseTime > 5) return false;
		}

		return true;		
	}

	public void debug(){

		System.out.print("Key states = {");

		for(int i = 0; i < codes.length; i++){

			System.out.print(" " + keyStates[i] + (i < (codes.length - 1) ? "," : ""));
		}

		System.out.println(" }");
	}
}


