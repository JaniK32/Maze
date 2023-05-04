package jk.maze.cheat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jk.maze.output.Printer;
import jk.maze.output.Square;

/**
 * Algortihm is taken from page : https://www.baeldung.com/java-solve-maze
 * File path is hardcoded and height & width are hardcoded in Maze class.
 * 
 * @author janik
 *
 */
public class CopiedSolution {

	private static int[][] DIRECTIONS  = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	
	public static void main(String[] args) {
		try {
			Maze maze = new Maze("c:\\temp\\maze-task-first_(2).txt");
			
			CopiedSolution solver = new CopiedSolution();
			List<Square> coordinates = solver.solve(maze);

			Printer.printResult(maze.getArray(), coordinates);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<Square> backtrackPath(Coordinate cur) {
		List<Square> path = new ArrayList<>();
	    Coordinate iter = cur;

	    while (iter != null) {
	        path.add(iter);
	        iter = iter.parent;
	    }

	    return path;
	}


	public List<Square> solve(Maze maze) {
	    LinkedList<Coordinate> nextToVisit  = new LinkedList<>();
	    Coordinate start = maze.getEntry();
	    
	    nextToVisit.add(start);

	    while (!nextToVisit.isEmpty()) {
	        Coordinate cur = nextToVisit.remove();
	        
	        if (!maze.isValidLocation(cur.getX(), cur.getY()) || maze.isExplored(cur.getX(), cur.getY())){
	            continue;
	        }

	        if (maze.isWall(cur.getX(), cur.getY())) {
	            maze.setVisited(cur.getX(), cur.getY(), true);
	            continue;
	        }

	        if (maze.isExit(cur.getX(), cur.getY())) {
	            return backtrackPath(cur);
	        }

	        for (int[] direction : DIRECTIONS) {
	            Coordinate coordinate 
	              = new Coordinate(
	                cur.getX() + direction[0], 
	                cur.getY() + direction[1], 
	                cur
	              );
	            nextToVisit.add(coordinate);
	            maze.setVisited(cur.getX(), cur.getY(), true);
	        }
	    }
	    return  Collections.emptyList();
	}
}
