import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}
	
	// Add more tests
	public void testSameCount3() {
		List<Character> l = Arrays.asList('a', 'a', 'c', 'k', 'a', 's', 'd', 'p', 'p');
		assertEquals(3, Appearances.sameCount(l, Arrays.asList('a', 'k', 'b', 'c', 'd')));
		assertEquals(2, Appearances.sameCount(l, Arrays.asList('a', 'a', 'b', 'a', 'd')));
		assertEquals(0, Appearances.sameCount(l, Arrays.asList('a', 'a', 'a', 'a', 'a')));
		assertEquals(2, Appearances.sameCount(l, Arrays.asList('m', 'k', 'w', 'c', 'n')));
	}

	public void testSameCount4() {
		List<Boolean> l = Arrays.asList(true, true, false, true);
		assertEquals(0, Appearances.sameCount(l, Arrays.asList(true, false, false)));
		assertEquals(1, Appearances.sameCount(l, Arrays.asList(true, true, false, false, true)));
		assertEquals(1, Appearances.sameCount(l, Arrays.asList(true, false)));
		assertEquals(2, Appearances.sameCount(l, Arrays.asList(true, true, true, false)));
	}
}
