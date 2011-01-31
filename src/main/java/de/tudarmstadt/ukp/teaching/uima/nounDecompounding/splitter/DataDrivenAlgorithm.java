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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.SimpleDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.CcorpusReader;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.SplitterEvaluation;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.Trie;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.ValueNode;

/**
 * A data driven algorithm, that uses a TRIE to look for splits
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class DataDrivenAlgorithm implements ISplitAlgorithm {

	private Trie forwardTrie;
	private Trie backwardTrie;
	private LinkingMorphemes morphmes;

	/**
	 * Constructor
	 * @param aDictionary A simple dictionary object
	 * @param aMorphemes A list of linking morphemes
	 */
	public DataDrivenAlgorithm(SimpleDictionary aDictionary, LinkingMorphemes aMorphemes) {
		this.forwardTrie = Trie.createForDict(aDictionary);
		this.backwardTrie = Trie.createForDictReverse(aDictionary);
		this.morphmes = aMorphemes;
	}
	
	@Override
	public SplitTree split(String word) {
		word = word.toLowerCase();
		SplitTree t = new SplitTree(word);
		t.getRoot().getValue().getSplits().get(0).setSplitAgain(true);
		
		this.splitIt(t.getRoot());
		
		return t;
	}
	
	/**
	 * Builds a splitting tree
	 * @param parent
	 */
	protected void splitIt(ValueNode<Split> parent) {
		// Iterate over all split elements
		for (int i = 0; i < parent.getValue().getSplits().size(); i++) {
			SplitElement element = parent.getValue().getSplits().get(i);
			
			// Do something if split element should be splitted
			if (element.shouldSplitAgain()) {
				// Split
				List<Split> results = this.makeSplit(element.getWord());
				
				for (Split result : results) {
					if (result.getSplits().size() > 1) {
						// Left site
						Split resultCopy1 = result.createCopy();
						resultCopy1.getSplits().get(0).setSplitAgain(true);
						Split parentCopy1 = parent.getValue().createCopy();
						parentCopy1.replaceSplitElement(i, resultCopy1);
						ValueNode<Split> node1 = new ValueNode<Split>(parentCopy1);
						parent.addChild(node1);
						this.splitIt(node1);
						
						// Right site
						Split resultCopy2 = result.createCopy();
						resultCopy2.getSplits().get(1).setSplitAgain(true);
						Split parentCopy2 = parent.getValue().createCopy();
						parentCopy2.replaceSplitElement(i, resultCopy2);
						ValueNode<Split> node2 = new ValueNode<Split>(parentCopy2);
						parent.addChild(node2);
						this.splitIt(node2);
					}
				}
			}
		}
	}
	
	/**
	 * Makes a single split on a given word. Returns all
	 * possible splittings. All splits consist of two elements
	 * @param word
	 * @return
	 */
	protected List<Split> makeSplit(String word) {
		List<Split> returnList = new ArrayList<Split>();
		if (word.length()-5 <= 0) {
			Split s = new Split();
			s.appendSplitElement(new SplitElement(word));
			returnList.add(s);
			
			return returnList;
		}
		
		int[] forward = new int[word.length()-2];
		int[] backward = new int[word.length()-2];
		int[] diffForward = new int[word.length()-3];
		int[] diffBackward = new int[word.length()-3];
		boolean[] maxForward = new boolean[word.length()-5];
		boolean[] maxBackward = new boolean[word.length()-5];
		
		// Read successor from trie
		for (int i = 2; i < word.length(); i++) {
			String subword = word.substring(0, i+1);
			forward[i-2] = this.forwardTrie.getSuccessors(subword);
		}
		
		for (int i = word.length()-3; i > -1; i--) {
			String subword = word.substring(i);
			backward[i] = this.backwardTrie.getSuccessors(new StringBuffer(subword).reverse().toString());
		}
		
		// Make difference
		for (int i = 1; i < forward.length; i++) {
			diffForward[i-1] = forward[i-1] - forward[i];
		}
		
		for (int i = backward.length-2; i > -1; i--) {
			diffBackward[i] = backward[i+1] - backward[i];
		}
		
		// Mark local maximas
		for (int i = 1; i < diffForward.length-1; i++) {
			if (diffForward[i-1] < diffForward[i] && diffForward[i] > diffForward[i+1]) {
				maxForward[i-1] = true;
			} else {
				maxForward[i-1] = false;
			}
		}
		
		for (int i = diffBackward.length-2; i > 0; i--) {
			if (diffBackward[i-1] < diffBackward[i] && diffBackward[i] > diffBackward[i+1]) {
				maxBackward[i-1] = true;
			} else {
				maxBackward[i-1] = false;
			}
		}
		
//		String debugForward = "";
//		for (int i = 0; i < word.length(); i++) {
//			if (i > 3 && i < word.length() -1 && maxForward[i-4]) {
//				debugForward += "|";
//			}
//			debugForward += word.charAt(i);
//		}
//		System.out.println("[DEBUG] F:" +debugForward);
//		
//		String debugBackward = "";
//		for (int i = 0; i < word.length(); i++) {
//			debugBackward += word.charAt(i);
//			if (i < word.length()-5 && i > 0 && maxBackward[i-1]) {
//				debugBackward += "|";
//			}
//		}
//		System.out.println("[DEBUG] B:" +debugBackward);
		
		// Get all split positions
		List<Integer> splitPos = new ArrayList<Integer>();
		for (int i = 0; i < maxForward.length-3; i++) {
			boolean maxF = maxForward[i];
			boolean maxB = maxBackward[i+2];
			if (maxF && maxB) {
				splitPos.add(i+4);
			}
		}
		
		// Create all splits
		if (splitPos.size() > 0) {
			for (Integer pos : splitPos) {
				Split s = new Split(); 
				s.appendSplitElement(new SplitElement(word.substring(0,pos)));
				s.appendSplitElement(new SplitElement(word.substring(pos)));
				
				returnList.addAll(this.checkForMorphemes(s));
			}
		} else {
			Split s = new Split();
			s.appendSplitElement(new SplitElement(word));
			returnList.add(s);
		}
		
		return returnList;
	}
	
	protected List<Split> checkForMorphemes(Split split) {
		List<Split> result = new ArrayList<Split>();
		result.add(split);
		
		int pos;
		String word = split.getSplits().get(1).getWord();
		if ((pos = morphmes.startsWith(word)) > 0) {
			String m = word.substring(0, pos);
			String newWord = word.substring(pos);
			Split copy = split.createCopy();
			
			copy.getSplits().get(0).setMorpheme(m);
			copy.getSplits().get(1).setWord(newWord);
			
			result.add(copy);
		}
		
		return result;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		SimpleDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		
		DataDrivenAlgorithm algo = new DataDrivenAlgorithm(dict, morphemes);
		
		SplitterEvaluation e = new SplitterEvaluation(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")));
		float result = e.evaluate(algo);
		
		System.out.println("Result " + result);
	}
}
