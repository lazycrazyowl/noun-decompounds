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
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 * Caluclates the total amount of frequency.
 * This value is needed for the proabibliy/mutal based method
 * @author Jens Haase <je.haase@googlemail.com>
 *
 */
public class TotalFreqAmout {

	private List<FSDirectory> dirs = new ArrayList<FSDirectory>();

	public TotalFreqAmout(File aWeb1tLucenceIndex) {
		try {
			if (this.checkForIndex(aWeb1tLucenceIndex)) {
				FSDirectory dir = FSDirectory.open(aWeb1tLucenceIndex);
				dir.setReadChunkSize(52428800);
				dirs.add(dir);
			} else {
				for (File f : aWeb1tLucenceIndex.listFiles()) {
					if (f.isDirectory() && this.checkForIndex(f)) {
						FSDirectory dir = FSDirectory.open(f);
						dir.setReadChunkSize(52428800);
						dirs.add(dir);
					}
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the folder is a lucence index
	 * @param indexFolder
	 * @return
	 */
	private boolean checkForIndex(File indexFolder) {
		File[] files = indexFolder.listFiles();
		boolean result = false;
		
		for (File file : files) {
			if (file.isFile() && file.getName().startsWith("segments")) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Adds all frequency values
	 * @return
	 * @throws IOException
	 */
	public BigInteger countFreq() throws IOException {
		BigInteger count = BigInteger.valueOf(0);
		for(FSDirectory dir: this.dirs) {
			count = count.add(this.countFreq(dir));
		}
		return count;
	}
	
	/**
	 * Adds all frequency values for a special directory
	 * @return
	 * @throws IOException
	 */
	protected BigInteger countFreq(FSDirectory dir) throws IOException {
		BigInteger count = BigInteger.valueOf(0);
		
		IndexReader reader = IndexReader.open(dir);
		for (int i = 0; i < reader.maxDoc(); i++) {
			if (reader.isDeleted(i)) {
				continue;
			}
			
			Document doc = reader.document(i);
			count = count.add(new BigInteger(doc.get("freq")));
		}
		
		return count;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("index")
				.withDescription("The path to the web1t lucene index")
				.hasArg().isRequired().create());
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println( "Error: " + e.getMessage() );
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("countTotalFreq", options);
			return;
		}
		
		TotalFreqAmout amount = new TotalFreqAmout(new File(cmd.getOptionValue("index")));
		System.out.println("Total amount: " + amount.countFreq());
	}

}
