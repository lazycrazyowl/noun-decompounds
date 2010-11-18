package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import java.util.List;

/**
 * Interface for all splitting algorithms
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public interface ISplitAlgorithm {

	/**
	 * Returns all possible splits for a given word.
	 * 
	 * @param word The word to split
	 * @return
	 */
	public List<Split> split(String word);
}
