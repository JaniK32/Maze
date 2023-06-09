package jk.maze.cheat;

import java.awt.Point;

import jk.maze.MazePoint;
import jk.maze.output.Square;

public class Coordinate extends Point implements Square {

	private static final long serialVersionUID = 1L;

	public Coordinate parent;
	
	public Coordinate(double d, double e, Coordinate cur) {
		super((int)d,(int)e);
		parent = cur;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Coordinate) {
			if (this.x == ((Coordinate)obj).x && this.y == ((Coordinate)obj).y) {
				return true;
			}
		}
		
		if (obj instanceof MazePoint) {
			if (this.x == ((MazePoint)obj).x && this.y == ((MazePoint)obj).y) {
				return true;
			}
		}
		
		return false;
	}

}
