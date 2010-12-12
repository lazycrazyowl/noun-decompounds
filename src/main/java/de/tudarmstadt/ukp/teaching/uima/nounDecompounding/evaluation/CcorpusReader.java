package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;

public class CcorpusReader extends BufferedReader {

	public CcorpusReader(File file) throws FileNotFoundException {
		this(new FileReader(file));
	}
	
	public CcorpusReader(Reader in) {
		super(in);
	}
	
	public Split readSplit() throws IOException {
		String line = this.readLine();
		if (line == null) {
			return null;
		}
		String[] data = line.split(" ");
		
		return this.createSplit(data[1]);
	}

	/**
	 * Creates a split object from the ccorpus file format
	 * 
	 * @param words The ccorpus split string
	 * @return
	 */
	public Split createSplit(String words) {
		Split s = new Split();
		
		List<String> wordList = new ArrayList<String>();
		String[] elements = words.split("\\+");
		String pfx = "";
		
		// Step 1. Check for prep and pfx classes.
		for (String word : elements) {
			// Get word
			String cpart = this.replaceUmlaute(word.replaceAll("(\\{[^\\}]+\\})$", "").replaceAll(",[^\\|\\{]+", ""));
			
			// Get categories of word
			Matcher catsMatcher = Pattern.compile("\\{([^\\}]+)\\}$").matcher(word);
			boolean pfxOrPrep = false;
			if (catsMatcher.find()) {
				String[] cats = catsMatcher.group().replace("{", "").replace("}", "").split(",");
				for (String cat : cats) {
					if (cat.equals("PREP") || cat.equals("PFX")) {
						pfx += cpart;
						pfxOrPrep = true;
					}
				}
			}
			
			if (!pfxOrPrep) {
				wordList.add(pfx + cpart);
				pfx = "";
			}
		}
		
		// Step 2. Extract words and morphemes
		for (String word : wordList) {
			// Remove morphemes
			String lemma = word.replaceAll("((\\|.+|[\\(].+[\\)])$|,)", "");
			String surface = word.replaceAll("(,[^\\|]+(\\|)?|\\||\\{[^\\}]+\\}|[\\(\\)])", "");
			
			SplitElement e = new SplitElement();
			e.setWord(lemma);
			
			// Extract morphemes
			if (surface.length() > lemma.length()) {
				e.setMorpheme(surface.substring(lemma.length()));
			}
			
			s.appendSplitElement(e);
		}
		
		return s;
	}
	
	private String replaceUmlaute(String word) {
		return word.replace("O", "ö").replace("A", "ä").replace("U", "ü");
	}
}
