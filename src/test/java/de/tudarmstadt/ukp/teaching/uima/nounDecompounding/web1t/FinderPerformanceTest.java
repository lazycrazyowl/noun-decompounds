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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class FinderPerformanceTest {

	/**
	 * Test the finder performace on the generated web1t index
	 * Time is printed on the console.
	 */
	@Test
	public void testPerformance1() {
		Finder f = new Finder(new File("/home/jens/Desktop/web1tIndex"));
		String[] words = new String[] {"hallo welt", "wie geht es euch", "alpha", "zutun", "lasst uns nach hause gehen", "rennen"};
		long time = 0;
		
		for (int i = 0; i < words.length; i++) {
			long start = System.currentTimeMillis();
			Assert.assertTrue(f.find(words[i]).size() > 0);
			long end = System.currentTimeMillis();
			time += end-start;
			System.out.println("Time for '"+words[i]+"' (ms): " + (end-start));
		}
		
		System.out.println("  -> Average time (ms): " + ((float) time/ (float) words.length));
	}
	
	@Test
	public void testPerformance2() {
		Finder f = new Finder(new File("/home/jens/Desktop/web1tIndex"));
		
		String[] words = {"filmtauscher", "minimalanforderungen", "berufungsinstanz"};
		
		long time = 0;
		long count = 0;
		for (String word : words) {
			for (int i = 1; i < word.length(); i++) {
				String searchFor = word.substring(0, i);
				
				long start = System.currentTimeMillis();
				f.contains(searchFor);
				long end = System.currentTimeMillis();
				
				time += end-start;
				count++;
				
				System.out.println("Time for '"+searchFor+"' (ms): " + (end-start));
			}
		}
		
		System.out.println("Average time (ms): " + ((float) time/ (float) count));
	}
}
