
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', '\0'},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
		assertEquals(0, cg.charArea('3'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', '\0'},
				{'b', '\0', 'b'},
				{'\0', '\0', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testCharArea3() {
		char[][] grid = new char[][] {
				{'c', 'a', 's'},
				{'b', '\0', 'b'},
				{'\0', '\0', 'a'},
				{'s', 'c', 'b'},
				{'a', 'd', '\0'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(15, cg.charArea('a'));
		assertEquals(9, cg.charArea('b'));
		assertEquals(8, cg.charArea('c'));
		assertEquals(1, cg.charArea('d'));
		assertEquals(12, cg.charArea('s'));
	}

	public void testCountPlus() {
		CharGrid cg = new CharGrid(new char[][] {
				{'\0', '\0', 'p', '\0', 'd', '\0', '\0', '\0', 'd'},
				{'\0', '\0', 'p', '\0', '\0', '\0', '\0', 'x', '\0'},
				{'p', 'p', 'p', 'p', 'p', '\0', 'x', 'x', 'x'},
				{'\0', '\0', 'p', '\0', '\0', 'y', '\0', 'x', '\0'},
				{'\0', '\0', 'p', '\0', 'y', 'y', 'y', '\0', '\0'},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{'\0', '\0', 'x', 'x', '\0', 'y', '\0', '\0', '\0'}
		});

		assertEquals(2, cg.countPlus());
	}
	
}
