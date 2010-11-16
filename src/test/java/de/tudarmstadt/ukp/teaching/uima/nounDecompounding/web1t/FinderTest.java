package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FinderTest {

	File source = new File("src/test/resources/n-grams");
	File index = new File("target/test/LuceneIndexer");
	
	@Before
	public void setUp() throws Exception {
		index.mkdirs();
		
		// Create index
		LuceneIndexer indexer = new LuceneIndexer(source, index);
		indexer.index();
	}
	
	@Test
	public void testFinder() throws Exception {
		// Search and check if data is correct
		Finder f = new Finder(index);
		List<NGram> result = f.find("couch");
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("relax on the couch", result.get(0).getGram());
		Assert.assertEquals(4, result.get(0).getN());
		Assert.assertEquals(100, result.get(0).getFreq());
		
		result = f.find("relax couch");
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("relax on the couch", result.get(0).getGram());
		Assert.assertEquals(4, result.get(0).getN());
		Assert.assertEquals(100, result.get(0).getFreq());
		
		result = f.find("relax");
		Assert.assertEquals(3, result.size());
	}
	
	@After
	public void tearDown() throws Exception {
		// Delete index at the end
		for (File f : index.listFiles()) {
			f.delete();
		}
		
		index.delete();
	}
}
