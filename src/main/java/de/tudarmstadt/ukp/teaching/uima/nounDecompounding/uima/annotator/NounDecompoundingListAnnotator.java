package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.annotator;

import java.io.File;

import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.FrequencyBased;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.IRankList;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.MutualInformationBased;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking.ProbabilityBased;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.Finder;

/**
 * Uses list based ranking methods.
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class NounDecompoundingListAnnotator extends AbstractNounDecompoundAnnotator {

	/**
	 * All possible list ranker
	 */
	public enum Ranker {
		FREQUENCY, MUTUAL_INFORMATION, PROBABILITY
	}
	
	private IRankList ranker;
	
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
		Split s = this.ranker.highestRank(this.getSplitter().split(word).getAllSplits());
		return s;
	}

}
