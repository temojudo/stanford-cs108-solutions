// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid. Does not make a copy.
	 *
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}

	/**
	 * Returns the area for the given char in the grid. (see handout).
	 *
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int num = getAppearance(ch, 0, 0, grid.length - 1, grid[0].length - 1);
		int ans = grid.length * grid[0].length;

		if (num == 0) {
			return 0;
		}

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				for (int i = row; i < grid.length; i++) {
					for (int j = col; j < grid[i].length; j++) {
						if (num == getAppearance(ch, row, col, i, j)) {
							ans = Math.min(ans, (i - row + 1) * (j - col + 1));
						}
					}
				}
			}
		}

		return ans;
	}

	private int getAppearance(char ch, int startX, int startY, int endX, int endY) {
		int ans = 0;
		for (int i = startX; i <= endX; i++) {
			for (int j = startY; j <= endY; j++) {
				if (grid[i][j] == ch) {
					ans++;
				}
			}
		}

		return ans;
	}

	/**
	 * Returns the count of '+' figuans in the grid (see handout).
	 *
	 * @return number of + in grid
	 */
	public int countPlus() {
		int ans = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (grid[row][col] != '\0' && isMiddle(row, col)) {
					ans++;
				}
			}
		}

		return ans;
	}

	private boolean isMiddle(int row, int col) {
		int right = calcLength(row, col, 1, 0);
		int left = calcLength(row, col, -1, 0);
		int down = calcLength(row, col, 0, 1);
		int up = calcLength(row, col, 0, -1);

		int[] mas = new int[] {left, right, up, down};
		for (int i = 1; i < mas.length; i++) {
			if (mas[i] != mas[0]) {
				return false;
			}
		}

		return mas[0] > 1;
	}

	private int calcLength(int row, int col, int diffX, int diffY) {
		char ch = grid[row][col];
		int ans = 0;
		while (true) {
			if (row < 0 || row >= grid.length ||
					col < 0 || col >= grid[row].length ||
					grid[row][col] != ch) {
				break;
			}

			ans++;
			row += diffX;
			col += diffY;
		}

		return ans;
	}

}
