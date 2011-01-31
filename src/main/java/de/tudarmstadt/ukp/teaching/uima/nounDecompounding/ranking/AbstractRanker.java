package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.ranking;

import java.math.BigInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.Finder;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.NGram;

/**
 * Contains base method for the ranking algorithms
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 *
 */
public abstract class AbstractRanker {

	private Finder finder;

	/**
	 * Constructor
	 * @param aFinder
	 */
	public AbstractRanker(Finder aFinder) {
		this.finder = aFinder;
	}
	
	/**
	 * Gets the frequency of a Split Element
	 * @param w1
	 * @return
	 */
	protected BigInteger freq(SplitElement w1) {
		return this.freq(new String[] { w1.getWord() });
	}

	/**
	 * Returns the frequency of n-grams that contain
	 * both split elements
	 * @param w1
	 * @param w2
	 * @return
	 */
	protected BigInteger freq(SplitElement w1, SplitElement w2) {
		return this.freq(new String[] { w1.getWord(), w2.getWord() });
	}

	/**
	 * Returns the frequency for a array of words
	 * @param words
	 * @return
	 */
	protected BigInteger freq(String[] words) {
		BigInteger total = BigInteger.valueOf(0l);

		for (NGram gram : finder.find(words)) {
			total = total.add(BigInteger.valueOf(gram.getFreq()));
		}

		return total;
	}
	
	public final static String INDEX_OPTION = "luceneIndex";
	public final static String LIMIT_OPTION = "limit";
	
	@SuppressWarnings("static-access")
	protected static Options basicOptions() {
		// Set up options
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt(INDEX_OPTION)
				.withDescription("The path to the web1t lucene index")
				.hasArg().isRequired().create());
		options.addOption(OptionBuilder.withLongOpt(LIMIT_OPTION)
				.withDescription("(optional) Limit of results to evaluate")
				.hasArg().create());
		
		return options;
	}
	
	public static CommandLine parseArgs(String[] args) {
		CommandLineParser parser = new PosixParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(AbstractRanker.basicOptions(), args);
		} catch (ParseException e) {
			System.err.println( "Error: " + e.getMessage() );
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("AbstractRanker", AbstractRanker.basicOptions());
			return null;
		}
		
		return cmd;
	}
	
	public static int getLimitOption(CommandLine cmd) {
		int i = Integer.MAX_VALUE;
		if (cmd.hasOption(LIMIT_OPTION)) {
			i = Integer.valueOf(cmd.getOptionValue(LIMIT_OPTION));
		}
		
		return i;
	}
	
	public static String getIndexPathOption(CommandLine cmd) {
		return cmd.getOptionValue(INDEX_OPTION);
	}
}
