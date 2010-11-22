package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.SimpleDictionary;

public class LeftToRightSplitAlgorithmTest {
	
	@Test
	public void testSplit1() {
		IDictionary dict = new SimpleDictionary("Akt" ,"ion", "plan", "Aktion", "Aktionsplan");
		LinkingMorphemes morphemes = new LinkingMorphemes("s");
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		List<Split> result = algo.split("Aktionsplan");
		// Akt+ion(s)+plan, Aktion(s)+plan, Aktionsplan
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("akt+ion(s)+plan", result.get(0).toString());
		Assert.assertEquals("aktion(s)+plan", result.get(1).toString());
		Assert.assertEquals("aktionsplan", result.get(2).toString());
	}
	
	@Test
	public void testSplit2() {
		IDictionary dict = new SimpleDictionary("Donau" ,"dampf", "schiff", "fahrt", "dampfschiff", "schifffahrt");
		LinkingMorphemes morphemes = new LinkingMorphemes("s");
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		List<Split> result = algo.split("Donaudampfschifffahrt");
		// Donau+dampf+schiff+fahrt, Donau+dampfschiff+fahrt, Donau+dampf+schifffahrt
		Assert.assertEquals(3, result.size());
	}
	
	@Test
	public void testSplit3() {
		IDictionary dict = new SimpleDictionary("Super" ,"mann", "anzug", "Supermann", "anzug");
		LinkingMorphemes morphemes = new LinkingMorphemes("s");
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		List<Split> result = algo.split("Supermannanzug");
		// Super+mann+anzug, Supermann+anzug
		Assert.assertEquals(2, result.size());
	}
	
	@Test
	public void testSplit4() {
		IDictionary dict = new SimpleDictionary("dreh" ,"zahl", "änderung");
		LinkingMorphemes morphemes = new LinkingMorphemes("en");
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		List<Split> result = algo.split("drehzahländerungen");
		// dreh+zahl+änderung(en)
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("dreh+zahl+änderung(en)", result.get(0).toString());
	}
}
