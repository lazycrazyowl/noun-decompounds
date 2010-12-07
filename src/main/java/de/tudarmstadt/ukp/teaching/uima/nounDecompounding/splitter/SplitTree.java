package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.ValueNode;

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
