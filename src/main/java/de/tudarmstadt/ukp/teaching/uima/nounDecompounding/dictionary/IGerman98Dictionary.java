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
			if (split[0].length() > 2) {
				// Add only words with a min length of 3
				words.add(split[0].toLowerCase());
			}
		}
		
		return words;
	}

	
}
