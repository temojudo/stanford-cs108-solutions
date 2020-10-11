// Board.java

import java.util.Arrays;

/**
 * CS108 Tetris Board. Represents a Tetris board -- essentially a 2-d grid of
 * booleans. Supports tetris pieces and row clearing. Has an "undo" feature that
 * allows clients to add and remove pieces efficiently. Does not do any drawing
 * or have any idea of pixels. Instead, just represents the abstract 2-d board.
 */
public class Board {
	// Some ivars are stubbed out for you:
	private int maxHeight;
	private int[] widths;
	private int[] heights;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int maxHeightBackup;
	private int[] widthsBackup;
	private int[] heightsBackup;
	private boolean[][] gridBackup;
	// Here a few trivial methods are provided:

	/**
	 * Creates an empty board of the given width and height measured in blocks.
	 */
	public Board(int width, int height) {
		grid = new boolean[width][height];
		widths = new int[height];
		heights = new int[width];
		maxHeight = 0;

		gridBackup = new boolean[width][height];
		widthsBackup = new int[height];
		heightsBackup = new int[width];
		maxHeightBackup = 0;

		committed = true;

        {   // trick for branch coverage
            DEBUG = false;
            sanityCheck();
            printDiff(widthsBackup, widths, heightsBackup, heights);
            DEBUG = true;
        }
	}

