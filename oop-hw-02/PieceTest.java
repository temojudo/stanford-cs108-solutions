import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece[] p;

	protected void setUp() throws Exception {
		super.setUp();

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		p = Piece.getPieces();
	}

	// Here are some sample tests to get you started

	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}

	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] { 0, 0, 0 }, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] { 1, 0, 1 }, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] { 0, 0, 1 }, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] { 1, 0 }, sRotated.getSkirt()));
	}

	public void testEquals() {
		pyr3 = p[Piece.PYRAMID].fastRotation().fastRotation();
		pyr4 = pyr3.fastRotation();

		assertFalse(p[Piece.SQUARE].equals(Double.valueOf(3.7)));
		assertFalse(p[Piece.STICK].equals("wohoo"));

		assertEquals(p[Piece.SQUARE].fastRotation(), new Piece(Piece.SQUARE_STR));
		assertEquals(p[Piece.STICK].fastRotation().fastRotation(), new Piece(Piece.STICK_STR));
		assertEquals(pyr4.fastRotation(), pyr1);
		assertEquals(p[Piece.SQUARE].fastRotation(), new Piece(Piece.SQUARE_STR));
	}

	public void testWrongPiece() {
		boolean exception = false;
		String[] pieceStr = new String[] {"1 2 3 4 2 3 4 5",
										  "1 2 3 2 1 2 33 213 13",
								 		  "1 2 3 4 4",
										  "sad 312 21b21 hkb a 2"};
		for (int i = 0; i < p.length; i++) {
			try {
				Piece p = new Piece(pieceStr[i]);
			} catch (RuntimeException e) {
				exception = true;
			}
		}

		assertTrue(exception);
	}

	public void testCircle() {
		Piece[] p = Piece.getPieces();
		assertEquals(p[Piece.STICK], p[Piece.STICK].fastRotation().fastRotation());
		assertEquals(p[Piece.SQUARE], p[Piece.SQUARE].fastRotation());
		assertEquals(p[Piece.PYRAMID], p[Piece.PYRAMID].fastRotation().fastRotation().fastRotation().fastRotation());
	}

}
