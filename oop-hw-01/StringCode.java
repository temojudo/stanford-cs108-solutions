import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run. A a run is a series of
	 * adajcent chars that are the same.
	 *
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.equals("")) {
			return 0;
		}

		int max, curr;
		max = curr = 1;

		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) != str.charAt(i - 1)) {
				max = Math.max(max, curr);
				curr = 1;
			} else {
				curr++;
			}
		}

		return max;
	}

	/**
	 * Given a string, for each digit in the original string, replaces the digit
	 * with that many occurrences of the character following. So the string "a3tx2z"
	 * yields "attttxzzz".
	 *
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		if (str.equals("")) {
			return "";
		}

		String ans = "";
		for (int i = 0; i < str.length() - 1; i++) {
			if (Character.isDigit(str.charAt(i))) {
				for (int j = 0; j < str.charAt(i) - '0'; j++) {
					ans += str.charAt(i + 1);
				}
			} else {
				ans += str.charAt(i);
			}
		}

		if (!Character.isDigit(str.charAt(str.length() - 1))) {
			ans += str.charAt(str.length() - 1);
		}
		return ans;
	}

	/**
	 * Given 2 strings, consider all the substrings within them of length len.
	 * Returns true if there are any such substrings which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (a == null || b == null) {
			return false;
		}

		if (a.length() < len || b.length() < len) {
			return false;
		}

		Set<String> set = new HashSet<>();
		for (int i = 0; i < a.length() - len + 1; i++) {
			set.add(a.substring(i, i + len));
		}

		for (int i = 0; i < b.length() - len + 1; i++) {
			if (set.contains(b.substring(i, i + len))) {
				return true;
			}
		}

		return false;
	}

}
