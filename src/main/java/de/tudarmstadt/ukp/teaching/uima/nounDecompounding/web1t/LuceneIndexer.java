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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;

/**
 * Index the Google Web1T corpus in lucene.
 * 
 * All values are stored in the index. The fields are
 *   * gram: The n-gram
 *   * freq: The frequence of the n-gram in the corpus
 *   
 * Note: This was only tested with the german corpus of Web1T.
 * The english one is much bigger and lucene can only handle
 * Integer.MAX_VALUE (2 147 483 647) documents per index.
 * Each n-gram is a document.
 * 
 * In the /bin folder is a script file to run the indexer.
 * Simple run: 
 * 
 *   ./bin/web1TLuceneIndexer.sh \
 *   	--web1t PATH/TO/FOLDER/WITH/ALL/EXTRACTED/N-GRAM/FILES \
 *   	--outputPath PAHT/TO/LUCENE/INDEX/FOLDER
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public class LuceneIndexer {

	private File web1tFolder;
	private File outputPath;
	private int indexes;
	private IDictionary dictionary;
	
	protected class Worker extends Thread {

		private List<File> files;
		private File output;
		private IDictionary dict;
		
		public Worker(List<File> fileList, File outputFolder, IDictionary aDictionary) {
			this.files = fileList;
			this.output = outputFolder;
			this.dict = aDictionary;
			
			this.output.mkdirs();
		}
		
		@Override
		public void run() {
			try {
				IndexWriter writer = new IndexWriter(FSDirectory.open(this.output), new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.LIMITED);
				
				int i = 0;
				for (File file: files) {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line;
					String[] split;
					Document doc;
					while ((line = reader.readLine()) != null) {
						split = line.split("\t");
						boolean add = true;
						
						if (this.dict != null) {
							add = false;
							for (String word : split[0].split(" ")) {
								if (this.dict.contains(word)) {
									add = true;
									break;
								}
							}
						}

						if (add) {
							doc = new Document();
							doc.add(new Field("gram", split[0], Field.Store.YES, Field.Index.ANALYZED));
							doc.add(new Field("freq", split[1], Field.Store.YES, Field.Index.NOT_ANALYZED));
						
							writer.addDocument(doc);
						}
					}
					i++;
					System.out.println(file.getName() + " is Ready. Only " + (files.size()-i) + " files left ...");
				}
				
				System.out.println("The index is optimized for you! This can take a moment...");
				writer.optimize();
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LockObtainFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	/**
	 * Constructor to create a indexer instance
	 * @param web1tFolder The folder with all extracted n-gram files
	 * @param outputPath The lucene index folder
	 */
	public LuceneIndexer(File web1tFolder, File outputPath) {
		this(web1tFolder, outputPath, 1);
	}
	
	/**
	 * Constructor to create a indexer instance
	 * @param web1tFolder The folder with all extracted n-gram files
	 * @param outputPath The lucene index folder
	 * @param indexes The number of indexes
	 */
	public LuceneIndexer(File web1tFolder, File outputPath, int indexes) {
		this.web1tFolder = web1tFolder;
		this.outputPath = outputPath;
		this.indexes = indexes;
	}
	
	/**
	 * Create the index. This is a very long running function.
	 * It will output some information on stdout.
	 * 
	 * @throws FileNotFoundException
	 * @throws InterruptedException 
	 */
	public void index() throws FileNotFoundException, InterruptedException {
		List<File> files;
		
		if (this.web1tFolder.isFile()) {
			files = Arrays.asList(new File[]{ this.web1tFolder });
		} else if (this.web1tFolder.isDirectory()) {
			files = Arrays.asList(this.web1tFolder.listFiles());
		} else {
			throw new FileNotFoundException();
		}
		
		if (this.indexes > files.size()) {
			this.indexes = files.size();
		}

		System.out.println("Oh, you started a long running task. Take a cup of coffee ...");
		
		int perIndex =  (int) Math.ceil((float) files.size() / (float) this.indexes);
		Worker[] workers = new Worker[this.indexes];
		for (int i = 0; i < this.indexes; i++) {
			int start = i*perIndex;
			int end = start+perIndex;
			if (end > files.size()) end = files.size();
			
			Worker w = new Worker(files.subList(start, end), new File(this.outputPath.getAbsoluteFile() + "/" + i), this.dictionary);
			w.start();
			workers[i] = w;
		}
		
		for (int i = 0; i < this.indexes; i++) {
			workers[i].join();
		}
		
		System.out.println("Great, index is ready. Have fun!");
	}
	
	public IDictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(IDictionary dictionary) {
		this.dictionary = dictionary;
	}

	/**
	 * Execute the indexer. Following parameter are allowed:
	 * 
	 *   * --web1t The folder with all extracted n-gram files
	 *   * --outputPath The lucene index folder
	 *   * --index (optional) Number of how many indexes should be created. Default: 1
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("web1t", true, "Folder with the web1t extracted documents");
		options.addOption("outputPath", true, "File, where the index should be created");
		options.addOption("index", true, "(optional) Number of how many indexes should be created. Default: 1");
		options.addOption("igerman98", false, "If this argument is set, only words of the german dictionary will be added to the index");
		
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			
			int i = Integer.valueOf(cmd.getOptionValue("index"));
			if (i <= 0) {
				i = 1;
			}
			
			LuceneIndexer indexer = new LuceneIndexer(
					new File(cmd.getOptionValue("web1t")),
					new File(cmd.getOptionValue("outputPath")), i);
			
			if (cmd.hasOption("igerman98")) {
				indexer.setDictionary(new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff")));
			}
			
			indexer.index();
		} catch (Exception e) {
			System.err.println( "Error: " + e.getMessage() );
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CouchDBIndexer", options);
		}
	}
}
