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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * A key/value tree node
 * @author Jens Haase <je.haase@googlemail.com>
 *
 * @param <K> The Key object
 * @param <V> The value object
 */
public class KeyValueNode<K, V> {

	K key;
	V value;
	List<KeyValueNode<K, V>> children = new ArrayList<KeyValueNode<K,V>>();
	
	/**
	 * Creates a tree node for a given key and value
	 * @param key The key of this node
	 * @param value The value of the node
	 */
	public KeyValueNode(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Returns the key object
	 * @return
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Set a new key to this object
	 * @param key
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * Returns the value object
	 * @return
	 */
	public V getValue() {
		return value;
	}

	/**
	 * Sets a new value for this object
	 * @param value
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
	/**
	 * Checks if this node has children
	 * @return
	 */
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	/**
	 * Gets a child object for a given key.
	 * If a node with the given key do not exist NULL is returned.
	 * @param key
	 * @return
	 */
	public KeyValueNode<K, V> getChild(K key) {
		for (KeyValueNode<K, V> node : this.children) {
			if (node.key.equals(key)) {
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if this node has a child with a given key
	 * @param key
	 * @return
	 */
	public boolean hasChild(K key) {
		return (this.getChild(key) != null);
	}
	
	/**
	 * Adds a child object to this node
	 * @param node
	 */
	public void addChild(KeyValueNode<K,V> node) {
		this.children.add(node);
	}
	
	/**
	 * Adds child object to this node
	 * @param key
	 * @param value
	 */
	public void addChild(K key, V value) {
		this.addChild(new KeyValueNode<K, V>(key, value));
	}

	/**
	 * Returns all children of the node
	 * @return
	 */
	public List<KeyValueNode<K, V>> getChildren() {
		return children;
	}

	/**
	 * Set a list of children to this node
	 * @param children
	 */
	public void setChildren(List<KeyValueNode<K, V>> children) {
		this.children = children;
	}
	
	/**
	 * Checks if the node is a leaf node
	 * @return
	 */
	public boolean isLeaf() {
		return !this.hasChildren();
	}
}
