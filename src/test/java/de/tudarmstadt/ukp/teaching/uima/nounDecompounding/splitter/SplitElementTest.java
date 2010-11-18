package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import junit.framework.Assert;

import org.junit.Test;

public class SplitElementTest {

	@Test
	public void testCreate() {
		SplitElement e = SplitElement.createFromString("aktion(s)");
		
		Assert.assertEquals("aktion", e.getWord());
		Assert.assertEquals("s", e.getMorpheme());
		
		e = SplitElement.createFromString("plan");
		Assert.assertEquals("plan", e.getWord());
		Assert.assertEquals(null, e.getMorpheme());
	}
	
	@Test
	public void testToString() {
		SplitElement e = new SplitElement();
		e.setWord("aktion");
		e.setMorpheme("s");
		Assert.assertEquals("aktion(s)", e.toString());
		
		e.setMorpheme(null);
		Assert.assertEquals("aktion", e.toString());
	}
	
	@Test
	public void testEquals() {
		SplitElement e1 = new SplitElement();
		e1.setWord("aktion");
		e1.setMorpheme("s");
		
		SplitElement e2 = new SplitElement();
		e2.setWord("aktion");
		e2.setMorpheme("s");
		
		Assert.assertTrue(e1.equals(e1));
		
		e2.setMorpheme(null);
		Assert.assertFalse(e1.equals(e2));
	}
}
