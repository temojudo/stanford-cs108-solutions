//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	boolean[][] grid;

	/**
	 * Constructs a new instance with the given grid. Does not make a copy.
	 *
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		boolean[][] ans = new boolean[grid.length][grid[0].length];
		int currIndex = 0;
		for (int col = 0; col < grid[0].length; col++) {
			if (!toDelete(col)) {
				for (int row = 0; row < grid.length; row++) {
					ans[row][currIndex] = grid[row][col];
				}
				currIndex++;
			}
		}

		grid = ans;
	}

	private boolean toDelete(int col) {
		for (int row = 0; row < grid.length; row++) {
			if (!grid[row][col]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the internal 2d grid array.
	 *
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}
