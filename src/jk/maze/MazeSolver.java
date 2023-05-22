package jk.maze;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jk.maze.output.Printer;
import jk.maze.output.Square;

/**
 * Some kind of maze solving application.
 * It doesn't always find a route and probably never the 
 * shortest one.
 * One can give a path to file as command line argument.
 * Format : ".\\Mazes\\maze-task-first_(2).txt"
 * After reading a file, application asks which exit should be used.
 * Application quits when 0 is given (or ctrl + c).
 * 
 * @author janik
 *
 */
public class MazeSolver extends BaseMazeApp {

	public static void main(String[] args) throws IOException {
		try {
			String filePath = getPathToFile(args);
			
			MazeProperties mazeProperties = getMazeProperties(filePath);
	        
	        Printer.p("lines " + mazeProperties.height + " columns " + mazeProperties.width + " exits " + mazeProperties.exits.size());
	        Printer.pl("");
	    	
			int selectedExit = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while ((selectedExit = askExitNumber(reader, mazeProperties.exits.size())) > 0) {
				resolveRoute(mazeProperties, selectedExit-1);
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Printer.pl("finished");
	}

	public static int askExitNumber(BufferedReader reader, int exitCount) throws IOException {
		Printer.pl("Give index of exit, or 0 for quit please.\n");
		int selection = -1;
		while ( (selection = readInput(reader)) > exitCount || selection < 0) {
			Printer.pl("Number is not valid.\n");
		}
		return selection;
	}
	
	public static int readInput (BufferedReader reader) throws IOException {
		int selection = -1;
		if (reader != null) {
			try {
				selection = Integer.parseInt(reader.readLine());
			} catch (NumberFormatException e) {
			}
		}
		return selection;
	}
		
	public static int resolveRoute(MazeProperties mazeProperties, int exitIndex) throws IOException {
		MazePoint [][] points = new MazePoint[mazeProperties.width][mazeProperties.height];
	
		Point startPoint = fillMazeArray(points,mazeProperties);
		
		Point exit = mazeProperties.exits.get(exitIndex);
		
        MazePoint selectedExit = points[exit.x][exit.y];

        ArrayList<Square> route = buildRoute(selectedExit, startPoint);
        
        Printer.printResult(points, route);
        
		return route.size();
	}

	public static Point fillMazeArray (MazePoint [][] points, MazeProperties mazeProperties) throws IOException {
		
		String line = null;
		int lineCounter = 0;
        BufferedReader br = new BufferedReader(new FileReader(mazeProperties.filePath));

        Point startPoint = null;
        MazePoint mazePoint = null;
       
        while (null != ( line = br.readLine())) {
        	MazePoint previous = null;
        	
        	for (int i=0;i<line.length();i++) {
        		char square = line.charAt(i);
        		mazePoint = new MazePoint(i, lineCounter);
        		
        		switch (square) {
        			case '^' : 
        				startPoint = new Point(i, lineCounter);
        				mazePoint.setStart(true);
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
        return startPoint;
	}
	
	/**
	 * Starts from certain exit point towards starting point.
	 * 
	 * @param fromExitPoint
	 * @param toStartPoint
	 * @return
	 */
	public static ArrayList<Square> buildRoute(MazePoint fromExitPoint, Point toStartPoint) {
		MazePoint previousPoint = null;
		MazePoint exit = fromExitPoint;
		ArrayList<Square> route = new ArrayList<>();
		route.add(exit);
		
        for (int i = 0;i<maxPathLength; i++) {
        	
        	fromExitPoint = fromExitPoint.getMostValidNeighbour(exit, toStartPoint);
        	
        	if (fromExitPoint == null) {
        		Printer.pl("route is not found\n");
        		break;
        	}
        	else if (fromExitPoint.isStart()) {
        		Printer.pl("route is found\n");
        		break;
        	}

        	route.add(fromExitPoint);
        	
        	if (previousPoint != null)
        		fromExitPoint.removePoint(previousPoint);
        	
        	previousPoint = fromExitPoint;        	
        }
        return route;
	}	
}
