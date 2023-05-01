package jk.maze;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
public class MazeSolver {

	private static int height = 0, width = 0, maxPathLength= 150;
	private static String filePath = ".\\Mazes\\maze-task-first_(2).txt"; // c:\\temp
	//private static String filePath = "c:\\temp\\maze-task-second_(2).txt";
	
	public static void main(String[] args) throws IOException {
		try {
			filePath = getPathToFile(args);
			
			ArrayList<Point> exits = getHeightWidthAndExits(args);
	        
	        p("lines " + height + " columns " + width + " exits " + exits.size());
	    	pl("");
	    	
			int selectedExit = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while ((selectedExit = askExitNumber(reader, exits.size())) > 0) {
				pl("steps " + resolveRoute(exits, selectedExit-1) + "\n");
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		pl("finished");
	}

	public static String getPathToFile(String[] args) {
		if (args != null && args.length >0 && args[0] != null && args[0].length()>0)
			return args[0];
		
		return filePath;
	}
	
	public static int askExitNumber(BufferedReader reader, int exitCount) throws IOException {
		pl("Give index of exit, or 0 for quit please.\n");
		int selection = Integer.parseInt(reader.readLine());
		while ( selection > exitCount || selection < 0) {
			pl("Number is not valid.\n");
			try {
				selection = Integer.parseInt(reader.readLine());
			} catch (NumberFormatException e) {
				selection = exitCount +1;
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return selection;
	}
	
	public static ArrayList<Point> getHeightWidthAndExits(String[] args) throws IOException {
	
		String line = null;
		int lineCounter = 0;
		int columnCounter = 0;
		ArrayList<Point> exits = new ArrayList<>();
		
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        while (null != ( line = br.readLine())) {
        	if (columnCounter == 0)
        		columnCounter = line.length();

        	lineCounter++;

        	if (line.indexOf('E')>-1) {
        		int exitXCoordinate = 0;
        		int exitYCoordinate = lineCounter;
        		while (line.indexOf('E',exitXCoordinate)>0) {
        			exitXCoordinate = line.indexOf('E',exitXCoordinate)+1;
        			exits.add(new Point(exitXCoordinate -1, exitYCoordinate));
        		}
        	}
        }
        br.close();
        
        height= lineCounter;
        width = columnCounter;
        
        return exits;
	}
	
	public static int resolveRoute(ArrayList<Point> exits, int exitIndex) throws IOException {
		MazePoint [][] points = new MazePoint[width][height];
	
		Point startPoint = fillMazeArray(points,exits);
		Point exit = exits.get(exitIndex);
		
        MazePoint selectedExit = points[exit.x][exit.y];

        ArrayList<MazePoint> route = buildRoute(selectedExit, startPoint);
        
        printResult(points, route);
        
		return route.size();
	}

	public static Point fillMazeArray (MazePoint [][] points, ArrayList<Point> exits) throws IOException {
		
		String line = null;
		int lineCounter = 0;
        BufferedReader br = new BufferedReader(new FileReader(filePath));

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
	public static ArrayList<MazePoint> buildRoute(MazePoint fromExitPoint, Point toStartPoint) {
		MazePoint previousPoint = null;
		MazePoint exit = fromExitPoint;
		ArrayList<MazePoint> route = new ArrayList<>();
		route.add(exit);
		
        for (int i = 0;i<maxPathLength; i++) {
        	
        	fromExitPoint = fromExitPoint.getMostValidNeighbour(exit, toStartPoint);
        	
        	if (fromExitPoint == null) {
        		pl("route is not found\n");
        		break;
        	}
        	else if (fromExitPoint.isStart()) {
        		pl("route is found\n");
        		break;
        	}

        	route.add(fromExitPoint);
        	
        	if (previousPoint != null)
        		fromExitPoint.removePoint(previousPoint);
        	
        	previousPoint = fromExitPoint;        	
        }
        return route;
	}
	
	public static void printResult(MazePoint [][] points, ArrayList<MazePoint> route) {
		for (int i=0;i<points[0].length;i++) {
        	for (int j=0;j<points.length;j++) {
        		MazePoint m = points[j][i];
        		if (route.contains(m))
        			p(". ");
        		else if (m.isValidPathMember())
        			p("  ");
        		else if (m.isWall())
        			p("# ");
        		else if (m.isExit())
        			p("E ");
        		else if (m.isConnectedToExit() && !m.isConnectedToStart())
        			p("  ");
        		else if (m.isPath() && !m.isConnectedToExit())
        			p("  ");
        		else if (m.isStart())
        			p("^ ");
            }
        	pl("");
        }
	}
	
	public static void p(Object o) {
		System.out.print(o);
	}
	
	public static void pl(Object o) {
		System.out.println(o);
	}
	
}