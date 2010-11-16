package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The simple dictionary reads a file
 * in which each line is a new word.
 * 
 * This can be used to create your own dictionary from a corpus
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 *
 */
public class SimpleDictionary implements IDictionary {

	private File dict;
	private Set<String> words;

	/**
	 * Constructor for a simple dictionary
	 * @param dict The file with all words
	 */
	public SimpleDictionary(File dict) {
		this.dict = dict;
		try {
			this.words  = this.readFileToSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean contains(String word) {
		return this.words.contains(word);
	}
	
	/**
	 * Reads the dictionary to set
	 * @return A set of words
	 * @throws IOException
	 */
	protected Set<String> readFileToSet() throws IOException {
		Set<String> words = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(this.dict));
		String line;
		while ((line = reader.readLine()) != null) {
			words.add(line);
		}
		
		return words;
	}

	public File getDict() {
		return dict;
	}

	public void setDict(File dict) {
		this.dict = dict;
	}

	public Set<String> getWords() {
		return words;
	}

	public void setWords(Set<String> words) {
		this.words = words;
	}
}
