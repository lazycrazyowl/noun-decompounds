package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

/**
 * N-gram model class.
 * 
 * This is only a data container for the n-grams
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class NGram {

	private String gram;
	private int freq;
	
	public NGram(String gram, int freq) {
		this.gram = gram;
		this.freq = freq;
	}

	public String getGram() {
		return gram;
	}

	public void setGram(String gram) {
		this.gram = gram;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getN() {
		return this.gram.split(" ").length;
	}
}
