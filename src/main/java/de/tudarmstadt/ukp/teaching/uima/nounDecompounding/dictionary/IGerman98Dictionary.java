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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.igerman98.Affix;

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

	private static final String PREFIX_KEY = "PFX";
	private static final String SUFFIX_KEY = "SFX";
	
	private File affix;
	private Map<Character, List<Affix>> affixes = new HashMap<Character, List<Affix>>();

	public IGerman98Dictionary(File dict, File affix) {
		this.affix = affix;
		this.readAffixFile();
		
		this.setDict(dict);
		try {
			this.setWords(this.readFileToSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			String word = split[0].toLowerCase();
			char[] flags = {};
			
			if (split.length > 1) {
				flags = split[1].toCharArray();
			}
			
			if (word.length() > 2) {
				words.add(word);
			}
			
			if (flags.length > 0) {
				words.addAll(this.buildWords(word, flags));
			}
		}
		
		return words;
	}

	/**
	 * Reads the affix file and processes the data
	 */
	protected void readAffixFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.affix));
			
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(PREFIX_KEY) || line.startsWith(SUFFIX_KEY)) {
					this.parseAffix(line, reader);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Parse a affix in the affix file
	 * @param header The header of the affix
	 * @param reader The file reader to read the rest of the affix
	 * @throws IOException
	 */
	private void parseAffix(String header, BufferedReader reader) throws IOException {
		String args[] = header.split("\\s+");
		
		boolean crossProduct = args[2].equals("Y");
		int numLines = Integer.parseInt(args[3]);
		
		for (int i = 0; i < numLines; i++) {
			String line = reader.readLine();
			String ruleArgs[] = line.split("\\s+");
			Character flag = ruleArgs[1].toCharArray()[0];
			
			Affix a = new Affix(args[0]);
			a.setCrossProduct(crossProduct);
			a.setFlag(flag);
			a.setStripping(ruleArgs[2]);
			a.setAffix(ruleArgs[3]);
			a.setCondition(ruleArgs[4]);
			
			List<Affix> list = this.affixes.get(flag);
			if (list == null) {
				list = new ArrayList<Affix>();
				this.affixes.put(flag, list);
			}
			list.add(a);
		}
	}
	
	/**
	 * Uses affixes to build new words
	 * @param word
	 * @param flags
	 * @return
	 */
	protected List<String> buildWords(String word, char[] flags) {
		List<String> words = new ArrayList<String>();
		for (char c : flags) {
			List<Affix> aff = this.affixes.get(new Character(c));
			if (aff == null) {
				continue;
			}
			for (Affix affix : aff) {
				String w = affix.handleWord(word);
				if (w != null && w.length() > 2) {
					words.add(w);
				}
			}
		}
		
		return words;
	}
}
