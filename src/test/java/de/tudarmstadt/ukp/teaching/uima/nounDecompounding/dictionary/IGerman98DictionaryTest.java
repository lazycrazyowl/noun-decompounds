package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class IGerman98DictionaryTest {

	@Test
	public void testContains() {
		IGerman98Dictionary dict = new IGerman98Dictionary(new File("src/test/resources/dic/igerman98.dic"));
		Assert.assertEquals(3, dict.getWords().size());
		
		Assert.assertTrue(dict.contains("Äbte"));
		Assert.assertTrue(dict.contains("Äbtissin"));
		Assert.assertTrue(dict.contains("Ächten"));
	}
}
