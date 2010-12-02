package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;

public class TrieTest {

	@Test
	public void testAddSorted() {
		Trie t = new Trie();

		t.addWord("abc");
		Assert.assertEquals(new Integer(1), t.findWord("a").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("ab").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("abc").getValue());
		
		t.addWord("abcde");
		Assert.assertEquals(new Integer(2), t.findWord("a").getValue());
		Assert.assertEquals(new Integer(2), t.findWord("ab").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("abc").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("abcd").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("abcde").getValue());
		
		t.addWord("abde");
		Assert.assertEquals(new Integer(3), t.findWord("a").getValue());
		Assert.assertEquals(new Integer(3), t.findWord("ab").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("abd").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("abde").getValue());
	}
	
	@Test
	public void testAddUnsorted() {
		Trie t = new Trie();

		t.addWord("abde");
		Assert.assertEquals(new Integer(1), t.findWord("a").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("ab").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("abd").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("abde").getValue());
		
		t.addWord("abc");
		Assert.assertEquals(new Integer(2), t.findWord("a").getValue());
		Assert.assertEquals(new Integer(2), t.findWord("ab").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("abc").getValue());
		
		t.addWord("abcde");
		Assert.assertEquals(new Integer(3), t.findWord("a").getValue());
		Assert.assertEquals(new Integer(3), t.findWord("ab").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("abc").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("abcd").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("abcde").getValue());
	}
	
	@Test
	public void testSimpleDict() {
		IGerman98Dictionary dict = new IGerman98Dictionary(new File("src/test/resources/dic/igerman98.dic"), new File("src/test/resources/dic/igerman98.aff"));
		Trie t = Trie.createForDict(dict);
		
		Assert.assertEquals(new Integer(1), t.findWord("h").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("hel").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("hello").getValue());
		
		Assert.assertEquals(new Integer(2), t.findWord("t").getValue());
		Assert.assertEquals(new Integer(2), t.findWord("tr").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("try").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("tri").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("tried").getValue());
		
		Assert.assertEquals(new Integer(2), t.findWord("w").getValue());
		Assert.assertEquals(new Integer(2), t.findWord("wor").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("work").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("worke").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("worked").getValue());
	}
	
	@Test
	public void testSimpleDictReverse() {
		IGerman98Dictionary dict = new IGerman98Dictionary(new File("src/test/resources/dic/igerman98.dic"), new File("src/test/resources/dic/igerman98.aff"));
		Trie t = Trie.createForDictReverse(dict);
		
		Assert.assertEquals(new Integer(2), t.findWord("d").getValue());
		Assert.assertEquals(new Integer(2), t.findWord("de").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("dei").getValue());
		Assert.assertEquals(new Integer(0), t.findWord("deirt").getValue());
		
		Assert.assertEquals(new Integer(2), t.findWord("k").getValue());
		Assert.assertEquals(new Integer(1), t.findWord("o").getValue());
	}
}
