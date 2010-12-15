package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitElement;

public class ExportCcorpusMorphemes {

	public static void main(String[] args) throws IOException {
		Set<String> morphemes = new HashSet<String>();
		CcorpusReader reader = new CcorpusReader(new File("src/main/resources/evaluation/ccorpus.txt"));
		Split split;
		
		while((split = reader.readSplit()) != null) {
			for (SplitElement s : split.getSplits()) {
				if (s.getMorpheme() != null) {
					morphemes.add(s.getMorpheme());
				}
			}
		}
		
		for (String s : morphemes) {
			System.out.println(s);
		}
	}
}
