package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.SimpleDictionary;

/**
 * A trie datastructor which also stores the number of successor for each node
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class Trie {

	// Key: substring. Value: Successors
	private KeyValueNode<String, Integer> root = new KeyValueNode<String, Integer>("", 0);
	
	/**
	 * Adds a word to the tree.
	 * Also increments the successor value for each node
	 * 
	 * @param word
	 */
	public void addWord(String word) {
		KeyValueNode<String, Integer> parent = this.root;
		
		for (int i = 0; i < word.length(); i++) {
			String subword = word.substring(0,i+1);
			KeyValueNode<String, Integer> child = parent.getChild(subword);
			
			if (child != null) {
				if (!subword.equals(word)) {
					child.setValue(child.getValue()+1);
				}
			} else {
				Integer value = 1;
				if (subword.equals(word)) {
					value = 0;
				}
				child = new KeyValueNode<String, Integer>(subword, value);
				parent.addChild(child);
			}
			
			parent = child;
		}
	}
	
	/**
	 * Finds a not with a given string. If not found NULL is returned.
	 * @param word
	 * @return
	 */
	public KeyValueNode<String, Integer> findWord(String word) {
		word = word.toLowerCase();
		KeyValueNode<String, Integer> parent = this.root;
		int depth = 1;
		
		while (parent.hasChildren()) {
			String w = word.substring(0, depth);
			KeyValueNode<String, Integer> child = parent.getChild(w);
			
			if (w.equals(word)) {
				return child;
			} else if (child != null) {
				parent = child;
				depth++;
			} else {
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the number of successor for a node.
	 * If the node could not be found the return value is 0. 
	 * @param word
	 * @return
	 */
	public Integer getSuccessors(String word) {
		KeyValueNode<String, Integer> node = this.findWord(word);
		if (node != null) {
			return node.getValue();
		}
		
		return 0;
	}
	
	/**
	 * Creates a Trie object for a SimpleDictionary
	 * @param dict
	 * @return
	 */
	public static Trie createForDict(SimpleDictionary dict) {
		Trie t = new Trie();
		
		for (String word : dict.getWords()) {
			t.addWord(word);
		}
		
		return t;
	}
	
	/**
	 * Creates a Trie object for a SimpleDictionary with all
	 * words reversed
	 * @param dict
	 * @return
	 */
	public static Trie createForDictReverse(SimpleDictionary dict) {
		Trie t = new Trie();
		
		for (String word : dict.getWords()) {
			t.addWord(new StringBuffer(word).reverse().toString());
		}
		
		return t;
	}
}
