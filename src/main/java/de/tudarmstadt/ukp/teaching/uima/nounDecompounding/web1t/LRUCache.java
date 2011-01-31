/**
 * Copyright (c) 2010 Jens Haase <je.haase@googlemail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Least recently used cache.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 *
 * @param <K> A key class
 * @param <V> A value class
 */
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
