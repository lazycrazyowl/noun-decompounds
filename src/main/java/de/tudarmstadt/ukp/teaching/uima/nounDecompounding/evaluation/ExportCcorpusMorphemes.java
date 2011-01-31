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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;

/**
 * Exports all morphemes from the corpus
 * and print in on the console.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class ExportCcorpusMorphemes {

	private CcorpusReader reader;

	public ExportCcorpusMorphemes(CcorpusReader aReader) {
		this.reader = aReader;
	}
	
	/**
	 * Read all splits and export the morphemems.
	 * @return
	 * @throws IOException
	 */
	public Set<String> getMorphemes() throws IOException {
		Set<String> morphemes = new HashSet<String>();
		Split split;
		while((split = reader.readSplit()) != null) {
			for (SplitElement s : split.getSplits()) {
				if (s.getMorpheme() != null) {
					morphemes.add(s.getMorpheme());
				}
			}
		}
		
		return morphemes;
	}
	
	
	public static void main(String[] args) throws IOException {
		ExportCcorpusMorphemes exporter = new ExportCcorpusMorphemes(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")));
		Set<String> morphemes = exporter.getMorphemes();

		for (String s : morphemes) {
			System.out.println(s);
		}
	}
}
