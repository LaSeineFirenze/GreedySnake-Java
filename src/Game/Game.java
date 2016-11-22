package Game;

import java.io.*;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import Constant.*;
import Snake.*;
import Socket.*;
import Sound.*;

public class Game extends JPanel {

	Grid[][] map;
	
	Snake snake1;
	Snake snake2;

	int speed;
	int delay;
	int eggNum;

	boolean running;
	Object pause, ready;
	int result;
	
	MainFrame mainFrame;
	int playerType;
	boolean alive1, alive2;
	int direction1, direction2;
	NetSocket mine;
	EggThread eggThread;
	ReceiveThread receiveThread;
	RunThread runThread;
	
	Sound soundThread;
	
	protected Game(){
		soundThread = new Sound();
		soundThread.play("toywar.wav");
		soundThread.start();
		
		setSize(C.WIDTH, C.HEIGHT);
		
		eggNum = 0;
		speed = C.NORMAL;
		delay = C.DELAY;
		running = false;
		result = 0;
		pause = new Object();
		ready = new Object();
		
		map = new Grid[C.row][C.col];
		int i, j;
		for(i=0;i<C.row;i++){
			for(j=0;j<C.col;j++){
				map[i][j] = new Grid((i+j)%2);
			}
		}
		mainFrame = new MainFrame(this);
		try {
			synchronized(pause){
				pause.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void createElements(int type){
		playerType = type;
		mine = new NetSocket(type);
		alive1 = true;
		alive2 = true;
		if(type == C.SERVER){
			snake1 = new Snake(1);
			direction1 = C.RIGHT;
			snake2 = new Snake(2);
			direction2 = C.LEFT;
			mainFrame.yourScore.setBackground(Color.YELLOW);
			mainFrame.rivalScore.setBackground(Color.GREEN);
			eggThread = new EggThread(this);
			createWall();
			sendMap();
		} else {
			snake1 = new Snake(2);
			direction1 = C.LEFT;
			snake2 = new Snake(1);
			direction2 = C.RIGHT;
			mainFrame.yourScore.setBackground(Color.GREEN);
			mainFrame.rivalScore.setBackground(Color.YELLOW);
			eggThread = null;
		}
		snake1.drawSnake(getGraphics());
		occupy(snake1);
		snake2.drawSnake(getGraphics());
		occupy(snake2);
		receiveThread = new ReceiveThread(this);
		runThread = new RunThread(this);
	}
	
	public void createWall(){
		int i, j, a, b, x, y;
		a = (int)(Math.random()*9)+8;
		b = (int)(Math.random()*9)+22;
		x = (int)(Math.random()*5);
		y = (int)(Math.random()*5);
		for(i=0;i<C.row;i++){
			for(j=0;j<C.col;j++){
				if((j>2+x && j<15+x && ((i-14)*(i-14)+(j-a)*(j-a)/2<49) && (i-14)*(i-14)+(j-a)*(j-a)/2>25)
						|| (j>21+y && j<32+y && ((i-16)*(i-16)+(j-b)*(j-b)/2<49) && (i-16)*(i-16)+(j-b)*(j-b)/2>25) 
						|| ((i==5 || i==24) && (j<10 || j>29))
						){
					map[i][j].setStatue(2);
					map[i][j].draw(j, i, getGraphics());
				}
			}
		}
	}
	
	public void paint(Graphics g){
		int i, j;
		for(i=0;i<C.row;i++){
			for(j=0;j<C.col;j++){
				map[i][j].draw(j, i, g);
			}
		}
		if(snake1 != null) snake1.drawSnake(g);
		if(snake2 != null) snake2.drawSnake(g);
	}
	
	public void occupy(Snake snake1){
		int i;
		for(i=0;i<snake1.getLength();i++){
			map[snake1.snake.get(i).getY()][snake1.snake.get(i).getX()].setOccupied(true);
		}
	}
	
	public  void changeSpeed(int s) {
		speed = s;
		delay = (speed == C.NORMAL) ? C.DELAY : (speed == C.FAST) ? (int)(C.DELAY * C.RATE) : (int)(C.DELAY / C.RATE);
		mainFrame.speedString = (speed == C.NORMAL) ? "   normal" : (speed == C.FAST) ? "   fast " : "   slow ";
		mainFrame.speedLevel.setText(mainFrame.speedString);
	}
	
	public boolean growSnake(Snake snake1, Graphics g){
		int x = snake1.snake.getFirst().getX();
		int y = snake1.snake.getFirst().getY();
		map[y][x].draw(x, y, g);
		snake1.grow();
		snake1.snake.getFirst().drawNode(g);
		snake1.snake.get(1).drawNode(g);
		x = snake1.snake.getFirst().getX();
		y = snake1.snake.getFirst().getY();
//		map[y][x].setOccupied(true);
		return true;
	}
	public boolean cutSnake(Snake snake1, Graphics g){
		int x = snake1.snake.getLast().getX();
		int y = snake1.snake.getLast().getY();
		map[y][x].draw(x, y, g);
		map[y][x].setOccupied(false);
		snake1.cut();
		x = snake1.snake.getLast().getX();
		y = snake1.snake.getLast().getY();
		map[y][x].draw(x, y, g);
		snake1.snake.getLast().drawNode(g);
		return true;
	}
	public boolean step(Snake snake1, Graphics g){
		growSnake(snake1, g);
		int x = snake1.snake.getLast().getX();
		int y = snake1.snake.getLast().getY();
		map[y][x].setOccupied(false);
		x = snake1.snake.getFirst().getX();
		y = snake1.snake.getFirst().getY();
		if(map[y][x].getOccupied() || map[y][x].getStatue()==C.wall){
			snake1.cut();
			return false;
		} else {
			map[y][x].setOccupied(true);
			if(map[y][x].getStatue() == C.egg) {
				map[y][x].setStatue((y+x)%2);
				eggNum --;
				return true;
			} else {
				cutSnake(snake1, g);
				return true;
			}
		}
	}

	public void addEgg(int x, int y){
		map[y][x].setStatue(C.egg);
		map[y][x].draw(x, y, getGraphics());
		eggNum ++;
	}
	
	public void sendMap(){
		int i, j;
		for(i=0;i<C.row;i++){
			for(j=0;j<C.col;j++){
				mine.send("MAP " + i + " " + j + " " +map[i][j].getStatue());
			}
		}
	}

	public void endGame(){
		int x1, x2, y1, y2;
		x1 = snake1.snake.getFirst().getX();
		y1 = snake1.snake.getFirst().getY();
		x2 = snake2.snake.getFirst().getX();
		y2 = snake2.snake.getFirst().getY();
		if((x1==x2 && y1==y2) || (!alive1 && !alive2)) result = C.DUAL;
		else result = alive1 ? C.WIN : C.LOSE;
		mine.send("END " + result);
		String r = (result==C.DUAL) ? "Die All ! " : (result==C.WIN) ? "You Win ! " : "You Lose...";
		JOptionPane.showMessageDialog(new JFrame(), r);
		computeScore();
		eggThread.stop();;
	}
	
	public void computeScore(){
		try {
			File file = new File("score.txt");
			Scanner sc = new Scanner(file);
			int[] scores = new int[6];
			int i, n=0, j, t;
			while(sc.hasNext()&&n<5){
				scores[n] = sc.nextInt();
				n++;
			}
			sc.close();
			int p = n+1;
			scores[5] = (result==C.WIN) ? snake1.getLength()-2 : (result==C.DUAL) ? snake1.getLength()/2-1 : 0;
			for(i=0;i<n;i++){
				for(j=i+1;j<n+1;j++){
					if(scores[i] <= scores[j]){
						t = scores[i];
						scores[i] = scores[j];
						scores[j] = t;
						if(j==n&&p==n+1) p = i+1;
					}
				}
			}
			PrintStream out = new PrintStream(file);
			for(i=0;i<5;i++) out.print(" "+ scores[i]);
			out.close();
			showScores(scores, n, p, scores[p-1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showScores(int[] scores, int n, int p, int c){
		JFrame frame = new JFrame("龙虎榜");
		frame.setLocation(500, 250);
		frame.setSize(300, 220);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().setLayout(new GridLayout(7, 1));
		
		JTextPane[] s = {new JTextPane(), new JTextPane(), new JTextPane(), new JTextPane(), new JTextPane(), new JTextPane(), new JTextPane()};
		int i;
		frame.getContentPane().add(s[0]);
		s[0].setSize(200, 20);
		s[0].setEditable(false);
		s[0].setFont(new Font("Serif", Font.ITALIC, 16));
		s[0].setForeground(Color.BLUE);
		s[0].setBackground(Color.yellow);
		s[0].setText("名次" + "\t\t\t" + "分数" + '\n');
		for(i=1;i<n+1;i++){
			frame.getContentPane().add(s[i]);
			s[i].setSize(200, 30);
			s[i].setEditable(false);
			if(i == p) {
				s[i].setForeground(Color.RED);
				s[i].setBackground(Color.green);
			}
			else {
				s[i].setForeground(Color.BLACK);
				s[i].setBackground(Color.yellow);
			}
			s[i].setText(i + "\t\t\t" + scores[i-1] + '\n');
		}
		frame.getContentPane().add(s[i]);
		s[i].setSize(200, 30);
		s[i].setEditable(false);
		s[i].setForeground(Color.black);
		s[i].setBackground(Color.white);
		s[i].setFont(new Font("Serif", Font.ITALIC, 16));
		s[i].setText("本次分数" + "\t\t" + c + '\n');
	}
}
