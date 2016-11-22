package Snake;

import java.awt.*;
import java.util.*;
import Constant.*;

public class Snake {
	
	public LinkedList<Node> snake;
	int length;
	int direction;
	Color bodyColor, edgeColor;	
	
	public Snake(int p){
		length = 2;
		snake = new LinkedList<Node>();
		if(p==1){
			direction = C.RIGHT;
			bodyColor = Color.ORANGE;
			edgeColor = Color.yellow;
			snake.add(new Node(4, 3, C.HEAD, C.RIGHT, bodyColor, edgeColor));
			snake.add(new Node(3, 3, C.BODY, C.RIGHT, bodyColor, edgeColor));
			snake.add(new Node(2, 3, C.TAIL, C.RIGHT, bodyColor, edgeColor));
		} else {
			direction = C.LEFT;
			bodyColor = new Color(0, 192, 0);
			edgeColor = Color.GREEN;
			snake.add(new Node(35, 26, C.HEAD, C.LEFT, bodyColor, edgeColor));
			snake.add(new Node(36, 26, C.BODY, C.LEFT, bodyColor, edgeColor));
			snake.add(new Node(37, 26, C.TAIL, C.LEFT, bodyColor, edgeColor));			
		}
	}
	public int getLength(){
		return length;
	}
	public int getDirection(){
		return direction;
	}
	
	public void turn(int d){
		if((direction - d) * (direction + d -5) == 0) return;
		else direction = d;
	}
	
	public void grow(){
		snake.getFirst().turn(direction);
		snake.getFirst().setPart(C.BODY);
		Node n;
		int x = snake.getFirst().getX();
		int y = snake.getFirst().getY();
		switch(direction){
		case C.UP: n = new Node(x, (y+29)%30, C.HEAD, direction, bodyColor, edgeColor);break;
		case C.RIGHT: n = new Node((x+1)%40, y, C.HEAD, direction, bodyColor, edgeColor);break;
		case C.DOWN: n = new Node(x, (y+1)%30, C.HEAD, direction, bodyColor, edgeColor);break;
		case C.LEFT: n = new Node((x+39)%40, y, C.HEAD, direction, bodyColor, edgeColor);break;
		default: n = null; break;
		}
		snake.addFirst(n);
		length ++;
	}
	
	public void cut(){
		int d = snake.removeLast().direction;
		snake.getLast().setPart(C.TAIL);
		if(snake.getLast().direction > 4) snake.getLast().direction += (d-8);
		length --;
	}
	
	public void move(){
		grow();
		cut();
	}
	
	
	public void drawSnake(Graphics g){
		int i;
		for(i=0;i<length+1;i++) snake.get(i).drawNode(g);
	}
}
