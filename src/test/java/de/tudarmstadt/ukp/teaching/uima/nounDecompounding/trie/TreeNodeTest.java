package de.tudarmstadt.ukp.teaching.uima.nounDecompounding.trie;

import junit.framework.Assert;

import org.junit.Test;

public class TreeNodeTest {

	@Test
	public void testLeaf() {
		TreeNode<String, Integer> root = new TreeNode<String, Integer>("root", 1);
		
		Assert.assertTrue(root.isLeaf());
		Assert.assertFalse(root.hasChildren());
	}
	
	@Test
	public void testAdd() {
		TreeNode<String, Integer> root = new TreeNode<String, Integer>("root", 1);
		TreeNode<String, Integer> child = new TreeNode<String, Integer>("child", 1);
		
		root.addChild(child);
		Assert.assertFalse(root.isLeaf());
		Assert.assertTrue(root.hasChildren());
		Assert.assertTrue(child.isLeaf());
		Assert.assertFalse(child.hasChildren());
		
		Assert.assertTrue(root.hasChild("child"));
		Assert.assertEquals(child, root.getChild("child"));
	}
}
