package net.gmx.nosefish.fishylib.datastructures;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ConcurrentMapWithSet<K,V>{
	private final ConcurrentMap<K, Set<V>> map = new ConcurrentHashMap<>(16, 0.9F, 2);
	
	public void clear() {
		map.clear();
		
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public void put(K key, V value) {
		while (true) {
			Set<V> existingValues = map.get(key);
			// if we're really unlucky, the entry gets removed after we
			// retrieve its value. So we have to do the atomic putIfAbsent *afterwards*.
			if (existingValues != null) {
				// is a synchronizedSet
				existingValues.add(value);
			}
			// create the entry if it doesn't exist
			Set<V> newIdSet = Collections.synchronizedSet(new LinkedHashSet<V>(4, 0.9F));
			newIdSet.add(value);
			map.putIfAbsent(key, newIdSet);
			// if we're among the least lucky fuckers in the world, the key was removed after the first get
			// and added back in before the putIfAbsent. Check if our value is in there
			// and retry if it isn't.
			try {
				if (map.get(key).contains(value)) {
					// looking good
					break;
				}
			} catch(NullPointerException e) {
				// Are you kidding me? That key was right there a nanosecond ago!
				// All that work for nothing! Screw you guys, I'm going home!
				break;
			}
		}
	}

	public Set<java.util.Map.Entry<K, Set<V>>> entrySet() {
		return map.entrySet();
	}

	public Set<V> get(K key) {
		return map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Set<V> removeKey(K key) {
		return map.remove(key);
	}
	
	/**
	 * Removes a value from all value sets in the map. Removes the associated
	 * key if the value set is empty.
	 * 
	 * @param value
	 * @return the keys that were removed from the map because they had no more values
	 */
	public List<K> removeValue(V value) {
		List<K>removedKeys = new LinkedList<>();
		for (Entry<K, Set<V>> entry : map.entrySet()) {
			Set<V> valueSet = entry.getValue();
			valueSet.remove(value);
			if (valueSet.isEmpty()) {
				removedKeys.add(entry.getKey());
				map.remove(entry.getKey());
			}
		}
		return removedKeys;
	}

	/**
	 * Caution: slow! O(n)!
	 * 
	 * @return
	 */
	public int size() {
		return map.size();
	}

	public Collection<Set<V>> values() {
		return map.values();
	}

	public boolean remove(Object key, Object value) {
		return map.remove(key, value);
	}
}
