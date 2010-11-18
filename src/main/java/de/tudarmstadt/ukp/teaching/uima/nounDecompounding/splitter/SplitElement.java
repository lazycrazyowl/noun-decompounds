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

/**
 * Data container for a split element.
 * A split element contains a word and optional a linking morpheme
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class SplitElement {

	private String word;
	private String morpheme;
	
	/**
	 * Creates a empty split element
	 */
	public SplitElement() {
		this(null, null);
	}
	
	/**
	 * Creates a split element with a word
	 * but no linking morpheme
	 * @param word
	 */
	public SplitElement(String word) {
		this(word, null);
	}
	
	/**
	 * Creates a split element with a word
	 * and a linking morpheme
	 * @param word
	 * @param morpheme
	 */
	public SplitElement(String word, String morpheme) {
		this.word = word;
		this.morpheme = morpheme;
	}
	
	/**
	 * Creates a split element from string. String format:
	 * 
	 *   word(morpheme)
	 *   
	 * Example: "auto" or "auto(s)"
	 * @param element
	 * @return
	 */
	public static SplitElement createFromString(String element) {
		SplitElement e = new SplitElement();
		
		String[] splits = element.split("\\(");
		e.setWord(splits[0]);
		if (splits.length == 2 && splits[1].endsWith(")")) {
			e.setMorpheme(splits[1].substring(0, splits[1].length()-1));
		}
		
		return e;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}
	
	@Override
	public String toString() {
		String s = this.word;
		
		if (this.morpheme != null) {
			s += "("+this.morpheme+")";
		}
		
		return s;
	}
	
	/**
	 * Returns the word of the split element
	 * @return
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Sets the word of the split element
	 * @param word
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
	/**
	 * Returns the linking morpheme of the split element
	 * @return
	 */
	public String getMorpheme() {
		return morpheme;
	}

	/**
	 * Sets the linking morpheme of the split element
	 * @param morpheme
	 */
	public void setMorpheme(String morpheme) {
		this.morpheme = morpheme;
	}
}
