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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.ValueNode;

/**
 * A split tree. Holds all splits in a tree structure.
 * This can help to see the how the split algorithm works
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class SplitTree {

	private ValueNode<Split> root;

	public SplitTree(String word) {
		this.root = new ValueNode<Split>(Split.createFromString(word));
	}
	
	public ValueNode<Split> getRoot() {
		return root;
	}

	public void setRoot(ValueNode<Split> root) {
		this.root = root;
	}
	
	/**
	 * Converts the tree to a list.
	 * @return
	 */
	public List<Split> getAllSplits() {
		Set<Split> splits = new HashSet<Split>();
		this.getAllSplitsRecursive(splits, this.getRoot());
		
		return new ArrayList<Split>(splits);
	}
	
	protected void getAllSplitsRecursive(Set<Split> splits, ValueNode<Split> node) {
		splits.add(node.getValue());
		if (node.hasChildren()) {
			for (ValueNode<Split> child : node.getChildren()) {
				this.getAllSplitsRecursive(splits, child);
			}
		}
	}
}
