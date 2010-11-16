package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;


public class SimpleDictionaryTest {

	@Test
	public void testContains() {
		SimpleDictionary dict = new SimpleDictionary(new File("src/test/resources/dic/simple.dic"));
		Assert.assertEquals(3, dict.getWords().size());
		
		Assert.assertTrue(dict.contains("word1"));
		Assert.assertTrue(dict.contains("word2"));
		Assert.assertTrue(dict.contains("word3"));
	}
}
