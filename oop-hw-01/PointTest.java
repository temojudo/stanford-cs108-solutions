import junit.framework.TestCase;

import java.util.Random;

// Provided testing for simple Point class.

public class PointTest extends TestCase {
	public void test1() {
		// test basic x/y/shift behavior
		Point p = new Point(1, 2);
		assertEquals(1.0, p.getX());
		assertEquals(2.0, p.getY());
		Point p2 = p.shiftedPoint(10, -10);
		assertEquals(11.0, p2.getX());
		assertEquals(-8.0, p2.getY());	
	}
	
	public void test2() {
		// test distance() and equals()
		Point p1 = new Point(1, 1);
		Point p2 = new Point(1, 4);
		assertEquals(3.0, p1.distance(p2));
		assertEquals(3.0, p2.distance(p1));
		assertFalse(p1.equals(p2));
		assertTrue(p1.equals(new Point(p1)));
		assertEquals(p1, p2.shiftedPoint(0, -3));
	}

	public void test3() {
		// test toString method
		Point p;
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			double x = r.nextDouble();
			double y = r.nextDouble();
			p = new Point(x, y);
			assertEquals(x + " " + y, p.toString());
		}
	}

	public void test4() {
		Point p1 = new Point(1.3, 7);
		Point p2 = new Point(1.3, 9);
		assertEquals(false, p1.equals(p2));
		assertEquals(false, p1.equals(null));
		assertEquals(false, p2.equals("sjndkjasa"));

		p2 = new Point(213, 7);
		assertEquals(false, p1.equals(p2));
	}

}

