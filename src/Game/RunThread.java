package Game;

public class RunThread extends Thread{

	Game game;
	Object pause;
	
	RunThread(Game g){
		super();
		game = g;
		pause = new Object();
	}
	
	public void run(){
		try {
			while(true){
				if(game.running){
					RunThread.sleep(game.delay);
					game.snake1.turn(game.direction1);
					game.snake2.turn(game.direction2);
					game.alive1 = game.step(game.snake1, game.getGraphics());
					game.alive2 = game.step(game.snake2, game.getGraphics());
					game.mainFrame.score1.setText("      " + (game.snake1.getLength()-2));
					game.mainFrame.score2.setText("      " + (game.snake2.getLength()-2));
					game.mine.send("SNAKE " + game.snake1.getDirection() + " " + game.snake2.getDirection());
					if(!(game.alive1 && game.alive2)) break;
				} else {
					synchronized(pause){
						pause.wait();
					}
				}
			}
			game.endGame();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
}
