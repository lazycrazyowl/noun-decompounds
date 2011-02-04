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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.annotator;

import java.io.File;

import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.FrequencyBased;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.IRankTree;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.MutualInformationBased;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.ProbabilityBased;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.Finder;

/**
 * Uses tree based ranking methods.
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class NounDecompoundingTreeAnnotator extends AbstractNounDecompoundAnnotator {

	/**
	 * All possible tree ranker
	 */
	public enum Ranker {
		FREQUENCY, MUTUAL_INFORMATION, PROBABILITY
	}
	
	private IRankTree ranker;
	
	protected void initializeRanker() throws ResourceInitializationException {
		if (this.getRankerName().equals(Ranker.FREQUENCY.toString())) {
			this.ranker = new FrequencyBased(new Finder(new File(this.getIndexPath())));
		} else if (this.getRankerName().equals(Ranker.MUTUAL_INFORMATION.toString())) {
			this.ranker = new MutualInformationBased(new Finder(new File(this.getIndexPath())));
		} else if (this.getRankerName().equals(Ranker.PROBABILITY.toString())) {
			this.ranker = new ProbabilityBased(new Finder(new File(this.getIndexPath())));
		} else {
			throw new ResourceInitializationException("The configuraiton value '"+PARAM_RANKER+"' has a wrong value. See NounDecompoundingListAnnotator.Ranker for possible values", new Object[]{});
		}
	}

	protected Split getHighestRank(String word) {
		Split s = this.ranker.highestRank(this.getSplitter().split(word));
		return s;
	}

}
