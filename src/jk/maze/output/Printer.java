package jk.maze.output;

import java.util.List;

import jk.maze.MazePoint;

public class Printer {

	public static void printResult(MazePoint [][] points, List<Square> route) {
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
		Printer.pl("steps: " + route.size());
	}
	
	public static void p(Object o) {
		System.out.print(o);
	}
	
	public static void pl(Object o) {
		System.out.println(o);
	}
}
