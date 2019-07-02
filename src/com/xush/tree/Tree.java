package com.xush.tree;

import org.json.JSONObject;

public class Tree {
	private TreeNode root;
	
	public Tree() {
		root = new TreeNode(0);
		root.leftChild = new TreeNode(1);
		root.rightChild = new TreeNode(4);
		root.leftChild.leftChild = new TreeNode(2);
		root.leftChild.rightChild =  new TreeNode(3);
		root.rightChild.leftChild =  new TreeNode(5);
		root.rightChild.rightChild =  new TreeNode(6);
		perOrder(root);
		System.out.println();
		this.inOrder(root);
		System.out.println();
		this.afterOrder(root);
	}
	
	/**
	    *  先序遍历
	 * @param node
	 */
	public void perOrder(TreeNode node) {
		//递归出口
		if(node == null) {
			return;
		}
		//递归体
		System.out.print(" " + node.value + " " );
		perOrder(node.leftChild);
		perOrder(node.rightChild);
	}
	
	/**
	    *  中序遍历
	 * @param node
	 */
	public void inOrder(TreeNode node) {
		//递归出口
		if(node == null) {
			return;
		}
		//递归体
		inOrder(node.leftChild);
		System.out.print(" " + node.value + " " );
		inOrder(node.rightChild);
	}

	/**
	    *  后序遍历
	 * @param node
	 */
	public void afterOrder(TreeNode node) {
		//递归出口
		if(node == null) {
			return;
		}
		//递归体
		afterOrder(node.leftChild);
		afterOrder(node.rightChild);
		System.out.print(" " + node.value + " " );
	}
	
}
