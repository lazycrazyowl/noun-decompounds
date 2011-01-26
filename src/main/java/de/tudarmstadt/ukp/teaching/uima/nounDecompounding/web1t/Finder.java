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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;

/**
 * This class searches on the Lucene Index for n-grams.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class Finder implements IDictionary {

	private ParallelMultiSearcher searcher;
	private LRUCache<String, List<NGram>> cache = new LRUCache<String, List<NGram>>();
	
	/**
	 * Constructor for the finder.
	 * 
	 * In case of performance it is recommended
	 * to use only one instance of this class.
	 * 
	 * @param indexFolder The folder to the lucene index or a folder with multiple indexes.
	 */
	public Finder(File indexFolder) {
		try {
			List<IndexSearcher> searcherList = new ArrayList<IndexSearcher>();
			if (this.checkForIndex(indexFolder)) {
				FSDirectory dir = FSDirectory.open(indexFolder);
				dir.setReadChunkSize(52428800);
				searcherList.add(new IndexSearcher(dir));
			} else {
				for (File f : indexFolder.listFiles()) {
					if (f.isDirectory() && this.checkForIndex(f)) {
						FSDirectory dir = FSDirectory.open(f);
						dir.setReadChunkSize(52428800);
						searcherList.add(new IndexSearcher(dir));
					}
				}
			}
			
			searcher = new ParallelMultiSearcher(searcherList.toArray(new IndexSearcher[0]));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean checkForIndex(File indexFolder) {
		File[] files = indexFolder.listFiles();
		boolean result = false;
		
		for (File file : files) {
			if (file.isFile() && file.getName().startsWith("segments")) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	/**
	 * Find all n-grams in the index.
	 * @param gram A String of token splitted by space
	 * @return
	 */
	public List<NGram> find(String gram) {
		return this.find(gram.split(" "));
	}
	
	/**
	 * Find all n-grams in the index.
	 * @param token A list of tokens
	 * @return
	 */
	public List<NGram> find(String[] token) {
		List<NGram> ngrams = new ArrayList<NGram>();
		
		BooleanQuery q = new BooleanQuery();
		for (String t : token) {
			q.add(new TermQuery(new Term("gram", t.toLowerCase())), Occur.MUST);
		}
		
		if (cache.containsKey(q.toString())) {
			return cache.get(q.toString());
		}
		
		try {
			ScoreDoc[] results = searcher.search(q, 100).scoreDocs;
			Document doc;
			
			for (ScoreDoc scoreDoc : results) {
				 doc = searcher.doc(scoreDoc.doc);
				 ngrams.add(new NGram(doc.get("gram"), Integer.valueOf(doc.get("freq"))));
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		cache.put(q.toString(), ngrams);
		
		return ngrams;
	}

	@Override
	public boolean contains(String word) {
		List<NGram> possible = this.find(word);
		
		for (NGram nGram : possible) {
			if (nGram.getGram().equals(word)) {
				return true;
			}
		}
		
		return false;
	}
}
