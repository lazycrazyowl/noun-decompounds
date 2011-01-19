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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 * Caluclates the total amount of frequency.
 * This value is needed for the proabibliy/mutal based method
 * @author jens
 *
 */
public class TotalFreqAmout {

	private File folder;

	public TotalFreqAmout(File aWeb1tLucenceIndex) {
		this.folder = aWeb1tLucenceIndex;
	}
	
	/**
	 * Adds all frequency values
	 * @return
	 * @throws IOException
	 */
	public BigInteger countFreq() throws IOException {
		BigInteger count = BigInteger.valueOf(0);
		
		FSDirectory dir = FSDirectory.open(this.folder);
		IndexReader reader = IndexReader.open(dir);
		
		for (int i = 0; i < reader.maxDoc(); i++) {
			if (reader.isDeleted(i)) {
				continue;
			}
			
			Document doc = reader.document(i);
			count = count.add(new BigInteger(doc.get("freq")));
		}
		
		return count;
	}
	
	public static void main(String[] args) throws IOException {
		TotalFreqAmout amount = new TotalFreqAmout(new File("/home/jens/Desktop/web1tIndex4/0"));
		System.out.println("Total amount: " + amount.countFreq());
	}

}
