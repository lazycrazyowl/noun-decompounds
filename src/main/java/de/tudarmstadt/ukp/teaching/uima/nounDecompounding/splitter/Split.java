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
import java.util.List;

/**
 * A data container for a split of a word.
 * This container stores on split of all possible splits.
 * 
 * @author Jens Haase <je.haase@google.com>
 */
public class Split {

	private List<SplitElement> splits = new ArrayList<SplitElement>();

	/**
	 * Create a split from a string
	 * 
	 * The string has the structure:
	 *   word1(morpheme)+word2(morpheme)+...+word3
	 * 
	 * For example: "Aktion(s)+plan" or "Verbraucher+zahlen"
	 * 
	 * @param split
	 * @return
	 */
	public static Split createFromString(String split) {
		Split s = new Split();
		String[] elems = split.split("\\+");
		
		for (String string : elems) {
			s.appendSplitElement(SplitElement.createFromString(string));
		}
		
		return s;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((splits == null) ? 0 : splits.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		String s = "";
		
		for (int i = 0; i < this.splits.size(); i++) {
			s += this.splits.get(i).toString();
			if (i<this.splits.size()-1) {
				s += "+";
			}
		}
		
		return s;
	}
	
	/**
	 * Adds a split element at the end
	 * @param split
	 */
	public void appendSplitElement(SplitElement split) {
		this.splits.add(split);
	}
	
	/**
	 * Adds a split element to the beginning
	 * @param split
	 */
	public void prependSplitElement(SplitElement split) {
		this.splits.add(0, split);
	}

	/**
	 * Returns all split elements
	 * @return
	 */
	public List<SplitElement> getSplits() {
		return splits;
	}

	/**
	 * Set all split elements
	 * @param splits
	 */
	public void setSplits(List<SplitElement> splits) {
		this.splits = splits;
	}

	/**
	 * Adds a list of split elements.
	 * @param splits
	 */
	public void addAll(List<SplitElement> splits) {
		this.splits.addAll(splits);
	}
	
	/**
	 * Replace one split element with a split.
	 * That means all split elements will be inserted at the position
	 * of the split element
	 * @param i
	 * @param split
	 */
	public void replaceSplitElement(int i, Split split) {
		this.splits.remove(i);
		for (int j = 0; j < split.getSplits().size(); j++) {
			SplitElement e = split.getSplits().get(j);
			this.splits.add(i+j, e);
		}
	}
	
	/**
	 * Replaces a split element with another one
	 * @param i
	 * @param split
	 */
	public void replaceSplitElement(int i, SplitElement split) {
		this.splits.set(i, split);
	}

	/**
	 * Similar to the equals method, but combines morpheme and word
	 * @param obj
	 * @return
	 */
	public boolean equalWithoutMorpheme(Split obj) {
		return this.toStringWithoutMorpheme().equals(obj.toStringWithoutMorpheme());
	}

	/**
	 * Similar to the toString method, but combines morpheme and word
	 * @return
	 */
	private String toStringWithoutMorpheme() {
		String s = "";
		
		for (int i = 0; i < this.splits.size(); i++) {
			s += this.splits.get(i).toStringWithoutMorpheme();
			if (i<this.splits.size()-1) {
				s += "+";
			}
		}
		
		return s;
	}
	
	public String getWord() {
		String word = "";
		for (SplitElement e : this.getSplits()) {
			word += e.getWord();
			if (e.hasMorpheme()) {
				word += e.getMorpheme();
			}
		}
		
		return word;
	}
	
	/**
	 * Creates a copy of this element.
	 * @return
	 */
	public Split createCopy() {
		return Split.createFromString(this.toString());
	}
	
	
}
