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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking;

import java.io.File;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.CcorpusReader;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.RankingEvaluation;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.LeftToRightSplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.Finder;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.NGram;

/**
 * Probability based ranking method
 * @author Jens Haase <je.haase@googlemail.com>
 *
 */
public class ProbabilityBased implements IRankList {

	public static double FREQUENCY = 391558376699d; 
	
	private Finder finder;

	/**
	 * Constructor
	 * @param aFinder
	 */
	public ProbabilityBased(Finder aFinder) {
		this.finder = aFinder;
	}
	
	@Override
	public Split highestRank(List<Split> splits) {
		return this.rank(splits).get(0);
	}

	@Override
	public List<Split> rank(List<Split> splits) {
		for (Split split : splits) {
			split.setWeight(this.calcRank(split));
		}
		
		Collections.sort(splits, Collections.reverseOrder());
		
		return splits;
	}
	
	/**
	 * Calculates the weight for a split
	 * @param split
	 * @return
	 */
	private float calcRank(Split split) {
		float result = 0;
		
		for (SplitElement elem : split.getSplits()) {
			result += -1 * Math.log((double) this.getFreq(elem) / FREQUENCY);
		}
		
		return result;
	}

	/**
	 * Returns the frequence for a split element
	 * @param elem
	 * @return
	 */
	private int getFreq(SplitElement elem) {
		int total = 0;
		
		for (NGram gram : finder.find(elem.getWord())) {
			total += gram.getFreq();
		}
		
		return total;
	}

	public static void main(String[] args) throws Exception {
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		LeftToRightSplitAlgorithm splitter = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		IRankList ranker = new ProbabilityBased(new Finder(new File("/home/jens/Desktop/web1tIndex")));
		
		RankingEvaluation e = new RankingEvaluation(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")));
		RankingEvaluation.Result result = e.evaluate(splitter, ranker, 1000);
		
		System.out.println("Result " + result.recall + " (without morpheme: " + result.recallWithoutMorpheme + ")");
	}
}
