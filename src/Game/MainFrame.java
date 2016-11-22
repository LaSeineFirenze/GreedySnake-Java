package Game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Constant.C;

public class MainFrame extends JFrame implements ActionListener, KeyListener{

	Game game;
	
	JPanel yourScore, rivalScore;
	JTextPane score1, score2;
	JTextPane speedLevel;
	String speedString;

	MainFrame(Game g){
		super("Greedy Snake");
		game = g;
		
		setSize(C.WIDTH+140, C.HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(getGamePane());
		setVisible(true);
		addKeyListener(this);
	}
	JPanel getGamePane(){
		JPanel gamePane = new JPanel(new BorderLayout());
		gamePane.add(game, "Center");
		gamePane.add(getStatusPane(), "East");
		return gamePane;
	}
	JPanel getStatusPane(){
		JPanel statusPane = new JPanel(new GridLayout(5, 0));
			
		yourScore = getYourScore();
		statusPane.add(yourScore);
		
		JButton play = new JButton();
		play.add(getText("PLAY", 24));
		play.setActionCommand("PLAY");
		play.addActionListener(this);
		statusPane.add(play);
		
		statusPane.add(getSpeedPane());
		
		JButton pause = new JButton();
		pause.add(getText("PAUSE", 24));
		pause.setActionCommand("PAUSE");
		pause.addActionListener(this);
		statusPane.add(pause);
		
		rivalScore = getRivalScore();
		statusPane.add(rivalScore);
		return statusPane;
	}
	JTextPane getText(String s, int size){
		JTextPane text = new JTextPane();
		text.setEditable(false);
		Font font = new Font("Serif", Font.ITALIC, size);
		text.setForeground(Color.DARK_GRAY);
		text.setFont(font);
		text.setOpaque(false);
		text.setText(s);
		return text;
	}
	JPanel getYourScore(){
		JPanel yourScore = new JPanel(new GridLayout(2, 0));
		yourScore.add(getText("  Your score: ", 24));
		score1 = getText("       0", 24);
		yourScore.add(score1);
		return yourScore;
	}
	JPanel getSpeedPane(){
		JPanel speedPane = new JPanel(new BorderLayout());
		speedString = (game.speed == C.NORMAL) ? "   normal" : (game.speed == C.FAST) ? "    fast " : "    slow " ;
		JPanel sp = new JPanel(new GridLayout(2, 0));
		speedLevel = getText(speedString, 20);
		sp.add(getText("Speed:", 16));
		sp.add(speedLevel);
		speedPane.add(sp);
		JPanel change = new JPanel(new GridLayout(2, 0));
		JButton up = new JButton("+");
		up.setActionCommand("SPEEDUP");
		up.addActionListener(this);
		change.add(up);
		JButton down = new JButton("-");
		down.setActionCommand("SPEEDDOWN");
		down.addActionListener(this);
		change.add(down);
		speedPane.add(change, "East");
		return speedPane;
	} 
	JPanel getRivalScore(){
		JPanel rivalScore = new JPanel(new GridLayout(2, 0));
		rivalScore.add(getText("Rival's score:", 24));
		score2 = getText("       0", 24);
		rivalScore.add(score2);
		return rivalScore;
	}
	
	public void pause(){
		if(game.playerType == C.SERVER) game.running = false;
		JOptionPane.showMessageDialog(new JFrame(), "Game paused! ");
	}
	
	public void resume(){
		game.running = true;
		if(game.playerType == C.SERVER){
			synchronized(game.runThread.pause){
				game.runThread.pause.notify();
			}
		}
	//	JOptionPane.showMessageDialog(new JFrame(), "Game resumed. ");
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(game.playerType == C.SERVER){
			int k = arg0.getKeyCode();
			switch(k){
			case KeyEvent.VK_UP: 
				game.direction1=(C.UP);
				break;
			case KeyEvent.VK_RIGHT: 
				game.direction1=(C.RIGHT);
				break;
			case KeyEvent.VK_DOWN: 
				game.direction1=(C.DOWN);
				break;
			case KeyEvent.VK_LEFT: 
				game.direction1=(C.LEFT);
				break;
			case '=': 
				game.mine.send("FASTER");
				if(game.speed < C.FAST) game.changeSpeed(game.speed+1); 
				break;
			case '-':
				game.mine.send("SLOWER");
				if(game.speed > C.SLOW) game.changeSpeed(game.speed-1);
				break;
			case KeyEvent.VK_SPACE: 
				game.mine.send("PAUSE");
				pause();
				game.mine.send("RESUME");
				resume();
				break;
			default : break;
			}
		} else {
			char c = arg0.getKeyChar(); 
			switch(c){
			case 'w': 
				game.mine.send("UP");
				break;
			case 'a': 
				game.mine.send("LEFT");
				break;
			case 's': 
				game.mine.send("DOWN");
				break;
			case 'd': 
				game.mine.send("RIGHT");
				break;
			case '=': 
				game.mine.send("FASTER");
				if(game.speed < C.FAST) game.changeSpeed(game.speed+1); 
				break;
			case '-':
				game.mine.send("SLOWER");
				if(game.speed > C.SLOW) game.changeSpeed(game.speed-1);
				break;
			case KeyEvent.VK_SPACE:
				game.mine.send("PAUSE");
				pause();
				game.mine.send("RESUME");
				resume();
				break;
			default : break;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String s = arg0.getActionCommand();
		switch(s){
		case "PLAY":
			game.running = true;
			game.playerType = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to set up a server?");
			game.createElements(game.playerType);
			
			synchronized(game.pause){
				game.pause.notify();
			}
			requestFocus();
			break;
		case "PAUSE":
			game.mine.send("PAUSE");
			pause();
			requestFocus();
			break;
		case "SPEEDUP":
			game.mine.send("SPEEDUP");
			if(game.speed < C.FAST) game.changeSpeed(game.speed+1);
			requestFocus();
			break;
		case "SPEEDDOWN":
			game.mine.send("SPEEDDOWN");
			if(game.speed > C.SLOW) game.changeSpeed(game.speed-1);
			requestFocus();
			break;
		default: break;
		}
		
	}
	
	public static void main(String[] args){
		Game game = new Game();
		game.receiveThread.start();
		if(game.playerType == C.SERVER){
			game.runThread.start();
			game.eggThread.start();
		}
	}
}
