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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.splitter;

import junit.framework.Assert;

import org.junit.Test;

public class SplitTest {
	
	@Test
	public void testCreate() {
		Split s = Split.createFromString("aktion(s)+plan");
		
		Assert.assertEquals("aktion", s.getSplits().get(0).getWord());
		Assert.assertEquals("s", s.getSplits().get(0).getMorpheme());
		
		Assert.assertEquals("plan", s.getSplits().get(1).getWord());
		Assert.assertEquals(null, s.getSplits().get(1).getMorpheme());
	}
	
	@Test
	public void testToString() {
		SplitElement e1 = new SplitElement();
		e1.setWord("aktion");
		e1.setMorpheme("s");
		
		SplitElement e2 = new SplitElement();
		e2.setWord("plan");
		
		Split s = new Split();
		s.appendSplitElement(e1);
		s.appendSplitElement(e2);
		
		Assert.assertEquals("aktion(s)+plan", s.toString());
	}
	
	@Test
	public void testEquals() {
		SplitElement e1 = new SplitElement();
		e1.setWord("aktion");
		e1.setMorpheme("s");
		
		SplitElement e2 = new SplitElement();
		e2.setWord("plan");
		
		Split s1 = new Split();
		s1.appendSplitElement(e1);
		s1.appendSplitElement(e2);
		
		SplitElement e3 = new SplitElement();
		e3.setWord("aktion");
		e3.setMorpheme("s");
		
		SplitElement e4 = new SplitElement();
		e4.setWord("plan");
		
		Split s2 = new Split();
		s2.appendSplitElement(e3);
		s2.appendSplitElement(e4);
		
		Assert.assertTrue(s1.equals(s2));
		
		e2.setMorpheme("e");
		Assert.assertFalse(s1.equals(s2));
	}
}
