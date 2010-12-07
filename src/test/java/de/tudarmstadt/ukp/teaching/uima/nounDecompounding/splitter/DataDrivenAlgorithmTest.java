/**
 * Copyright (c) 2010 Jens Haase <je.haase@googlemail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
