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
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.IRankList;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.IRankListAndTree;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.IRankTree;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.ISplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitTree;


public class RankingEvaluationTest {

	protected class SplitterMock implements ISplitAlgorithm {

		String[] words = new String[]{"zugangsliste", "fachkameras", "doppelprozessormaschine", "filmtauscher"};
		String[] splits = new String[] {"zugangs+liste", "fach+kamera(s)", "doppel+prozessor+maschine", "film+tauscher"};
		Map<String, String> map = new HashMap<String, String>();
		
		public SplitterMock() {
			for (int i = 0; i < words.length; i++) {
				map.put(words[i], splits[i]);
			}
		}
		
		@Override
		public SplitTree split(String word) {
			if (map.containsKey(word)) {
				word = map.get(word);
			}
			
			return new SplitTree(word);
		}
		
	}
	
	protected class ListRankerMock implements IRankList {

		@Override
		public Split highestRank(List<Split> splits) {
			return this.rank(splits).get(0);
		}

		@Override
		public List<Split> rank(List<Split> splits) {
			// Add dummys at the end to have more than one object
			splits.add(Split.createFromString("dummy"));
			splits.add(Split.createFromString("dummy+split"));
			return splits;
		}
		
	}
	
	protected  class TreeRankerMock implements IRankTree {

		@Override
		public Split highestRank(SplitTree tree) {
			return tree.getRoot().getValue();
		}
		
	}
	
	protected class ListAndTreeRankerMock implements IRankListAndTree {
		@Override
		public Split highestRank(List<Split> splits) {
			return this.rank(splits).get(0);
		}

		@Override
		public List<Split> rank(List<Split> splits) {
			// Add dummys at the end to have more than one object
			splits.add(Split.createFromString("dummy"));
			splits.add(Split.createFromString("dummy+split"));
			return splits;
		}
		
		@Override
		public Split highestRank(SplitTree tree) {
			return tree.getRoot().getValue();
		}		
	}
	
	
	@Test
	public void testListEvaluation() {
		try {
			RankingEvaluation e = new RankingEvaluation(new CcorpusReader(new File("src/test/resources/ccorpus.txt")));
			RankingEvaluation.Result r = e.evaluateList(new SplitterMock(), new ListRankerMock());
			
			Assert.assertEquals(0.4f, r.recall);
			Assert.assertEquals(0.5f, r.recallWithoutMorpheme);
			
			Assert.assertEquals(0.4f, r.recallAt2);
			Assert.assertEquals(0.5f, r.recallAt2WithoutMorpheme);
			
			Assert.assertEquals(0.4f, r.recallAt2);
			Assert.assertEquals(0.5f, r.recallAt2WithoutMorpheme);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTreeEvaluation() {
		try {
			RankingEvaluation e = new RankingEvaluation(new CcorpusReader(new File("src/test/resources/ccorpus.txt")));
			RankingEvaluation.Result r = e.evaluateTree(new SplitterMock(), new TreeRankerMock());
			
			Assert.assertEquals(0.4f, r.recall);
			Assert.assertEquals(0.5f, r.recallWithoutMorpheme);
			
			Assert.assertEquals(0f, r.recallAt2);
			Assert.assertEquals(0f, r.recallAt2WithoutMorpheme);
			
			Assert.assertEquals(0f, r.recallAt2);
			Assert.assertEquals(0f, r.recallAt2WithoutMorpheme);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testListAndTreeEvaluation() {
		try {
			RankingEvaluation e = new RankingEvaluation(new CcorpusReader(new File("src/test/resources/ccorpus.txt")));
			RankingEvaluation.Result[] r = e.evaluateListAndTree(new SplitterMock(), new ListAndTreeRankerMock());
			
			Assert.assertEquals(0.4f, r[0].recall);
			Assert.assertEquals(0.5f, r[0].recallWithoutMorpheme);
			
			Assert.assertEquals(0.4f, r[0].recallAt2);
			Assert.assertEquals(0.5f, r[0].recallAt2WithoutMorpheme);
			
			Assert.assertEquals(0.4f, r[0].recallAt2);
			Assert.assertEquals(0.5f, r[0].recallAt2WithoutMorpheme);
			
			
			Assert.assertEquals(0.4f, r[1].recall);
			Assert.assertEquals(0.5f, r[1].recallWithoutMorpheme);
			
			Assert.assertEquals(0f, r[1].recallAt2);
			Assert.assertEquals(0f, r[1].recallAt2WithoutMorpheme);
			
			Assert.assertEquals(0f, r[1].recallAt2);
			Assert.assertEquals(0f, r[1].recallAt2WithoutMorpheme);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
