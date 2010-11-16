package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

/**
 * Dictionary interface. Can be used to
 * create different Dictionaries.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public interface IDictionary {

	/**
	 * Checks if a word is in a dictionary
	 * @param word
	 * @return
	 */
	public boolean contains(String word);
}
