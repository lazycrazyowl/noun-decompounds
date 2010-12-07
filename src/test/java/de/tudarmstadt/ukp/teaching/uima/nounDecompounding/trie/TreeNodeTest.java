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

package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie;

import junit.framework.Assert;

import org.junit.Test;

public class TreeNodeTest {

	@Test
	public void testLeaf() {
		KeyValueNode<String, Integer> root = new KeyValueNode<String, Integer>("root", 1);
		
		Assert.assertTrue(root.isLeaf());
		Assert.assertFalse(root.hasChildren());
	}
	
	@Test
	public void testAdd() {
		KeyValueNode<String, Integer> root = new KeyValueNode<String, Integer>("root", 1);
		KeyValueNode<String, Integer> child = new KeyValueNode<String, Integer>("child", 1);
		
		root.addChild(child);
		Assert.assertFalse(root.isLeaf());
		Assert.assertTrue(root.hasChildren());
		Assert.assertTrue(child.isLeaf());
		Assert.assertFalse(child.hasChildren());
		
		Assert.assertTrue(root.hasChild("child"));
		Assert.assertEquals(child, root.getChild("child"));
	}
}
