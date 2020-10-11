import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {

	private class Spot implements Comparable<Spot> {
		private int row;
		private int col;
		private int square;

		public Spot(int row, int col) {
			this.row = row;
			this.col = col;
			square = (col / PART) * PART + row / PART;
		}

		public void setValue(int value) {
			squares.get(square).add(value);
			columns.get(col).add(value);
			rows.get(row).add(value);
			grid[row][col] = value;
		}

		public void reset() {
			squares.get(square).remove(grid[row][col]);
			columns.get(col).remove(grid[row][col]);
			rows.get(row).remove(grid[row][col]);
			grid[row][col] = 0;
		}

		public Set<Integer> possibleValues() {
			Set<Integer> possible = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
			possible.removeAll(squares.get(square));
			possible.removeAll(rows.get(row));
			possible.removeAll(columns.get(col));
			return possible;
		}

		@Override
		public int compareTo(Sudoku.Spot spot) {
			return possibleValues().size() - spot.possibleValues().size();
		}

	}

	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");

	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");

	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	private boolean solutionFound;
	private long elapsedTime;

	private int[][] grid;
	private int[][] solution;

	private List<Spot> spots;
	private List<Set<Integer>> squares;
	private List<Set<Integer>> rows;
	private List<Set<Integer>> columns;

	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	private static void runSudokuWithAdversary(int[][] grid) {
		Sudoku sudoku;
		sudoku = new Sudoku(grid);
		System.out.println(sudoku);

		int count = sudoku.solve();
		System.out.println("solutions: " + count);
		System.out.println("elapsed: " + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		runSudokuWithAdversary(easyGrid);
		runSudokuWithAdversary(mediumGrid);
		runSudokuWithAdversary(hardGrid);
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		// YOUR CODE HERE
		grid = ints;
		solutionFound = false;

		generateColumns();
		generateRows();
		generateSquares();
		generateSpots();
	}

	private void generateSquares() {
		int startRow = 0;
		int startCol = 0;
		squares = new ArrayList<>();

		for(int i = 0; i < SIZE; i++) {
			squares.add(new HashSet<>());
			for(int row = startRow; row < startRow + PART; row++) {
				for (int col = startCol; col < startCol + PART; col++) {
					if (grid[row][col] != 0 && !squares.get(i).add(grid[row][col])) {
						throw new RuntimeException("Equal numbers in same part");
					}
				}
			}

			startRow += PART;
			if((startRow / PART) % PART == 0) {
				startRow = 0;
				startCol += PART;
			}
		}
	}

	private void generateColumns() {
		columns = new ArrayList<>();
		for(int col = 0; col < SIZE; col++) {
			columns.add(new HashSet<>());
			for (int row = 0; row < SIZE; row++) {
				if (grid[row][col] != 0 && !columns.get(col).add(grid[row][col])) {
					throw new RuntimeException("Equal numbers in same column");
				}
			}
		}
	}

	private void generateRows() {
		rows = new ArrayList<>();
		for(int row = 0; row < SIZE; row++) {
			rows.add(new HashSet<>());
			for(int col = 0; col < SIZE; col++) {
				if(grid[row][col] != 0 && !rows.get(row).add(grid[row][col])) {
					throw new RuntimeException("Equal numbers in same raw");
				}
			}
		}
	}

	private int[][] copyGrid(int[][] arr) {
		int[][] copy = new int[arr.length][arr[0].length];
		for(int i = 0; i < arr.length; i++) { System.arraycopy(arr[i], 0, copy[i], 0, arr[i].length); }
		return copy;
	}

	private void generateSpots() {
		spots = new LinkedList<>();
		for(int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				if (grid[row][col] == 0) {
					spots.add(new Spot(row, col));
				}
			}
		}
		Collections.sort(spots);
	}

	private int recSolve(LinkedList<Spot> workingSpots) {
		if(workingSpots.isEmpty()) {
			if(!solutionFound) {
				solution = copyGrid(grid);
				solutionFound = true;
			}
			return 1;
		}

		int ans = 0;
		Spot currentSpot = workingSpots.removeFirst();
		Set<Integer> spotVals = currentSpot.possibleValues();

		if(!spotVals.isEmpty()) {
			for (Integer spotValue : spotVals) {
				currentSpot.setValue(spotValue);
				ans += recSolve(workingSpots);
				if (ans >= MAX_SOLUTIONS) { return MAX_SOLUTIONS; }
				currentSpot.reset();
			}
		}

		workingSpots.addFirst(currentSpot);
		return ans;
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		// YOUR CODE HERE
		elapsedTime = System.currentTimeMillis();
		int numSolutions = recSolve(new LinkedList<>(spots));
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		return numSolutions;
	}
	
	public String getSolutionText() {
		// YOUR CODE HERE
		if (!solutionFound) { return ""; }

		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < SIZE; i++) {
			buffer.append(Arrays.toString(solution[i])).append("\n");
		}

		String text = new String(buffer);
		return text.replaceAll("[,\\[\\]]", "");
	}
	
	public long getElapsed() {
		// YOUR CODE HERE
		return elapsedTime;
	}
}
