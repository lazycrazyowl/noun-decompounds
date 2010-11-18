package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import junit.framework.Assert;

import org.junit.Test;

public class SplitTest {
	
	@Test
	public void testCreate() {
		Split s = Split.createFromString("aktion(s)+plan");
		
		Assert.assertEquals("aktion", s.getSplits().get(0).getWord());
		Assert.assertEquals("s", s.getSplits().get(0).getMorpheme());
		
		Assert.assertEquals("plan", s.getSplits().get(1).getWord());
		Assert.assertEquals(null, s.getSplits().get(1).getMorpheme());
	}
	
	@Test
	public void testToString() {
		SplitElement e1 = new SplitElement();
		e1.setWord("aktion");
		e1.setMorpheme("s");
		
		SplitElement e2 = new SplitElement();
		e2.setWord("plan");
		
		Split s = new Split();
		s.addSplitElement(e1);
		s.addSplitElement(e2);
		
		Assert.assertEquals("aktion(s)+plan", s.toString());
	}
	
	@Test
	public void testEquals() {
		SplitElement e1 = new SplitElement();
		e1.setWord("aktion");
		e1.setMorpheme("s");
		
		SplitElement e2 = new SplitElement();
		e2.setWord("plan");
		
		Split s1 = new Split();
		s1.addSplitElement(e1);
		s1.addSplitElement(e2);
		
		SplitElement e3 = new SplitElement();
		e3.setWord("aktion");
		e3.setMorpheme("s");
		
		SplitElement e4 = new SplitElement();
		e4.setWord("plan");
		
		Split s2 = new Split();
		s2.addSplitElement(e3);
		s2.addSplitElement(e4);
		
		Assert.assertTrue(s1.equals(s2));
		
		e2.setMorpheme("e");
		Assert.assertFalse(s1.equals(s2));
	}
}
