package Constant;

import java.awt.Color;

public class C {
	//map
	public static final int WIDTH = 816;
	public static final int HEIGHT = 639;
	public static final int row = 30;
	public static final int col = 40;

	//Grid
	public static final int SIDE = 20;
	
	//Node
	public static final int HEAD = 1;
	public static final int BODY = 2;
	public static final int TAIL = 3;
	
	//direction <- Node/Snake
	public static final int UP = 1, RIGHT = 2, DOWN = 4, LEFT = 3;
	public static final int UR = 6, UL = 7, DR = 9, DL = 10; // d(new)-d(old)+8

	//statue <- Grid
	public static final int grass = 0;
	public static final int bush = 1;
	public static final int wall = 2;
	public static final int hole = 3;
	public static final int egg = 4;
	
	//Color of Grid
	public static final Color GRASS = new Color(0,96,0);
	public static final Color BUSH = new Color(0, 64, 0);
	public static final Color WALL = new Color(128, 64, 0);
	
	//Type of Socket
	public static final int SERVER = 0;
	public static final int CLIENT = 1;
	
	//Speed of game
	public static final int SLOW = 1;
	public static final int NORMAL = 2;
	public static final int FAST = 3;
	public static final int DELAY = 200;
	public static final double RATE = 0.6;
	
	//Result of game
	public static final int WIN = 0;
	public static final int DUAL = 1;
	public static final int LOSE = 2;
}
