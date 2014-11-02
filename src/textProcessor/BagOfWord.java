package textProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BagOfWord {
	private final Map<String, Integer> map;
	private int size;

	public BagOfWord() {
		map = new HashMap<String, Integer>();
		size = -1;
	}

	public void upsert(String term) {
		if (term == null || term.isEmpty()) {
			return;
		}

		if (map.containsKey(term)) { // increment
			map.put(term, map.get(term) + 1);
		} else { // new term
			map.put(term, 1);
		}
	}

	public Set<Entry<String, Integer>> entrySet() {
		return map.entrySet();
	}

	public int size() {
		if (size == -1) {
			size = 0;
			for (int tf : map.values()) {
				size += tf;
			}
		}
		return size;
	}

	public int length() {
		return map.size();

	}

	public Map<String, Integer> getMap() {
		return map;
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
