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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.old;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.SplitterEvaluation;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;

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
@Deprecated
public class LeftToRightSplitAlgorithmV1 implements ISplitAlgorithmV1 {

	private IDictionary dict;
	private LinkingMorphemes morphemes;

	/**
	 * Create a instance of the algorithm
	 * @param dict A dictionary with all words
	 * @param morphemes A LinkingMorphemes class
	 */
	public LeftToRightSplitAlgorithmV1(IDictionary dict, LinkingMorphemes morphemes) {
		this.dict = dict;
		this.morphemes = morphemes;
	}
	
	@Override
	public List<Split> split(String word) {
		word = word.toLowerCase();
		
		Split s = this.ltrSplit(word);
		List<Split> result = new ArrayList<Split>();
		
		if (s != null) {
			List<Split> combination = this.combine(s);
			for (Split split : combination) {
				result.addAll(this.morpheme(split.getSplits()));
			}
		} else {
			s = new Split();
			s.appendSplitElement(new SplitElement(word));
			result.add(s);
		}

		return result;
	}
	
	/**
	 * The basic split algorithm. Moves the word from left to right
	 * and checks for valid words.
	 * @param word
	 * @return
	 */
	protected Split ltrSplit(String word) {
		Split rightSplit;
		
		// Move from left to right
		for (int i = 0; i < word.length(); i++) {
			String leftWord = word.substring(0, i+1);
			String rightWord = word.substring(i+1);
	
			// Check if the left word is in the dict and the right part can be splitted
			if (this.dict.contains(leftWord) && (rightSplit = this.ltrSplit(rightWord)) != null) {
				rightSplit.prependSplitElement(new SplitElement(leftWord));
				return rightSplit;
			} else {
				// Next: Add morpheme between word.
				for (String morpheme : this.morphemes.getAll()) {
					try {
						String m = word.substring(i+1, i+1+morpheme.length());
						rightWord = word.substring(i+1+morpheme.length());
						
						if (this.dict.contains(leftWord) && m.equals(morpheme) && (rightSplit = this.ltrSplit(rightWord)) != null) {
							rightSplit.prependSplitElement(new SplitElement(leftWord, morpheme));
							return rightSplit;
						}
					} catch (StringIndexOutOfBoundsException e) {
						continue;
					}
				}
			}
		}
		
		if (this.dict.contains(word)) {
			// If word is in dict add it;
			rightSplit = new Split();
			rightSplit.appendSplitElement(new SplitElement(word));
			return rightSplit;
		} else {
			// If word not in dict, check for morpheme add the end
			for (String morpheme : this.morphemes.getAll()) {
				try {
					String w = word.substring(0, word.length() - morpheme.length());
					if (word.endsWith(morpheme) && this.dict.contains(w)) {
						rightSplit = new Split();
						rightSplit.appendSplitElement(new SplitElement(w, morpheme));
						return rightSplit;
					}
				} catch (StringIndexOutOfBoundsException e) {
					continue;
				}
			}
			
			// No one found return null
			return null;
		}
	}

	/**
	 * Checks for every word with a morpheme if it is also with morpheme in the dict
	 * and creates all possible combination.
	 * 
	 * Example: If the split is "Akt+ion(s)+plan" and the word "ions" is in the dict,
	 * 	the return value is {Akt+ion(s)+plan, Akt+ions+plan}
	 * @param elements
	 * @return
	 */
	protected List<Split> morpheme(List<SplitElement> elements) {
		// Split list in first and rest
		SplitElement first = elements.get(0);
		List<SplitElement> rest = elements.subList(1, elements.size());
		
		// Create word with morpheme and word without morpheme
		SplitElement element1 = first;
		SplitElement element2 = null;
		if (first.hasMorpheme() && this.dict.contains(first.getWord() + first.getMorpheme())) {
			element2 = new SplitElement(first.getWord() + first.getMorpheme());
		}
		
		
		List<Split> result = new ArrayList<Split>();
		
		if (rest.isEmpty()) {
			// Leaf node. Just return the two word
			Split s1 = new Split();
			s1.appendSplitElement(element1);
			result.add(s1);
			
			if (element2 != null) {
				Split s2 = new Split();
				s2.appendSplitElement(element2);
				result.add(s2);
			}
			
			
			return result;
		} else {
			List<Split> children = this.morpheme(rest);
			
			for (Split child : children) {
				Split s1 = child.createCopy();
				s1.prependSplitElement(element1);
				result.add(s1);
				
				if (element2 != null) {
					Split s2 = child.createCopy();
					s2.prependSplitElement(element2);
					result.add(s2);
				}
			}
			
			return result;
		}
	}
	
	/**
	 * The basic left to right splitting algorithm
	 * has only small word. This function will combine
	 * single word to new word and checks if they are in
	 * the dictioary.
	 * 
	 * @param split
	 * @return
	 */
	protected List<Split> combine(Split split) {
		List<Split> list = new ArrayList<Split>();
		List<SplitElement> elements = split.getSplits();
		list.add(split);
		
		for (int i = 0; i < elements.size(); i++) {
			Split possibleSplit = new Split();
			possibleSplit.appendSplitElement(elements.get(i));
			
			for (int j = i+1; j < elements.size(); j++) {
				SplitElement next = elements.get(j);
				List<SplitElement> before = elements.subList(0, i);
				List<SplitElement> rest = elements.subList(j+1, elements.size());
				String word1 = this.toWord(possibleSplit) + this.toWord(next, false);
				String word2 = word1 + next.getMorpheme();
				
				if (this.dict.contains(word1)) {
					// Word without morpheme at the end
					possibleSplit.appendSplitElement(next);
					
					Split toAdd = new Split();
					toAdd.addAll(before);
					toAdd.appendSplitElement(new SplitElement(word1, next.getMorpheme()));
					toAdd.addAll(rest);
					list.add(toAdd);
				} else if (this.dict.contains(word2)) {
					// Word with morpheme at the end
					possibleSplit.appendSplitElement(next);
					
					Split toAdd = new Split();
					toAdd.addAll(before);
					toAdd.appendSplitElement(new SplitElement(word2));
					toAdd.addAll(rest);
					list.add(toAdd);
				}
			}
		}
		
		return list;
	}
	
	protected String toWord(Split split) {
		String s = "";
		
		for (SplitElement e : split.getSplits()) {
			s += this.toWord(e, true);
		}
		
		return s;
	}
	
	protected String toWord(SplitElement element, boolean withMorpheme) {
		String m = (withMorpheme && element.getMorpheme() != null) ? element.getMorpheme() : "";
		
		return element.getWord() + m;
	}

	public static void main(String[] args) {
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		LeftToRightSplitAlgorithmV1 algo1 = new LeftToRightSplitAlgorithmV1(dict, morphemes); // Algorithm 1 with IGerman98 Dictionary
		
		SplitterEvaluation e = new SplitterEvaluation(new File("src/main/resources/evaluation/ccorpus.txt"));
		float result = e.evaluate(algo1);
		
		System.out.println("Result " + result);
	}
}
