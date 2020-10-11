import junit.framework.TestCase;

public class TPointTest extends TestCase {

    public void testTestEquals() {
        TPoint t1 = new TPoint(1, 3);
        TPoint t2 = new TPoint(1, 3);
        TPoint t3 = new TPoint(t2);

        assertEquals(t1, t2);
        assertEquals(t2, t3);
        assertEquals(t1, t1);

        assertFalse(t1.equals(Double.valueOf(23.3)));
        assertFalse(t2.equals(new TPoint(1, 5)));
        assertFalse(t3.equals(new TPoint(7, 3)));
    }

    public void testTestToString() {
        TPoint p1 = new TPoint(3, 9);
        TPoint p2 = new TPoint(23, 4);

        assertEquals("(3,9)", p1.toString());
        assertEquals("(23,4)", p2.toString());
    }
}