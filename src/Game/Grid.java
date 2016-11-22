package Game;

import java.awt.*;
import Constant.*;

public class Grid {

	protected int statue;
	protected boolean occupied;
	
	Grid(){
		statue = 0;
		occupied = false;
	}
	Grid(int k){
		statue = k;
		occupied = false;
	}
	
	public void setStatue(int s){
		statue = s;
	}
	
	public void setOccupied(boolean o){
		occupied = o;
	}
	
	public int getStatue(){
		return statue;
	}
	
	public boolean getOccupied(){
		return occupied;
	}
	
	public void draw(int x, int y, Graphics g){
		int X = x * C.SIDE;
		int Y = y * C.SIDE;
		switch(statue){
		case C.grass: drawGrass(X, Y, g); break;
		case C.bush: drawBush(X, Y, g); break;
		case C.wall: drawWall(X, Y, g); break;
		case C.egg: drawEgg(X, Y, g); break;
		default :break;
		}
	}	
	void drawGrass(int X, int Y, Graphics g){
		g.setColor(C.GRASS);
		g.fillRect(X, Y, C.SIDE, C.SIDE);
	}
	void drawBush(int X, int Y, Graphics g){
		g.setColor(C.BUSH);
		g.fillRect(X, Y, C.SIDE, C.SIDE);
	}	
	void drawWall(int X, int Y, Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(X, Y, C.SIDE, C.SIDE);
		g.setColor(C.WALL);
		g.fillRect(X+1, Y+1, 8, 8);
		g.fillRect(X+11, Y+1, 8, 8);
		g.fillRect(X+11, Y+11, 8, 8);
		g.fillRect(X+1, Y+11, 8, 8);
	}
	void drawEgg(int X, int Y, Graphics g){
		g.setColor(new Color(0, 128, 0));
		g.fillRect(X, Y, C.SIDE, C.SIDE);
		g.setColor(Color.BLACK);
		g.fillOval(X, Y+8, 16, 12);
		g.setColor(new Color(255, 255, 170));
		g.fillOval(X+5, Y+1, 14, 18);
	}
}
