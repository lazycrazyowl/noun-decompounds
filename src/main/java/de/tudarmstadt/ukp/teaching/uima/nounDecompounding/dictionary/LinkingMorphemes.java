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
	
	/**
	 * Checks if the given word starts with a morpheme
	 * @param word
	 * @return The length of the morpheme or -1 if it do not start with a morpheme
	 */
	public int startsWith(String word) {
		for (String m: this.getAll()) {
			if (word.startsWith(m)) {
				return m.length();
			}
		}
		
		return -1;
	}
}
