package Game;

import Constant.C;

public class EggThread extends Thread{
	
	Game game;
	boolean running;
	
	EggThread(Game g){
		super();
		game = g;
		running = true;
	}


	public void run(){
		while(running){
			try {
				if(game.running){
					if(game.eggNum == 0){
						EggThread.sleep(2000);
						createEgg();
						EggThread.sleep(10);
						createEgg();
					} else {
						EggThread.sleep(10);					
					}
				} else {
					EggThread.sleep(10);					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createEgg(){
		int x, y;
		while(true){
			x = (int)(Math.random()*C.col);
			y = (int)(Math.random()*C.row);
			if(game.map[y][x].getStatue() < 2 && !game.map[y][x].getOccupied()) break;
		}
		game.map[y][x].setStatue(C.egg);
		game.map[y][x].draw(x, y, game.getGraphics());
		game.mine.send("EGGS " + x + " " + y);
		game.eggNum ++;
	}
}
