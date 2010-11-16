package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

	/**
	 * Constructor to create a indexer instance
	 * @param web1tFolder The folder with all extracted n-gram files
	 * @param outputPath The lucene index folder
	 */
	public LuceneIndexer(File web1tFolder, File outputPath) {
		this.web1tFolder = web1tFolder;
		this.outputPath = outputPath;
	}
	
	/**
	 * Create the index. This is a very long running function.
	 * It will output some information on stdout.
	 * 
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public void index() throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriter writer = new IndexWriter(FSDirectory.open(this.outputPath), new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.LIMITED);
		
		File[] files;
		
		if (this.web1tFolder.isFile()) {
			files = new File[]{ this.web1tFolder };
		} else if (this.web1tFolder.isDirectory()) {
			files = this.web1tFolder.listFiles();
		} else {
			throw new FileNotFoundException();
		}

		int i = 0;
		System.out.println("Oh, you started a long running task. Take a cup of coffee ...");
		for (File file: files) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String[] split;
			Document doc;
			while ((line = reader.readLine()) != null) {
				doc = new Document();
				split = line.split("\t");
				
				doc.add(new Field("gram", split[0], Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field("freq", split[1], Field.Store.YES, Field.Index.NOT_ANALYZED));
				
				writer.addDocument(doc);
			}
			i++;
			System.out.println(file.getName() + " is Ready. Only " + (files.length-i) + " files left ...");
		}
		
		System.out.println("The index is optimized for you! This can take a moment...");
		writer.optimize();
		writer.close();
		
		System.out.println("Great, index is ready. Have fun!");
	}

	/**
	 * Execute the indexer. Following parameter are allowed:
	 * 
	 *   * --web1t The folder with all extracted n-gram files
	 *   * --outputPath The lucene index folder
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("web1t", true, "Folder with the web1t extracted documents");
		options.addOption("outputPath", true, "File, where the index should be created");
		
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			
			LuceneIndexer indexer = new LuceneIndexer(
					new File(cmd.getOptionValue("web1t")),
					new File(cmd.getOptionValue("outputPath")));
			
			indexer.index();
		} catch (Exception e) {
			System.err.println( "Error: " + e.getMessage() );
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CouchDBIndexer", options);
		}
	}
}
