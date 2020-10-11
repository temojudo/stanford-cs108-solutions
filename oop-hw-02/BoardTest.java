import junit.framework.TestCase;

public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.

	protected void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		b.place(pyr1, 0, 0);
		b.commit();
	}

	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	public void testSample2() {
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	public void testDropHeight() {
		assertEquals(1, b.dropHeight(pyr2, 1));
		assertEquals(2, b.dropHeight(pyr1, 0));
		assertEquals(2,b.dropHeight(new Piece(Piece.SQUARE_STR), 0));
	}

	public void testBadPlace() {
		int pb = b.place(pyr1, 0, 0);
		assertEquals(Board.PLACE_BAD, pb);

		b.commit();
		int out = b.place(new Piece(Piece.SQUARE_STR), 2, 3);
		assertEquals(Board.PLACE_OUT_BOUNDS, out);

		b.commit();
		out = b.place(pyr2, -2, 2);
		assertEquals(Board.PLACE_OUT_BOUNDS, out);

		b.commit();
		out = b.place(pyr3, 0, -2);
		assertEquals(Board.PLACE_OUT_BOUNDS, out);

		b.commit();
		out = b.place(pyr4, 0, 8);
		assertEquals(Board.PLACE_OUT_BOUNDS, out);
	}

	public void testGoodPlace() {
		Board board = new Board(4, 6);

		int ok = board.place(new Piece(Piece.SQUARE_STR), 0, 0);
		assertEquals(Board.PLACE_OK, ok);

		boolean exception = false;
		Piece[] pieces = new Piece[] {pyr1, pyr2, pyr3, pyr4};
		for (int i = 0; i < pieces.length; i++) {
			try {
				board.place(pieces[i], i, i + 3);
			} catch (RuntimeException e) {
				exception = true;
			}
			board.commit();
		}
		assertTrue(exception);

		board.commit();
		int rf = board.place(new Piece(Piece.SQUARE_STR), 2, 0);
		assertEquals(Board.PLACE_ROW_FILLED, rf);
	}

	public void testClearRows() {
		int result = b.place(sRotated, 1, 1);
		System.out.println(b.toString());
		assertEquals(Board.PLACE_OK, result);
		assertEquals(3, b.getRowWidth(0));

		int cleared = b.clearRows();
		assertEquals(1, cleared);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));

		cleared = b.clearRows();
		assertEquals(0, cleared);
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));

		Board board = new Board(4, 8);
		board.place(new Piece(Piece.SQUARE_STR), 0, 0);
		assertEquals(0, board.clearRows());

		board.commit();
		board.place(new Piece(Piece.SQUARE_STR), 2, 0);
		assertEquals(2, board.clearRows());

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				assertFalse(board.getGrid(i, j));
			}
		}
	}

	public void testUndo() {
		b.undo();
		int cleared = b.clearRows();
		assertEquals(1, cleared);

		b.undo();
		cleared = b.clearRows();
		assertEquals(1, cleared);

		b.commit();
		int result = b.place(pyr1, 0, 3);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		assertEquals(3, b.getRowWidth(3));
	}

}
