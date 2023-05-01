package jk.maze.cheat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import jk.maze.MazePoint;

public class Maze {
	
	// for first maze
	int width = 37;
	int height = 19;

	// for second maze
	//int width = 38;
	//int height = 20;
	
	MazePoint [][] array;
	Coordinate startPoint;
	
	public Maze(String filePath) throws IOException{
		array = fillMazeArray(filePath);
	}

	public MazePoint[][] getArray() {
		return array;
	}
	
	public Coordinate getEntry() {
		return startPoint;
	}

	public boolean isValidLocation(double x, double y) {
		if (x>=width) return false;
		if (y>=height) return false;
		
		MazePoint mp = array[(int)x][(int)y];
		return (mp.isPath() || mp.isStart());
	}

	public boolean isExplored(double x, double y) {
		return (array[(int)x][(int)y].isVisited());
	}

	public boolean isWall(double x, double y) {
		return (array[(int)x][(int)y].isWall());
	}

	public void setVisited(double x, double y, boolean b) {
		 array[(int)x][(int)y].setVisited(true);		
	}

	public boolean isExit(double x, double y) {
		return (array[(int)x][(int)y].isExit() || array[(int)x][(int)y-1].isExit() || array[(int)x+1][(int)y].isExit());
	}

	public MazePoint [][] fillMazeArray (String filePath) throws IOException {
		
		MazePoint [][] points = new MazePoint[width][height];
			String line = null;
			int lineCounter = 0;
	        BufferedReader br = new BufferedReader(new FileReader(filePath));
	
	        MazePoint mazePoint = null;
	       
	        while (null != ( line = br.readLine())) {
	        	MazePoint previous = null;
	        	
	        	for (int i=0;i<line.length();i++) {
	        		char square = line.charAt(i);
	        		mazePoint = new MazePoint(i, lineCounter);
	        		
	        		switch (square) {
	        			case '^' : 
	        				mazePoint.setStart(true);
	        				startPoint = new Coordinate(i, lineCounter, null);
	        			break;
	        			case 'E' : 
	            			mazePoint.setExit(true);
	        			break;
	        			case '#' : 
	        				mazePoint.setWall(true);
	        			break;
	        			case ' ' : 
	        				mazePoint.setPath(true);
	        			break;
	        		}
	        		
	        		if (previous != null) {
	        			previous.addNeighbour(mazePoint);       			
	        		}
	        		
	        		if (lineCounter>0) {
	        			points[i][lineCounter-1].addNeighbour(mazePoint);
	        		}
	        		points[i][lineCounter] = mazePoint;
	        		previous = mazePoint;
	        	}
	
	        	lineCounter++;
	        }

        br.close();
        return points;
	}
}
