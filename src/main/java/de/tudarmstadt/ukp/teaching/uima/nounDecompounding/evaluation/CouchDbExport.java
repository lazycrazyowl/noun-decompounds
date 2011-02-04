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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.ISplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.LeftToRightSplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitTree;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie.ValueNode;

/**
 * Uses a splitting algorithm to split words.
 * The resulting trees will exported to couchdb.
 * 
 * Then the data can be used to visualize the tree
 * in a browser.
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 *
 */
public class CouchDbExport {
	
	private CcorpusReader reader;
	private CouchDbConnector db;

	public CouchDbExport(CcorpusReader aCcorpusReader, CouchDbConnector dbCon) {
		this.reader = aCcorpusReader;
		
		this.db = dbCon;
		db.createDatabaseIfNotExists();
	}
	
	/**
	 * Export data to couchdb
	 * @param algo The splitting algorithm
	 * @param limit Limits the amount of data that is exported.
	 */
	public void export(ISplitAlgorithm algo, int limit) {
		try {
			Split split;
			List<Object> bulk = new ArrayList<Object>();
			int i = 0;
			
			while ((split = reader.readSplit()) != null && (i<limit || limit<0)) {
				bulk.add(this.createDoc(split, algo.split(split.getWord())));
				i++;
				
				if (i % 10000 == 0) {
					this.db.executeBulk(bulk);
					bulk = new ArrayList<Object>();
				}
			}
			this.db.executeBulk(bulk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new couchdb document
	 * @param correct The correct split
	 * @param tree The split tree from the splitting algorithm
	 * @return
	 */
	private Object createDoc(Split correct, SplitTree tree) {
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("word", tree.getRoot().getValue().getWord());
		doc.put("correct", correct.toString());
		doc.put("tree", this.createNode(tree.getRoot()));
		
		return doc;
	}

	/**
	 * Creates all nodes for the tree
	 * @param node
	 * @return
	 */
	private Map<String, Object> createNode(ValueNode<Split> node) {
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("id", node.hashCode());
		doc.put("name", this.generateName(node.getValue().getSplits()));
		
		List<Object> children = new ArrayList<Object>();
		for (ValueNode<Split> child : node.getChildren()) {
			children.add(this.createNode(child));
		}
		doc.put("children", children);
		
		return doc;
	}
	
	/**
	 * Creates a html element from a split
	 * @param elements
	 * @return
	 */
	private String generateName(List<SplitElement> elements) {
		String s = "";
		for (int i = 0; i < elements.size(); i++) {
			SplitElement splitElement = elements.get(i);
			
			if (splitElement.shouldSplitAgain()) {
				s += "<span class='shouldSplit'>";
			}
			
			s += splitElement.toString();
			
			if (splitElement.shouldSplitAgain()) {
				s += "</span>";
			}
			
			if (i < elements.size() -1) {
				s+="+";
			}
		}
		
		return s;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("host")
				.withDescription("(optional) The couchdb host. default: 127.0.0.1")
				.hasArg().create());
		options.addOption(OptionBuilder.withLongOpt("port")
				.withDescription("(optional) The couchdb port. default: 5984")
				.hasArg().create());
		options.addOption(OptionBuilder.withLongOpt("username")
				.withDescription("(optional) The couchdb username. default: <empty>")
				.hasArg().create());
		options.addOption(OptionBuilder.withLongOpt("password")
				.withDescription("(optional) The couchdb password. default: <empty>")
				.hasArg().create());
		options.addOption(OptionBuilder.withLongOpt("dbname")
				.withDescription("(optional) The couchdb database name. default: noun_decompounding")
				.hasArg().create());
		options.addOption(OptionBuilder.withLongOpt("limit")
				.withDescription("(optional) The amount of documents you want to export. default: all")
				.hasArg().create());
		
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
		String host = (cmd.hasOption("host")) ? cmd.getOptionValue("host") : "127.0.0.1";
		int port = Integer.parseInt((cmd.hasOption("port")) ? cmd.getOptionValue("port") : "5984");
		String username = (cmd.hasOption("username")) ? cmd.getOptionValue("username") : "";
		String password = (cmd.hasOption("password")) ? cmd.getOptionValue("password") : "";
		String dbName = (cmd.hasOption("dbname")) ? cmd.getOptionValue("dbname") : "";
		int limit = (cmd.hasOption("limit")) ? Integer.parseInt(cmd.getOptionValue("limit")) : Integer.MAX_VALUE;
		
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		HttpClient httpClient = new StdHttpClient.Builder().host(host).port(port).username(username).password(password).build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector db = new StdCouchDbConnector(dbName, dbInstance);
		
		try {
			CouchDbExport exporter = new CouchDbExport(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")), db);
			exporter.export(algo, limit);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
