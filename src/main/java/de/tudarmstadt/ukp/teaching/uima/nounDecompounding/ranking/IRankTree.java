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

import java.util.List;

import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.Split;
import de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter.SplitTree;

/**
 * Interface to rank a split tree
 * @author Jens Haase <je.haase@googlemail.com>
 */
public interface IRankTree {

	/**
	 * Ranks a split tree and returns the highest ranked split
	 * 
	 * @param tree
	 * @return
	 */
	public Split highestRank(SplitTree tree);
	
	/**
	 * Ranks a split tree and returns a ordered list
	 * @param tree
	 * @return
	 */
	public List<Split> rank(SplitTree tree);
}
