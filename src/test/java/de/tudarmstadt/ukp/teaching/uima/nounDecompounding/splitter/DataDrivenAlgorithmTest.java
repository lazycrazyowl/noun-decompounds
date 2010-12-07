package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.SimpleDictionary;

public class DataDrivenAlgorithmTest {

	@Test
	public void testSplit() {
		SimpleDictionary dict = new SimpleDictionary("friedens", "politik",
				"friedenspolitik", "friedensverhaltungen", "friedenshaltung",
				"frittieren", "friseur", "außenpolitik", "innenpolitik");
		LinkingMorphemes morphemes = new LinkingMorphemes("en", "s", "ens");

		DataDrivenAlgorithm algo = new DataDrivenAlgorithm(dict, morphemes);
		List<Split> result = algo.split("friedenspolitik").getAllSplits();

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("friedenspolitik", result.get(0).toString());
		Assert.assertEquals("friedens+politik", result.get(1).toString());
	}
}
