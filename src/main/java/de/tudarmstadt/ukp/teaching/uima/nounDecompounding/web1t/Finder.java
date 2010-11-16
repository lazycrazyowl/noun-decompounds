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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class searches on the Lucene Index for n-grams.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class Finder {

	IndexReader ir;
	IndexSearcher searcher;
	QueryParser parser;
	
	/**
	 * Constructor for the finder.
	 * 
	 * In case of performance it is recommended
	 * to use only one instance of this class.
	 * 
	 * @param indexFolder The folder to the lucene index
	 */
	public Finder(File indexFolder) {
		try {
			this.ir = IndexReader.open(FSDirectory.open(indexFolder));
			this.searcher = new IndexSearcher(FSDirectory.open(indexFolder));
			this.parser = new QueryParser(Version.LUCENE_30, "gram", new StandardAnalyzer(Version.LUCENE_30));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		try {
			ScoreDoc[] results = searcher.search(parser.parse(this.buildQuery(token)), 100).scoreDocs;
			Document doc;
			
			for (ScoreDoc scoreDoc : results) {
				 doc = ir.document(scoreDoc.doc);
				 ngrams.add(new NGram(doc.get("gram"), Integer.valueOf(doc.get("freq"))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ngrams;
	}
	
	/**
	 * Build a lucene query string
	 * from a list of token
	 * @param token A list of token
	 * @return
	 */
	private String buildQuery(String[] token) {
		String query = "";
		
		for (int i = 0; i < token.length; i++) {
			query += "gram:" + token[i];
			if (i != token.length - 1) {
				query += " AND ";
			}
		}
		
		return query;
	}
}
