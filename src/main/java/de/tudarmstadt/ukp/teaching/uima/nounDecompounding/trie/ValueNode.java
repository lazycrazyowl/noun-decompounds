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
 * A tree node with a value object
 * @author Jens Haase <je.haase@googlemail.com>
 *
 * @param <V> The value object class
 */
public class ValueNode<V> {

	V value;
	List<ValueNode<V>> children = new ArrayList<ValueNode<V>>();
	
	public ValueNode() {
		this(null);
	}
	
	public ValueNode(V value) {
		this.value = value;
	}
	
	/**
	 * Returns the value object of this node
	 * @return
	 */
	public V getValue() {
		return value;
	}
	
	/**
	 * Sets the value object of this node
	 * @param value
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
	/**
	 * Returns a children for this node
	 * @return
	 */
	public List<ValueNode<V>> getChildren() {
		return children;
	}
	
	/**
	 * Returns all Children values
	 * @return
	 */
	public List<V> getChildrenValues() {
		List<ValueNode<V>> values = this.getChildren();
		List<V> result = new ArrayList<V>();
		
		for (ValueNode<V> value : values) {
			result.add(value.getValue());
		}
		
		return result;
	}
	
	/**
	 * Sets the children for this node
	 * @param children
	 */
	public void setChildren(List<ValueNode<V>> children) {
		this.children = children;
	}
	
	/**
	 * Adds a child object to this node
	 * @param node
	 */
	public void addChild(ValueNode<V> node) {
		this.children.add(node);
	}
	
	/**
	 * Checks if this node has children
	 * @return
	 */
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	/**
	 * Checks if the node is a leaf node
	 * @return
	 */
	public boolean isLeaf() {
		return !this.hasChildren();
	}
}
