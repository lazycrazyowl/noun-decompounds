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
		public float recallAt2;
		public float recallAt2WithoutMorpheme;
		public float recallAt3;
		public float recallAt3WithoutMorpheme;
		
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append("Result:\n");
			buf.append("\tCorrect: "+recall+" (without Morpheme: " +recallWithoutMorpheme+")\n");
			buf.append("\tCorrect@2: "+recallAt2+" (without Morpheme: " +recallAt2WithoutMorpheme+")\n");
			buf.append("\tCorrect@3: "+recallAt3+" (without Morpheme: " +recallAt3WithoutMorpheme+")");
			
			return buf.toString();
		}
	}

	public RankingEvaluation(CcorpusReader aReader) {
		this.reader = aReader;
	}
	
	public Result evaluate(ISplitAlgorithm splitter, IRankList ranker, int limit) {
		int total = 0, correct = 0, correctWithoutMorpheme = 0,
			correctAt2 = 0, correctAt2WithoutMorpheme = 0,
			correctAt3 = 0, correctAt3WithoutMorpheme = 0;
		
		try {
			Split split;
			List<Split> result;
			
			
			while ((split = reader.readSplit()) != null && total < limit) {
				result = ranker.rank(splitter.split(split.getWord()).getAllSplits());
				
				if (result.get(0).equals(split)) {
					correct++;
				} else {
					System.out.println(total + ": " + split + " -> " + result.get(0) + " " + this.getResultList(result));
				}
				
				if (result.get(0).equalWithoutMorpheme(split)) {
					correctWithoutMorpheme++;
				}
				
				if (result.size() >= 2 && (result.get(0).equals(split) || result.get(1).equals(split))) {
					correctAt2++;
				}
				if (result.size() >= 2 && (result.get(0).equalWithoutMorpheme(split) || result.get(1).equalWithoutMorpheme(split))) {
					correctAt2WithoutMorpheme++;
				}
				
				if (result.size() >= 3 && (result.get(0).equals(split) || result.get(1).equals(split) || result.get(2).equals(split))) {
					correctAt3++;
				}
				if (result.size() >= 3 && (result.get(0).equalWithoutMorpheme(split) || result.get(1).equalWithoutMorpheme(split) || result.get(2).equalWithoutMorpheme(split))) {
					correctAt3WithoutMorpheme++;
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
		r.recallAt2 = (float) correctAt2 / (float) total;
		r.recallAt2WithoutMorpheme = (float) correctAt2WithoutMorpheme / (float) total;
		r.recallAt3 = (float) correctAt3 / (float) total;
		r.recallAt3WithoutMorpheme = (float) correctAt3WithoutMorpheme / (float) total;
		return r;
	}
	
	private String getResultList(List<Split> splits) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		
		for (int i = 0; i < splits.size(); i++) {
			buffer.append(splits.get(i).toString());
			buffer.append(":");
			buffer.append(splits.get(i).getWeight());
			if (i < splits.size()-1) {
				buffer.append(", ");
			}
		}
		
		buffer.append("]");
		
		return buffer.toString();
	}
	
	public Result evaluate(ISplitAlgorithm splitter, IRankList ranker) {
		return this.evaluate(splitter, ranker, Integer.MAX_VALUE);
	}
}
