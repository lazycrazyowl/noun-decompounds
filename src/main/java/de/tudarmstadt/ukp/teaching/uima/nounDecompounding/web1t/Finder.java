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
