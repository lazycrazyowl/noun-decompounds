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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.CcorpusReader;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation.RankingEvaluation;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.LeftToRightSplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitTree;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.ValueNode;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.Finder;

/**
 * Mutual informationen based ranking algorithm.
 * See doc folder for more information
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class MutualInformationBased extends AbstractRanker implements IRankListAndTree {

	public static BigInteger FREQUENCY = new BigInteger("143782944956");

	public MutualInformationBased(Finder aFinder) {
		super(aFinder);
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
		
		List<Split> filtered = new ArrayList<Split>();
		for (Split split : splits) {
			if (!(Float.isInfinite(split.getWeight()) || Float.isNaN(split.getWeight()))) {
				filtered.add(split);
			}
		}
		
		if (filtered.size() == 0) {
			filtered = splits;
		}
		
		Collections.sort(filtered);
		return filtered;
	}

	/**
	 * Calculates the weight for a split
	 * 
	 * @param split
	 * @return
	 */
	private float calcRank(Split split) {
		double total = 0;
		double count = 0;
		
		if (split.getSplits().size() == 1) {
			// Entropy for single words
			SplitElement w = split.getSplits().get(0);
			double p = this.freq(w).doubleValue() / FREQUENCY.doubleValue();
			
			return (float) ((-1) * p * Math.log(p)); 
		}
		
		for (int i = 1; i < split.getSplits().size(); i++) {
			// Mutual Information for splits.
			
			SplitElement w1 = split.getSplits().get(i - 1);
			SplitElement w2 = split.getSplits().get(i);

			double a = this.freq(w1, w2).multiply(FREQUENCY).doubleValue();
			double b = this.freq(w1).multiply(this.freq(w2)).doubleValue();
			double mutal;
			
			if  (a == 0d) {
				mutal = 0d;
			} else {
				mutal = Math.log(a / b);
			}
			
			
			total += mutal;
			count++;
		}
		
		return (float) (total / count);
	}
	
	@Override
	public Split highestRank(SplitTree tree) {
		return this.highestRank(tree.getRoot());
	}
	
	private Split highestRank(ValueNode<Split> parent) {
		List<Split> children = parent.getChildrenValues();
		if (children.size() == 0) {
			return parent.getValue();
		}
		
		children.add(parent.getValue());
		List<Split> result = this.rank(children);
		
		if (result.get(0).equals(parent.getValue())) {
			return parent.getValue();
		} else {
			for (ValueNode<Split> split : parent.getChildren()) {
				if (result.get(0).equals(split.getValue())) {
					return this.highestRank(split);
				}
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		CommandLine cmd = AbstractRanker.parseArgs(args);
		if (cmd == null) {
			return;
		}
		
		int limit = AbstractRanker.getLimitOption(cmd);
		String indexPath = AbstractRanker.getIndexPathOption(cmd);
		
		
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		LeftToRightSplitAlgorithm splitter = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		MutualInformationBased ranker = new MutualInformationBased(new Finder(new File(indexPath)));
		
		RankingEvaluation e = new RankingEvaluation(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")));
		RankingEvaluation.Result[] result = e.evaluateListAndTree(splitter, ranker, limit);
		
		// Print result
		System.out.println("List:");
		System.out.println(result[0].toString());
		System.out.println("Tree:");
		System.out.println(result[1].toString());
	}
}
