package test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import jk.maze.MazeSolver;

class TestInputs {

	@Test
	void testReadInputWithNull() {
		try {
			int result = MazeSolver.readInput(null);
			assertEquals(-1, result);
		} catch (IOException e) {
			fail(e);
		}
	}
	
	@Test
	void testReadInputWithValue() {
		BufferedReader bf = new BufferedReader(new StringReader("1"));
		try {
			int result = MazeSolver.readInput(bf);
			assertEquals(1, result);
		} catch (IOException e) {
			fail(e);
		}
		finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
