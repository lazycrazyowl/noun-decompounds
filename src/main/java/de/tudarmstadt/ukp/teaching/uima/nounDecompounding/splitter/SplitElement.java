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
