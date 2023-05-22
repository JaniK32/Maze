package jk.maze.cheat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jk.maze.BaseMazeApp;
import jk.maze.MazeProperties;
import jk.maze.output.Printer;
import jk.maze.output.Square;

/**
 * Algorithm is taken from page : https://www.baeldung.com/java-solve-maze
 * 
 * @author janik
 *
 */
public class CopiedSolution extends BaseMazeApp {

	private static int[][] DIRECTIONS  = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	
	public static void main(String[] args) {
		try {
			String filePath = null;// getPathToFile(args);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while ((filePath = askFile(reader)).length()>0) {
				
				MazeProperties mazeProperties = getMazeProperties(filePath);
				
				Maze maze = new Maze(mazeProperties);
				
				CopiedSolution solver = new CopiedSolution();
				List<Square> coordinates = solver.solve(maze);

				Printer.printResult(maze.getArray(), coordinates);
			}
			reader.close();
			Printer.pl("Finished.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String askFile(BufferedReader reader) throws IOException {
		Printer.pl("Give path to maze, or push enter for quit please.\n");
		return readInput(reader);
	}
	
	public static String readInput (BufferedReader reader) throws IOException {
		String selection = null;
		if (reader != null) {
			while ((selection = reader.readLine() ).length()>0 && !new File(selection).exists()) {
				Printer.pl("File "+selection+" is not found.\n");
			}
		}
		return selection;
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
