package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {

	private final Map<K, V> cachedData;
	
	public LRUCache() {
		this(1000);
	}
	
	public LRUCache(final int size) {
		this.cachedData = (Map<K,V>) Collections.synchronizedMap(new LinkedHashMap<K, V>(size+1, .75f, true) {
			private static final long serialVersionUID = 5478686188905105193L;

			@Override  
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest)  
            {  
                return size() > size;  
            }
		});
	}
	
	public V put(K key, V value) {
		return cachedData.put(key, value);
	}
	
	public V get(K key) {
		return cachedData.get(key);
	}
	
	public boolean containsKey(K key) {
		return cachedData.containsKey(key);
	}
}
