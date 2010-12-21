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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class TotalFreqAmout {

	private File folder;

	public TotalFreqAmout(File aWeb1tFolder) {
		this.folder = aWeb1tFolder;
	}
	
	public BigInteger countFreq() throws IOException {
		BigInteger count = BigInteger.valueOf(0);
		
		File[] files;
		
		if (this.folder.isFile()) {
			files = new File[]{ this.folder };
		} else if (this.folder.isDirectory()) {
			files = this.folder.listFiles();
		} else {
			throw new FileNotFoundException();
		}
		
		int i = 0;
		for (File file: files) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String[] split;
			while ((line = reader.readLine()) != null) {
				split = line.split("\t");
				count = count.add(new BigInteger(split[1]));
			}
			i++;
			System.out.println(file.getName() + " is Ready. Only " + (files.length-i) + " files left ...");
		}
		
		return count;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TotalFreqAmout amount = new TotalFreqAmout(new File("/home/jens/Desktop/web1t/GERMAN_EXTRACT"));
		System.out.println("Total amount: " + amount.countFreq());
	}

}
