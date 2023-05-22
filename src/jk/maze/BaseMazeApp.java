package jk.maze;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BaseMazeApp {
	protected static int maxPathLength= 150;
	private static String filePath = ".\\Mazes\\maze-task-first_(2).txt";
	
	
	public static String getPathToFile(String[] args) {
		if (args != null && args.length >0 && args[0] != null && args[0].length()>0)
			return args[0];
		
		return filePath;
	}
	
	public static MazeProperties getMazeProperties(String filePath) throws IOException {
		
		MazeProperties mazeProperties = new MazeProperties();
		mazeProperties.filePath = filePath;
		
		String line = null;
		int lineCounter = 0;
		int columnCounter = 0;
		
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
        			mazeProperties.exits.add(new Point(exitXCoordinate -1, exitYCoordinate));
        		}
        	}
        }
        br.close();
        
        mazeProperties.height= lineCounter;
        mazeProperties.width = columnCounter;
        
        return mazeProperties;
	}
	
}
