
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	List<T> rules;
	HashMap<T, HashSet<T>> noFollow;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 *
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		this.rules = rules;
		noFollow = new HashMap<>();

		for (int i = 0; i < rules.size() - 1; i++) {
			T first = rules.get(i);
			T second = rules.get(i + 1);
			if (first != null && second != null) {
				if (noFollow.containsKey(first)) {
					noFollow.get(first).add(second);
				} else {
					HashSet<T> newRule = new HashSet<>();
					newRule.add(second);
					noFollow.put(first, newRule);
				}
			}
		}

	}

	/**
	 * Returns the set of elements which should not follow the given element.
	 *
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if (elem == null || !noFollow.containsKey(elem)) {
			return Collections.emptySet();
		}
		return (Set) noFollow.get(elem);
	}

	/**
	 * Removes elements from the given list that violate the rules (see handout).
	 *
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		if (list.isEmpty()) {
			return;
		}

		for (int i = 1; i < list.size(); i++) {
			if (!noFollow.containsKey(list.get(i - 1))) { continue; }
			if (noFollow.get(list.get(i - 1)).contains(list.get(i))) {
				list.remove(i);
				i--;
			}
		}
	}

}