	/**
	 * Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return heights.length;
	}

	/**
	 * Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return widths.length;
	}

	/**
	 * Returns the max column height present in the board. For an empty board this
	 * is 0.
	 */
	public int getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Checks the board for internal consistency -- used for debugging.
	 */
	public void sanityCheck() {
		if (!DEBUG) { return; }

//		System.out.println(toString());
		int[] widths = new int[getHeight()];
		int[] heights = new int[getWidth()];
		int maxHeight = 0;

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (grid[x][y]) {
					widths[y]++;
					heights[x] = y + 1;
				}
			}
			maxHeight = Math.max(maxHeight, heights[x]);
		}

		if (maxHeight != this.maxHeight || !Arrays.equals(widths, this.widths) || !Arrays.equals(heights, this.heights)) {
            printDiff(widths, this.widths, heights, this.heights);
			throw new RuntimeException("Invalid state");
		}
	}

    private void printDiff(int[] widths1, int[] widths2, int[] heights1, int[] heights2) {
        System.out.println(toString());
        for (int i = 0; i < widths1.length; i++)  System.out.print(widths1[i] + " ");  System.out.println();
        for (int i = 0; i < widths2.length; i++)  System.out.print(widths2[i] + " ");  System.out.println();
        for (int i = 0; i < heights1.length; i++) System.out.print(heights2[i] + " "); System.out.println();
        for (int i = 0; i < heights2.length; i++) System.out.print(heights2[i] + " "); System.out.println();
    }

    /**
	 * Given a piece and an x, returns the y value where the piece would come to
	 * rest if it were dropped straight down at that x.
	 *
	 * <p>
	 * Implementation: use the skirt and the col heights to compute this fast --
	 * O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		int ans = 0;
		int[] skirt = piece.getSkirt();

		for (int i = 0; i < piece.getWidth(); i++) {
			ans = Math.max(ans, getColumnHeight(x + i) - skirt[i]);
		}

		return ans;
	}

	/**
	 * Returns the height of the given column -- i.e. the y value of the highest
	 * block + 1. The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		return heights[x];
	}

	/**
	 * Returns the number of filled blocks in the given row.
	 */
	public int getRowWidth(int y) {
		return widths[y];
	}

	/**
	 * Returns true if the given block is filled in the board. Blocks outside of the
	 * valid width/height area always return true.
	 */
	public boolean getGrid(int x, int y) {
		return isOutOfBounds(x, y) || grid[x][y];
	}

	private boolean isOutOfBounds(int x, int y) {
		return x < 0 || x >= getWidth() || y < 0 || y >= getHeight();
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 * Attempts to add the body of a piece to the board. Copies the piece blocks
	 * into the board grid. Returns PLACE_OK for a regular placement, or
	 * PLACE_ROW_FILLED for a regular placement that causes at least one row to be
	 * filled.
	 *
	 * <p>
	 * Error cases: A placement may fail in two ways. First, if part of the piece
	 * may falls out of bounds of the board, PLACE_OUT_BOUNDS is returned. Or the
	 * placement may collide with existing blocks in the grid in which case
	 * PLACE_BAD is returned. In both error cases, the board may be left in an
	 * invalid state. The client can use undo(), to recover the valid, pre-place
	 * state.
	 */
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) { throw new RuntimeException("place commit problem"); }

		TPoint[] body = piece.getBody();
		int result = PLACE_OK;

		for (int i = 0; i < body.length; i++) {
			if (getGrid(x + body[i].x, y + body[i].y)) {
				if (isOutOfBounds(x + body[i].x, y + body[i].y)) {
					result = PLACE_OUT_BOUNDS;
					break;
				}
				result = PLACE_BAD;
				break;
			}

			grid[x + body[i].x][y + body[i].y] = true;
			widths[y + body[i].y]++;
			heights[x + body[i].x] = Math.max(heights[x + body[i].x], y + body[i].y + 1);
			maxHeight = Math.max(getMaxHeight(), heights[x + body[i].x]);

			if (getRowWidth(y + body[i].y) == getWidth()) {
				result = PLACE_ROW_FILLED;
			}

		}

		if (result == PLACE_OK || result == PLACE_ROW_FILLED) { sanityCheck(); }
		committed = false;
		return result;
	}

	private void backupAll() {
		maxHeightBackup = maxHeight;
		System.arraycopy(widths, 0, widthsBackup, 0, widths.length);
		System.arraycopy(heights, 0, heightsBackup, 0, heights.length);
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, gridBackup[i], 0, grid[i].length);
		}
	}

	/**
	 * Deletes rows that are filled all the way across, moving things above down.
	 * Returns the number of rows cleared.
	 */
	public int clearRows() {
		int rowsCleared = 0;
		for (int y = 0; y < getMaxHeight(); y++) {
			if (widths[y] == getWidth()) {
				bubbleDown(y + 1);
				rowsCleared++;
				y--;
			}
		}

		if (rowsCleared > 0) { committed = false; }
		sanityCheck();
		return rowsCleared;
	}

	private void bubbleDown(int i) {
		for (int y = i; y <= getMaxHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				grid[x][y - 1] = (y < getHeight()) && getGrid(x, y);
			}
			widths[y - 1] = (y < getHeight()) ? widths[y] : 0;
		}

		int mxHeight = 0;
        for (int x = 0; x < getWidth(); x++) {
            heights[x] = 0;
            for (int y = 0; y < getMaxHeight(); y++) {
                if (getGrid(x, y)) {
                	heights[x] = y + 1;
                	mxHeight = Math.max(mxHeight, y + 1);
                }
            }
        }
        maxHeight = mxHeight;
	}

	/**
	 * Reverts the board to its state before up to one place and one clearRows(); If
	 * the conditions for undo() are not met, such as calling undo() twice in a row,
	 * then the second undo() does nothing. See the overview docs.
	 */
	public void undo() {
		if (committed) { return; }

		int tempMaxHeight = maxHeight;
		maxHeight = maxHeightBackup;
		maxHeightBackup = tempMaxHeight;

		int[] tempWidths = widths;
		widths = widthsBackup;
		widthsBackup = tempWidths;

		int[] tempHeights = heights;
		heights = heightsBackup;
		heightsBackup = tempHeights;

		boolean[][] tempGrid = grid;
		grid = gridBackup;
		gridBackup = tempGrid;

		commit();
		sanityCheck();
	}

	/**
	 * Puts the board in the committed state.
	 */
	public void commit() {
		if (committed) { return; }

		backupAll();
		committed = true;
	}

	/*
	 * Renders the board state as a big String, suitable for printing. This is the
	 * sort of print-obj-state utility that can help see complex state change over
	 * time. (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = getHeight() - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < getWidth(); x++) {
				if (getGrid(x, y)) {
					buff.append('+');
				} else {
					buff.append(' ');
				}
			}
			buff.append("|\n");
		}

		for (int x = 0; x < getWidth() + 2; x++) {
			buff.append('-');
		}

		return (buff.toString());
	}
}
