package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie;

import java.util.ArrayList;
import java.util.List;

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
