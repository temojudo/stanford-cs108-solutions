// TabooTest.java
// Taboo class tests -- nothing provided.

import java.lang.reflect.Array;
import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
    public void testNoFollow() {
        List<Character> rules = Arrays.asList('a', 'c', 'a', 'b');
        Taboo<Character> tb = new Taboo<Character>(rules);
        assertEquals(Collections.emptySet(), tb.noFollow('x'));
        assertEquals(new HashSet<Character>(Arrays.asList('b', 'c')), tb.noFollow('a'));
        assertEquals(new HashSet<Character>(Arrays.asList('a')), tb.noFollow('c'));
    }

    public void testNoFollowNullElements() {
        List<Character> rules = Arrays.asList('a', 'c', null, 'c', 'a', 'b');
        Taboo<Character> tb = new Taboo<Character>(rules);
        assertEquals(Collections.emptySet(), tb.noFollow('z'));
        assertEquals(new HashSet<Character>(Arrays.asList('a')), tb.noFollow('c'));
        assertEquals(Collections.emptySet(), tb.noFollow(null));
    }

    public void testReduce() {
        List<Character> list = new ArrayList<Character>(
                Arrays.asList('a', 'c', 'b', 'x', 'c', 'a'));
        List<Character> rules = Arrays.asList('a', 'c', 'a', 'b');
        Taboo<Character> tb = new Taboo<Character>(rules);
        tb.reduce(list);
        assertEquals(Arrays.asList('a', 'x', 'c'), list);
    }

    public void testReduceNullList() {
        List<Character> list = Collections.emptyList();
        List<Character> rules = Arrays.asList('a', 'c', 'a', 'b');
        Taboo<Character> tb = new Taboo<Character>(rules);
        tb.reduce(list);
        assertEquals(Collections.emptyList(), list);
    }

}
