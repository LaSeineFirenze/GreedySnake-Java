package Game;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Constant.C;

public class ReceiveThread extends Thread{

	Game game;
	boolean running;
	String line;
	
	ReceiveThread(Game g){
		super();
		game = g;
		running = true;
		
	}

	public void run(){
		if(game.playerType == C.SERVER){
			try {
				int x, y;
				while(running){
					line = game.mine.receive();
					switch(line){
					case "UP":
						game.direction2=(C.UP);
						System.out.println("UP");
						break;
					case "RIGHT":
						game.direction2=(C.RIGHT);
						break;
					case "DOWN":
						game.direction2=(C.DOWN);
						break;
					case "LEFT":
						game.direction2=(C.LEFT);
						break;
					case "FASTER":
						if(game.speed < C.FAST) game.changeSpeed(game.speed+1); 
						break;
					case "SLOWER":
						if(game.speed > C.SLOW) game.changeSpeed(game.speed-1);
						break;
					case "PAUSE":
						game.mainFrame.pause();
						break;
					case "RESUME":
						game.mainFrame.resume();
						break;
					default :
						String[] str = line.split(" ");
						if(str[0].equals("EGGS")){
							x = Integer.parseInt(str[1]);
							y = Integer.parseInt(str[2]);
							putEggs(x, y);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(game.playerType == C.CLIENT){
			String line;
			String[] str;
			int d1, d2;
			try {
				line = game.mine.receive();
				while(!line.equals("END")){
					str = line.split(" ");
					switch(str[0]){
					case "SNAKE":
						d1 = Integer.parseInt(str[1]);
						d2 = Integer.parseInt(str[2]);
						game.snake1.turn(d2);
						game.step(game.snake1, game.getGraphics());
						game.snake2.turn(d1);
						game.step(game.snake2, game.getGraphics());
						game.mainFrame.score1.setText("      " + (game.snake1.getLength()-2));
						game.mainFrame.score2.setText("      " + (game.snake2.getLength()-2));
						break;
					case "EGGS":
						d1 = Integer.parseInt(str[1]);
						d2 = Integer.parseInt(str[2]);
						game.addEgg(d1, d2);
						break;
					case "MAP":
						d1 = Integer.parseInt(str[1]);
						d2 = Integer.parseInt(str[2]);
						game.map[d1][d2].setStatue(Integer.parseInt(str[3]));
						game.map[d1][d2].draw(d2, d1, game.getGraphics());
						break;
					case "FASTER":
						if(game.speed < C.FAST) game.changeSpeed(game.speed+1); 
						break;
					case "SLOWER":
						if(game.speed > C.SLOW) game.changeSpeed(game.speed-1);
						break;
					case "PAUSE":
						game.mainFrame.pause();
						break;
					case "RESUME":
						game.mainFrame.resume();
						break;
					case "END":
						game.result = 2 - Integer.parseInt(str[1]);
				//		String r = (game.result==C.DUAL) ? "Die All ! " : (game.result==C.WIN) ? "You Win ! " : "You Lose...";
					//	JOptionPane.showMessageDialog(new JFrame(), r);
						game.endGame();
						break;
					default : break;
					}
					line = game.mine.receive();
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		
	}
	public void putEggs(int x, int y){
		game.map[y][x].setStatue(C.egg);
		game.map[y][x].draw(x, y, game.getGraphics());
		game.eggNum ++;
	}
}
