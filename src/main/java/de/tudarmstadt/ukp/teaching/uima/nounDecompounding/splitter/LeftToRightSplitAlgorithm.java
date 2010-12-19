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

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.CcorpusReader;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.SplitterEvaluation;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.ValueNode;

/**
 * Implements a simple left to right split algorithm.
 * 
 * Goes from left to right to the word. If a word is found
 * the right side is evaluation from left to right. At the
 * end we have a right balanced tree. All leaves are the
 * smallest word fractions.
 * 
 * The leaves will combined to all possible splits.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class LeftToRightSplitAlgorithm implements ISplitAlgorithm {

	private IDictionary dict;
	private LinkingMorphemes morphemes;

	/**
	 * Create a instance of the algorithm
	 * @param dict A dictionary with all words
	 * @param morphemes A LinkingMorphemes class
	 */
	public LeftToRightSplitAlgorithm(IDictionary dict, LinkingMorphemes morphemes) {
		this.dict = dict;
		this.morphemes = morphemes;
	}
	
	@Override
	public SplitTree split(String word) {
		word = word.toLowerCase();
		
		SplitTree t = new SplitTree(word);
		t.getRoot().getValue().getSplits().get(0).setSplitAgain(true);
		
		this.ltrSplit(t.getRoot());
		
		return t;
	}
	
	/**
	 * The basic split algorithm. Moves the word from left to right
	 * and checks for valid words.
	 * @param word
	 * @return
	 */
	protected void ltrSplit(ValueNode<Split> parent) {
		for (int i = 0; i < parent.getValue().getSplits().size(); i++) {
			SplitElement element = parent.getValue().getSplits().get(i);
			
			if (element.shouldSplitAgain()) {
				List<Split> results = this.makeSplit(element.getWord());
				
				for (Split result : results) {
					Split copy = parent.getValue().createCopy();
					if (result.getSplits().size() > 1) {
						result.getSplits().get(1).setSplitAgain(true);
						copy.replaceSplitElement(i, result);
						ValueNode<Split> child = new ValueNode<Split>(copy);
						parent.addChild(child);
						this.ltrSplit(child);
					} else if (result.getSplits().size() == 1 && !result.equals(parent.getValue())) {
						copy.replaceSplitElement(i, result);
						ValueNode<Split> child = new ValueNode<Split>(copy);
						parent.addChild(child);
					}
				}
			}
		}
	}
	
	/**
	 * Splits a word in two word.
	 * @param word
	 * @return
	 */
	protected List<Split> makeSplit(String word) {
		List<Split> result = new ArrayList<Split>();
		
		for (int i = 0; i < word.length(); i++) {
			String leftWord = word.substring(0, i+1);
			String rightWord = word.substring(i+1);
			
			if (this.dict.contains(leftWord)) {
				if (rightWord.length() > 2 || rightWord.length() == 0)
					result.add(Split.createFromString(leftWord + "+" + rightWord));
			} 
			// Next: Add morpheme between word.
			for (String morpheme : this.morphemes.getAll()) {
				try {
					String leftWithoutMorpheme = leftWord.substring(0, leftWord.length() - morpheme.length());
					if (leftWord.endsWith(morpheme) && this.dict.contains(leftWithoutMorpheme) &&
							(rightWord.length() > 2 || rightWord.length() == 0)) {
						result.add(Split.createFromString(leftWithoutMorpheme+"("+morpheme+")"+"+"+rightWord));
					}
				} catch (StringIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		
		return result;
	}

	public static void main(String[] args) throws FileNotFoundException {
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		SplitterEvaluation e = new SplitterEvaluation(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")));
		float result = e.evaluate(algo);
		
		System.out.println("Result " + result);
	}
}
