package Snake;

import java.awt.Color;
import java.awt.Graphics;
import Constant.*;

public class Node {
	
	int x, y;
	int part;
	int direction;
	Color bodyColor, edgeColor;
	
	Node(){}
	Node(int xx, int yy, int pp, int dd){
		x = xx;
		y = yy;
		part = pp;
		direction = dd;
	}
	public Node(int xx, int yy, int pp, int dd, Color bc, Color ec){
		x = xx;
		y = yy;
		part = pp;
		direction = dd;
		bodyColor = bc;
		edgeColor = ec;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setPart(int p){
		part = p;
	}
	
	public void turn(int d){
		if( (direction-d)*(direction+d-5) == 0 )return ;
		else direction = d - direction + 8;
	}
	
	public void drawNode(Graphics g){
		int X = x*C.SIDE;
		int Y = y*C.SIDE;
		switch(part){
		case C.HEAD:drawHead(X, Y, g);break;
		case C.BODY:drawBody(X, Y, g);break;
		case C.TAIL:drawTail(X, Y, g);break;
		default : break;
		}
	}
	void drawBody(int X, int Y, Graphics g){
		switch(direction){
		case C.UP:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y, 12, 20);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y, 8, 20);
			break;
		case C.DOWN:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y, 12, 20);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y, 8, 20);
			break;
		case C.RIGHT:
			g.setColor(edgeColor);
			g.fillRect(X, Y+4, 20, 12);
			g.setColor(bodyColor);
			g.fillRect(X, Y+6, 20, 8);
			break;
		case C.LEFT:
			g.setColor(edgeColor);
			g.fillRect(X, Y+4, 20, 12);
			g.setColor(bodyColor);
			g.fillRect(X, Y+6, 20, 8);
			break;
		case C.UR:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y, 12, 10);
			g.fillRect(X+10, Y+4, 10, 12);
			g.fillOval(X+4, Y+4, 12, 12);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y, 8, 10);
			g.fillRect(X+10, Y+6, 10, 8);
			g.fillOval(X+6, Y+6, 8, 8);
			break;
		case C.UL:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y, 12, 10);
			g.fillRect(X, Y+4, 10, 12);
			g.fillOval(X+4, Y+4, 12, 12);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y, 8, 10);
			g.fillRect(X, Y+6, 10, 8);
			g.fillOval(X+6, Y+6, 8, 8);
			break;
		case C.DR:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y+10, 12, 10);
			g.fillRect(X+10, Y+4, 10, 12);
			g.fillOval(X+4, Y+4, 12, 12);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y+10, 8, 10);
			g.fillRect(X+10, Y+6, 10, 8);
			g.fillOval(X+6, Y+6, 8, 8);
			break;
		case C.DL:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y+10, 12, 10);
			g.fillRect(X, Y+4, 10, 12);
			g.fillOval(X+4, Y+4, 12, 12);			
			g.setColor(bodyColor);
			g.fillRect(X+6, Y+10, 8, 10);
			g.fillRect(X, Y+6, 10, 8);
			g.fillOval(X+6, Y+6, 8, 8);
			break;
		default : break;
		}
	}
	void drawHead(int X, int Y, Graphics g){
		switch(direction){
		case C.UP:
			g.setColor(edgeColor);
			g.fillOval(X, Y, 20, 24);
			g.setColor(bodyColor);
			g.fillOval(X+3, Y+2, 14, 20);
			g.setColor(Color.WHITE);
			g.fillOval(X, Y, 10, 10);
			g.fillOval(X+10, Y, 10, 10);
			g.setColor(Color.BLACK);
			g.fillOval(X+1, Y+1, 6, 6);
			g.fillOval(X+13, Y+1, 6, 6);
			break;
		case C.RIGHT:
			g.setColor(edgeColor);
			g.fillOval(X-4, Y, 24, 20);
			g.setColor(bodyColor);
			g.fillOval(X-2, Y+3, 20, 14);
			g.setColor(Color.WHITE);
			g.fillOval(X+10, Y, 10, 10);
			g.fillOval(X+10, Y+10, 10, 10);
			g.setColor(Color.BLACK);
			g.fillOval(X+13, Y+1, 6, 6);
			g.fillOval(X+13, Y+13, 6, 6);
			break;
		case C.DOWN:
			g.setColor(edgeColor);
			g.fillOval(X, Y-4, 20, 24);
			g.setColor(bodyColor);
			g.fillOval(X+3, Y-2, 14, 20);
			g.setColor(Color.WHITE);
			g.fillOval(X, Y+10, 10, 10);
			g.fillOval(X+10, Y+10, 10, 10);
			g.setColor(Color.BLACK);
			g.fillOval(X+1, Y+13, 6, 6);
			g.fillOval(X+13, Y+13, 6, 6);
			break;
		case C.LEFT:
			g.setColor(edgeColor);
			g.fillOval(X, Y, 24, 20);
			g.setColor(bodyColor);
			g.fillOval(X+2, Y+3, 20, 14);
			g.setColor(Color.WHITE);
			g.fillOval(X, Y, 10, 10);
			g.fillOval(X, Y+10, 10, 10);
			g.setColor(Color.BLACK);
			g.fillOval(X+1, Y+1, 6, 6);
			g.fillOval(X+1, Y+13, 6, 6);
			break;
		default : break;
		}
	}
	void drawTail(int X, int Y, Graphics g){
		switch(direction){
		case C.UP:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y, 12, 10);
			g.fillRect(X+6, Y+10, 8, 4);
			g.fillRect(X+8, Y+14, 4, 6);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y, 8, 8);
			break;
		case C.RIGHT:
			g.setColor(edgeColor);
			g.fillRect(X+10, Y+4, 10, 12);
			g.fillRect(X+6, Y+6, 4, 8);
			g.fillRect(X, Y+8, 6, 4);
			g.setColor(bodyColor);
			g.fillRect(X+12, Y+6, 8, 8);
			break;
		case C.DOWN:
			g.setColor(edgeColor);
			g.fillRect(X+4, Y+10, 12, 10);
			g.fillRect(X+6, Y+6, 8, 4);
			g.fillRect(X+8, Y, 4, 6);
			g.setColor(bodyColor);
			g.fillRect(X+6, Y+12, 8, 8);
			break;
		case C.LEFT:
			g.setColor(edgeColor);
			g.fillRect(X, Y+4, 10, 12);
			g.fillRect(X+10, Y+6, 4, 8);
			g.fillRect(X+14, Y+8, 6, 4);
			g.setColor(bodyColor);
			g.fillRect(X, Y+6, 8, 8);
			break;
		default : break;
		}
	}
}
