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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.dictionary;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class IGerman98DictionaryTest {

	@Test
	public void testContains() {
		IGerman98Dictionary dict = new IGerman98Dictionary(new File("src/test/resources/dic/igerman98.dic"), new File("src/test/resources/dic/igerman98.aff"));
		Assert.assertEquals(6, dict.getWords().size());
		
		Assert.assertTrue(dict.contains("hello"));
		Assert.assertTrue(dict.contains("try"));
		Assert.assertTrue(dict.contains("tried"));
		Assert.assertTrue(dict.contains("work"));
		Assert.assertTrue(dict.contains("worked"));
		Assert.assertTrue(dict.contains("rework"));
	}
}
