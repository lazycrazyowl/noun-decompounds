package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class CouchDbExport {
	
	private CouchDbInstance dbInstance;
	private CcorpusReader reader;
	private CouchDbConnector db;

	public CouchDbExport(CcorpusReader aCcorpusReader, String dbName, boolean clearDatabase) {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		this.dbInstance = new StdCouchDbInstance(httpClient);
		this.reader = aCcorpusReader;
		
		this.db = new StdCouchDbConnector(dbName, this.dbInstance);
		if (clearDatabase && this.dbInstance.getAllDatabases().contains(dbName)) this.dbInstance.deleteDatabase(dbName);
		db.createDatabaseIfNotExists();
	}
	
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
	
	private Object createDoc(Split correct, SplitTree tree) {
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("word", tree.getRoot().getValue().getWord());
		doc.put("correct", correct.toString());
		doc.put("tree", this.createNode(tree.getRoot()));
		
		return doc;
	}

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
	
	public static void main(String[] args) {
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		LeftToRightSplitAlgorithm algo = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		try {
			CouchDbExport exporter = new CouchDbExport(new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt")), "noun_decompounding", true);
			exporter.export(algo, 20000);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}