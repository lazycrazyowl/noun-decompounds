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
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.Finder;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.LuceneIndexer;

public class FrequencyBasedTest {

	static File source = new File("src/test/resources/ranking/n-grams");
	static File index = new File("src/test/resources/ranking/index");
	
	@BeforeClass
	public static void createIndex() throws Exception {
		index.mkdirs();
		
		LuceneIndexer indexer = new LuceneIndexer(source, index);
		indexer.index();
	}
	
	@Test
	public void testRank() {
		FrequencyBased ranker = new FrequencyBased(new Finder(index));
		
		List<Split> list = new ArrayList<Split>();
		Split s1 = Split.createFromString("Aktionsplan");
		list.add(s1);
		Split s2 = Split.createFromString("Akt+ion(s)+plan");
		list.add(s2);
		Split s3 = Split.createFromString("Aktion(s)+plan");
		list.add(s3);
		
		List<Split> result = ranker.rank(list);
		Assert.assertEquals(s1, result.get(1));
		Assert.assertEquals(s2, result.get(2));
		Assert.assertEquals(s3, result.get(0));
		
		
		Assert.assertEquals(s3, ranker.highestRank(list));
	}
	
	@AfterClass
	public static void removeIndex() {
		for (File f: index.listFiles()) {
			f.delete();
		}
		index.delete();
	}
}
