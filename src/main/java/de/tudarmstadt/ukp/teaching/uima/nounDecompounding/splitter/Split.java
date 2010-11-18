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
 *
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
			s.addSplitElement(SplitElement.createFromString(string));
		}
		
		return s;
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
	 * Adds a split element 
	 * @param split
	 */
	public void addSplitElement(SplitElement split) {
		this.splits.add(split);
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
}
