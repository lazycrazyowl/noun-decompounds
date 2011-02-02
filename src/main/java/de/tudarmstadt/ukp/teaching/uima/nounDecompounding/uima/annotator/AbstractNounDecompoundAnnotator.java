package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.annotator;

import java.io.File;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IDictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.IGerman98Dictionary;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.ISplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.LeftToRightSplitAlgorithm;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.uima.type.SplittedToken;

/**
 * Abstract Noun Decompound Annotator
 * 
 * Makes the most of the work. But ranking is
 * done by subclasses
 * 
 * @author Jens Haase <je.haase@googlemail.com>
 */
public abstract class AbstractNounDecompoundAnnotator extends
		JCasAnnotator_ImplBase {

	public static final String PARAM_INDEX = "index";
	@ConfigurationParameter(name=PARAM_INDEX, mandatory=true, description="The path to the lucene index")
	private String indexPath;
	
	public static final String PARAM_RANKER = "ranker";
	@ConfigurationParameter(name=PARAM_RANKER, mandatory=false, defaultValue="FREQUENCY", description="The used ranker implementaion. See AbstractNounDecompoundAnnotator.Ranker for possible values")
	private String rankerName;
	
	public static final String PARAM_TOKEN_CLASS = "tokenClassName";
	@ConfigurationParameter(name=PARAM_TOKEN_CLASS, mandatory=true, description="Classname of the use token annotation. (e.g. path.to.package.Token)")
	private String tokenType;
	
	private ISplitAlgorithm splitter;
	
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		// TODO Auto-generated method stub
		super.initialize(context);
		
		IDictionary dict = new IGerman98Dictionary(new File("src/main/resources/de_DE.dic"), new File("src/main/resources/de_DE.aff"));
		LinkingMorphemes morphemes = new LinkingMorphemes(new File("src/main/resources/linkingMorphemes.txt"));
		this.splitter = new LeftToRightSplitAlgorithm(dict, morphemes);
		
		this.initializeRanker();
	}
	
	abstract protected void initializeRanker() throws ResourceInitializationException;

	@SuppressWarnings("unchecked")
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		try {
			Class<Annotation> tokenClass = (Class<Annotation>) Class.forName(this.tokenType);
			
			for (Annotation token: JCasUtil.iterate(aJCas, tokenClass)) {
				String word = token.getCoveredText();
				
				Split s = getHighestRank(word);
				
				SplittedToken type = new SplittedToken(aJCas);
				type.setBegin(token.getBegin());
				type.setEnd(token.getEnd());
				
				StringArray splits = new StringArray(aJCas, s.getSplits().size());
				for (int i = 0; i < s.getSplits().size(); i++) {
					splits.set(i, s.getSplits().get(i).toString());
				}
				type.setSplits(splits);
				type.addToIndexes();
			}
		} catch (ClassNotFoundException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	abstract protected Split getHighestRank(String word);

	public String getIndexPath() {
		return indexPath;
	}

	public String getRankerName() {
		return rankerName;
	}

	public String getTokenType() {
		return tokenType;
	}

	public ISplitAlgorithm getSplitter() {
		return splitter;
	}
}