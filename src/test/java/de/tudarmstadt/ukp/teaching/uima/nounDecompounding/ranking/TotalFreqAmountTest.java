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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.LuceneIndexer;

public class TotalFreqAmountTest {

	File source = new File("src/test/resources/n-grams");
	static File index = new File("target/test/index");
	
	@Before
	public void setUp() throws Exception {
		index.mkdirs();
		
		// Create index
		LuceneIndexer indexer = new LuceneIndexer(source, index, 2);
		indexer.index();
	}
	
	@Test
	public void testCount() throws IOException {
		TotalFreqAmout amount = new TotalFreqAmout(index);
		Assert.assertEquals(new BigInteger("310"), amount.countFreq());
	}
	
	@After
	public void tearDown() throws Exception {
		// Delete index again
		for (File f : index.listFiles()) {
			for (File _f: f.listFiles()) {
				_f.delete();
			}
			f.delete();
		}
		
		index.delete();
	}
}
