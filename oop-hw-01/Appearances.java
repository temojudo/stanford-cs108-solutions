import java.util.*;

public class Appearances {

	/**
	 * Returns the number of elements that appear the same number of times in both
	 * collections. Static method. (see handout).
	 *
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		Map<T, Integer> appearenceA = new HashMap<>();
		Map<T, Integer> appearenceB = new HashMap<>();

		fillAppearances(a, appearenceA);
		fillAppearances(b, appearenceB);

		return compareAppearances(appearenceA, appearenceB);
	}

	private static <T> int compareAppearances(Map<T, Integer> appearenceA, Map<T, Integer> appearenceB) {
		int count = 0;
		for (T elem : appearenceA.keySet()) {
			if (appearenceB.containsKey(elem) && appearenceA.get(elem) == appearenceB.get(elem)) {
				count++;
			}
		}
		return count;
	}

	private static <T> void fillAppearances(Collection<T> col, Map<T, Integer> appearence) {
		for (T elem : col) {
			if (appearence.containsKey(elem)) {
				int k = appearence.get(elem) + 1;
				appearence.put(elem, k);
			} else {
				appearence.put(elem, 1);
			}
		}
	}

}
