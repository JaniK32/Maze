package jk.maze;

import java.awt.Point;
import java.util.ArrayList;

public class MazePoint extends Point {

	private static final long serialVersionUID = 1L; 
	
	private boolean connectedToExit = false;
	private boolean connectedToStart = false;
	private boolean wall = false;
	private boolean path = false;
	private boolean exit = false;
	private boolean start = false;
	
	ArrayList<MazePoint> neigbours = new ArrayList<>();
	
	public MazePoint(int x, int y) {
		super(x,y);
	}
	
	public boolean isConnectedToExit() {
		return connectedToExit;
	}
	public void setConnectedToExit(boolean connectedToExit, MazePoint p) {
		this.connectedToExit = connectedToExit;
		for (MazePoint neigbour : neigbours) {
			if (neigbour.isPath() && !neigbour.isConnectedToExit() && !neigbour.onlyWallsAround(p))
				neigbour.setConnectedToExit(true,p);
		}
	}
	public boolean isConnectedToStart() {
		return connectedToStart;
	}
	public void setConnectedToStart(boolean connectedToStart) {
		this.connectedToStart = connectedToStart;
		for (MazePoint neigbour : neigbours) {
			if (neigbour.isPath() && !neigbour.isConnectedToStart())
				neigbour.setConnectedToStart(true);
		}
	}
	
	public boolean isWall() {
		return wall;
	}

	public void setWall(boolean wall) {
		this.wall = wall;
	}

	public boolean isPath() {
		return path;
	}

	public void setPath(boolean path) {
		this.path = path;
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	/*
	 * public boolean isNexto(MazePoint mazePoint) { if (x == mazePoint.x &&
	 * Math.abs(y - mazePoint.y) == 1) return true;
	 * 
	 * if (y == mazePoint.y && Math.abs(x - mazePoint.x) == 1) return true;
	 * 
	 * return false; }
	 */
	
	public void addNeighbour(MazePoint mazePoint) {
		if (mazePoint.isExit() && isPath()) {
			setConnectedToExit(true,mazePoint);
			for (MazePoint neigbour : neigbours) {
				if (neigbour.isPath())
					neigbour.setConnectedToExit(true,this);
			}
		}
		
		if (connectedToExit && mazePoint.isPath())
			mazePoint.setConnectedToExit(true,this);
		
		if (connectedToStart && mazePoint.isPath())
			mazePoint.setConnectedToStart(true);
		
		if (mazePoint.isStart() && isPath()) {
			setConnectedToStart(true);
			for (MazePoint neigbour : neigbours) {
				if (neigbour.isPath())
					neigbour.setConnectedToStart(true);
			}
		}
		
		if (!neigbours.contains(mazePoint))
			neigbours.add(mazePoint);
		
		if (!mazePoint.neigbours.contains(this))
			mazePoint.addNeighbour(this);
	}
	
	public boolean isValidPathMember(){
		return isConnectedToExit() && isConnectedToStart();
	}
		
	public boolean onlyWallsAround(MazePoint p) {
		
		for (MazePoint neigbour : neigbours) {
			if (neigbour != p && neigbour.isPath()) {
				return false;
			}
		}
		return true;
	}
	
	public int getValidity(/*Point end,*/ Point start) {
		int diffStart = Math.abs(x - start.x) + Math.abs(y - start.y) ;
		//int diffEnd = Math.abs(x - end.x) + Math.abs(y - end.y) ;
		return diffStart;// + diffEnd;
	}
	
	public MazePoint getMostValidNeighbour(Point end, Point start) {
		MazePoint n = null;

		for (MazePoint neigbour : neigbours) {
			 if (neigbour.isStart())
				 return neigbour;
		
			 if ((n == null && neigbour.isValidPathMember()) || (neigbour.isValidPathMember() && n.getValidity(/*end,*/ start)> neigbour.getValidity(/*end,*/ start) ) && !neigbour.isDeadEnd2()) {
				 n = neigbour;
			 }
		}
	
		return n;
	}
 
 	public ArrayList<MazePoint> getNeighbours(){ 
	 	return  neigbours;
 	}
 	
	public boolean removePoint(MazePoint mp) {
		return neigbours.remove(mp);
	}
	
	public boolean isDeadEnd() {
		int count = 0;
		for (MazePoint neighbour : neigbours) {
			if (!neighbour.isWall()) {
				 count++;
			}
		}
		return count<2;
	}
	
	public boolean isDeadEnd2() {
		int count = 0;
		for (MazePoint neighbour : neigbours) {
			if (!neighbour.isWall() && !neighbour.isDeadEnd()) {
				 count++;
			}
		}
		return count<2;
	}
	
	// FOR CHEAT PACKAGE :
	boolean visited = false;

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
}
