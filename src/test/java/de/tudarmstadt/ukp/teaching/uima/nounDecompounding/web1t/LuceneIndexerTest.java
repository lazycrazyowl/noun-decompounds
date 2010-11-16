package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.io.File;

import junit.framework.Assert;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LuceneIndexerTest {

	File source = new File("src/test/resources/n-grams");
	File index = new File("target/test/LuceneIndexer");
	
	@Before
	public void setUp() throws Exception {
		// Create folder if not exists
		index.mkdirs();
		
		// Create index
		LuceneIndexer indexer = new LuceneIndexer(source, index);
		indexer.index();
	}
	
	@Test
	public void testSearch() throws Exception {
		// Check if fields and all documents exists
		IndexReader ir = IndexReader.open(FSDirectory.open(index));
		Assert.assertEquals("Number of documents", 3, ir.numDocs());
		Document doc = ir.document(0);
		Assert.assertNotNull("Field: gram", doc.getField("gram"));
		Assert.assertNotNull("Field: freq", doc.getField("freq"));
		ir.close();

		// Search on the index
		IndexSearcher searcher = new IndexSearcher(FSDirectory.open(index));
		QueryParser p = new QueryParser(Version.LUCENE_30, "token", new StandardAnalyzer(Version.LUCENE_30));
		Query q = p.parse("gram:relax");
		Assert.assertEquals("Hit count 'Relax'", 3, searcher.search(q, 100).totalHits);
		
		q = p.parse("gram:couch");
		Assert.assertEquals("Hit count 'couch'", 1, searcher.search(q, 100).totalHits);
		
		q = p.parse("gram:relax AND gram:couch");
		Assert.assertEquals("Hit count 'couch'", 1, searcher.search(q, 100).totalHits);
		
		q = p.parse("gram:couchdb");
		Assert.assertEquals("Hit count 'couchdb'", 1, searcher.search(q, 100).totalHits);
		searcher.close();
	}
	
	@Test
	public void testData() throws Exception {
		IndexReader ir = IndexReader.open(FSDirectory.open(index));
		IndexSearcher searcher = new IndexSearcher(FSDirectory.open(index));
		QueryParser p = new QueryParser(Version.LUCENE_30, "gram", new StandardAnalyzer(Version.LUCENE_30));
		
		LuceneIndexer indexer = new LuceneIndexer(source, index);
		indexer.index();

		// Test if all data is set correct
		Query q = p.parse("gram:couch");
		Document doc = ir.document(searcher.search(q, 100).scoreDocs[0].doc);
		Assert.assertEquals(new Integer(100), Integer.valueOf(doc.get("freq")));
		Assert.assertEquals("relax on the couch", doc.get("gram"));

		ir.close();
		searcher.close();		
	}
	
	
	@After
	public void tearDown() throws Exception {
		// Delete index again
		for (File f : index.listFiles()) {
			f.delete();
		}
		
		index.delete();
	}
}
