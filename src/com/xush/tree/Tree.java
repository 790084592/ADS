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
	    *  �������
	 * @param node
	 */
	public void perOrder(TreeNode node) {
		//�ݹ����
		if(node == null) {
			return;
		}
		//�ݹ���
		System.out.print(" " + node.value + " " );
		perOrder(node.leftChild);
		perOrder(node.rightChild);
	}
	
	/**
	    *  �������
	 * @param node
	 */
	public void inOrder(TreeNode node) {
		//�ݹ����
		if(node == null) {
			return;
		}
		//�ݹ���
		inOrder(node.leftChild);
		System.out.print(" " + node.value + " " );
		inOrder(node.rightChild);
	}

	/**
	    *  �������
	 * @param node
	 */
	public void afterOrder(TreeNode node) {
		//�ݹ����
		if(node == null) {
			return;
		}
		//�ݹ���
		afterOrder(node.leftChild);
		afterOrder(node.rightChild);
		System.out.print(" " + node.value + " " );
	}
	
}
