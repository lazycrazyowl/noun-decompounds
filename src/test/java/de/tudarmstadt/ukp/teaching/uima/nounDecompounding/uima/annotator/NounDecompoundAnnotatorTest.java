package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.annotator;

import static org.uimafit.factory.TypeSystemDescriptionFactory.createTypeSystemDescription;

import java.io.File;

import junit.framework.Assert;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.testing.factory.TokenBuilder;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.Token;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.web1t.LuceneIndexer;

public class NounDecompoundAnnotatorTest {

	static File source = new File("src/test/resources/ranking/n-grams");
	static File index = new File("target/test/index");
	
	@BeforeClass
	public static void createIndex() throws Exception {
		index.mkdirs();
		
		LuceneIndexer indexer = new LuceneIndexer(source, index);
		indexer.index();
	}
	
	@Test
	public void testListFreq() throws Exception {
		AnalysisEngine ae = AnalysisEngineFactory.createPrimitive(
				NounDecompoundingListAnnotator.class, createTypeSystemDescription(),
				AbstractNounDecompoundAnnotator.PARAM_INDEX, index.getAbsolutePath(),
				AbstractNounDecompoundAnnotator.PARAM_TOKEN_CLASS, Token.class.getName());
		
		JCas cas = ae.newJCas();
		TokenBuilder<Token, Annotation> builder= new TokenBuilder<Token, Annotation>(Token.class, Annotation.class);
		builder.buildTokens(cas, "Aktionsplan im Aktionshaus");
		String[] splits = new String[]{"aktion(s)+plan", "im", "akt+ions+haus"};
		ae.process(cas);
		
		int i = 0;
		for (SplittedToken t: JCasUtil.iterate(cas, SplittedToken.class)) {
			String splitString = "";
			for (int j = 0; j < t.getSplits().size(); j++) {
				splitString += t.getSplits(j);
				if (j<t.getSplits().size()-1) splitString += "+";
			}
			Split actual = Split.createFromString(splitString);
			Split expected = Split.createFromString(splits[i]);
			
			Assert.assertEquals(expected, actual);
			i++;
		}
	}
	
	@Test
	public void testListProb() throws Exception {
		AnalysisEngine ae = AnalysisEngineFactory.createPrimitive(
				NounDecompoundingListAnnotator.class, createTypeSystemDescription(),
				AbstractNounDecompoundAnnotator.PARAM_INDEX, index.getAbsolutePath(),
				AbstractNounDecompoundAnnotator.PARAM_TOKEN_CLASS, Token.class.getName(),
				AbstractNounDecompoundAnnotator.PARAM_RANKER, NounDecompoundingListAnnotator.Ranker.PROBABILITY.toString());
		
		JCas cas = ae.newJCas();
		TokenBuilder<Token, Annotation> builder= new TokenBuilder<Token, Annotation>(Token.class, Annotation.class);
		builder.buildTokens(cas, "Aktionsplan im Aktionshaus");
		String[] splits = new String[]{"aktionsplan", "im", "akt+ions+haus"};
		ae.process(cas);
		
		int i = 0;
		for (SplittedToken t: JCasUtil.iterate(cas, SplittedToken.class)) {
			String splitString = "";
			for (int j = 0; j < t.getSplits().size(); j++) {
				splitString += t.getSplits(j);
				if (j<t.getSplits().size()-1) splitString += "+";
			}
			Split actual = Split.createFromString(splitString);
			Split expected = Split.createFromString(splits[i]);
			
			Assert.assertEquals(expected, actual);
			i++;
		}
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		// Delete index again
		for (File f : index.listFiles()) {
			for (File _f: f.listFiles()) {
				_f.delete();
			}
			f.delete();
		}
		
		index.delete();
	}
}
