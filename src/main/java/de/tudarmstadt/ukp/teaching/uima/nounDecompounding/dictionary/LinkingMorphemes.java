package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Linking morphemes container.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class LinkingMorphemes {

	List<String> morphemes;
	
	/**
	 * Create a linking morphemes object from a array of morphemes
	 * @param morphemes
	 */
	public LinkingMorphemes(String... morphemes) {
		this.morphemes = new ArrayList<String>();
		
		for (String string : morphemes) {
			this.morphemes.add(string);
		}
	}
	
	/**
	 * Create a linking morphemes object from a list of morphemes
	 * @param morphemes
	 */
	public LinkingMorphemes(List<String> morphemes) {
		this.morphemes = morphemes;
	}
	
	/**
	 * Create a linking morphemes object from a file.
	 * 
	 * Each line in the file must contain one morpheme.
	 * Use # in front of a line for comments.
	 * 
	 * @param morphemesTextFile
	 */
	public LinkingMorphemes(File morphemesTextFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(morphemesTextFile));
			
			this.morphemes = new ArrayList<String>();
			String line;
			
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0 && line.substring(0,1) != "#") {
					morphemes.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns all morphemes.
	 * @return
	 */
	public List<String> getAll() {
		return morphemes;
	}
}
