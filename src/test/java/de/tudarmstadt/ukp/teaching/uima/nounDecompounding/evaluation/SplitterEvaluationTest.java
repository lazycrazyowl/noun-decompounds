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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.ISplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;

public class SplitterEvaluationTest {

	private class DummyAlgorithm implements ISplitAlgorithm {

		@Override
		public List<Split> split(String word) {
			List<Split> s = new ArrayList<Split>();
			// Simple return the normal word
			s.add(Split.createFromString(word));
			
			return s;
		}
		
	}
	
	@Test
	public void testCreateSplit() {
		SplitterEvaluation e = new SplitterEvaluation(new File("src/test/resources/ccorpus.txt"));
		
		Assert.assertEquals("blei+stift+strich(e)", e.createSplit("blei{N}+stift{N,V}+strich(e){N}").toString());
		Assert.assertEquals("ort(s)+netz+monopol", e.createSplit("ort|s{N,V}+netz{N,V}+monopol{N}").toString());
		Assert.assertEquals("dat(en)+satz+format", e.createSplit("dat,um|en{N}+satz{N}+format{N}").toString());
		Assert.assertEquals("schul+referat", e.createSplit("schul,e{N}+referat{N}").toString());
		Assert.assertEquals("sand+körn(er)", e.createSplit("sand{N}+kOrn(er){N}").toString());
		Assert.assertEquals("typ+abbildung", e.createSplit("typ{N,V}+ab{PREP}+bildung{N}").toString());
	}
	
	@Test
	public void testEvaluation() {
		SplitterEvaluation e = new SplitterEvaluation(new File("src/test/resources/ccorpus.txt"));
		
		Assert.assertEquals((float) 1 / (float) 10, e.evaluate(new DummyAlgorithm()));
	}
}
