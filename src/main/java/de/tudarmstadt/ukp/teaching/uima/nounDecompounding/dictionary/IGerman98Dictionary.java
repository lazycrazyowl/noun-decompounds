package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The igerman98 dictionary from www.j3e.de/ispell/igerman98
 * 
 * A current version of the german dictionary de_DE can be found
 * in /src/main/resources/de_DE.dic
 * 
 * This class can also be used to read other ispell/hunspell
 * dictionaries. 
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 *
 */
public class IGerman98Dictionary extends SimpleDictionary {

	public IGerman98Dictionary(File dict) {
		super(dict);
	}

	@Override
	protected Set<String> readFileToSet() throws IOException {
		Set<String> words = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(this.getDict()));
		
		// First line contains number of entries -> skip
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			if (line.equals("") || line.substring(0,1).equals("#") || line.substring(0,1).equals("\t")) {
				// Ignore lines starting with hash of tab (comments)
				continue;
			}
			String[] split = line.split("/");
			words.add(split[0]);
		}
		
		return words;
	}

	
}
