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

import java.io.IOException;
import java.util.List;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.IRankList;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.ISplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;

public class RankingEvaluation {

	private CcorpusReader reader;
	
	public class Result {
		public float recall;
		public float recallWithoutMorpheme;
		public float precision;
	}

	public RankingEvaluation(CcorpusReader aReader) {
		this.reader = aReader;
	}
	
	public Result evaluate(ISplitAlgorithm splitter, IRankList ranker, int limit) {
		int total = 0, correct = 0, correctWithoutMorpheme = 0;
		
		try {
			Split split;
			List<Split> result;
			
			
			while ((split = reader.readSplit()) != null && total < limit) {
				result = ranker.rank(splitter.split(split.getWord()).getAllSplits());
				
				if (result.get(0).equals(split)) {
					correct++;
				} else {
					System.out.println(total + ": " + split + " -> " + result.get(0) + " " + result);
				}
				
				if (result.get(0).equalWithoutMorpheme(split)) {
					correctWithoutMorpheme++;
				}
				
				total++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Result r = new Result();
		r.recall = (float) correct / (float) total;
		r.recallWithoutMorpheme = (float) correctWithoutMorpheme / (float) total;
		r.precision = 0;
		return r;
	}
	
	public Result evaluate(ISplitAlgorithm splitter, IRankList ranker) {
		return this.evaluate(splitter, ranker, Integer.MAX_VALUE);
	}
}
