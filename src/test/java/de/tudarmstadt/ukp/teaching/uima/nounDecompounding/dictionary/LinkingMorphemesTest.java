package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


public class LinkingMorphemesTest {

	@Test
	public void testStringConstructor() {
		LinkingMorphemes l = new LinkingMorphemes("s", "ens");
		Assert.assertEquals(2, l.getAll().size());
		Assert.assertEquals("s", l.getAll().get(0));
	}
	
	@Test
	public void testListConstructor() {
		List<String> list = new ArrayList<String>();
		list.add("s");
		list.add("ens");
		
		LinkingMorphemes l = new LinkingMorphemes(list);
		Assert.assertEquals(2, l.getAll().size());
		Assert.assertEquals("s", l.getAll().get(0));
	}
	
	@Test
	public void testFileConstructor() {
		LinkingMorphemes l = new LinkingMorphemes(new File("src/test/resources/dic/morphemes.txt"));
		Assert.assertEquals(2, l.getAll().size());
		Assert.assertEquals("s", l.getAll().get(0));
	}
}
