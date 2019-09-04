package com.xush.map;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
   * 哈夫曼编码，及其解码
 * @author xush
 * @since  2019年7月23日
 */
public class HashDecode {

	public static void main(String[] args) {
		
		createTree("www21aa");
	}
	
	public static void createTree(String str) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i = 0, len =str.length(); i < len; i++) {
			String c = str.charAt(i) + "";
			if(map.containsKey(c)) {
				map.put(c, map.get(c)+1);
			}else {
				map.put(c, 1);
			}
		}
		ArrayList <TreeNode>list = new ArrayList<TreeNode>();
		for(String key : map.keySet()) {
			TreeNode node = new TreeNode(key, map.get(key));
			list.add(node);
		}
		list.sort(new Comparator<TreeNode>() {
				public int compare(TreeNode n1, TreeNode n2){
        	return  n2.weight - n1.weight;
        }
		});
		while(list.size()>1){
			mergerTree(list);
		}
		
		printHTree(list.get(0), "");
	}
	
	public static void printHTree(TreeNode node, String code) {
		if(node.left == null) {
			System.out.print( node.value + ":" + code + " ");
			return;
		}
		printHTree(node.left, code + "0");
		printHTree(node.right, code + "1");
	}
	
	public static void mergerTree(ArrayList <TreeNode>list) {
		if(list == null) {
			return;
		}
		int len = list.size();
		if(len <= 1) {
			return;
		}
		TreeNode node1 = list.get(len-1);
		TreeNode node2 = list.get(len-2);
		list.remove(len-1);
		list.remove(len-2);
		TreeNode new_node = new TreeNode(node1.weight + node2.weight);
		new_node.left = node2;
		new_node.right = node1;
		restSort(list, new_node);
	}
	
	public static void restSort(ArrayList <TreeNode>list, TreeNode node) {
		if(list.size() == 0) {
			list.add(node);
			return;
		}
		for(int i = list.size()-1; i >= 0; i--) {
			if(i == 0) {
				list.add(node);
				break;
			}
			if(list.get(i).weight >= node.weight) {
				list.add(i+1, node);
				break;
			}
		}
	}

	/**
	   * 哈夫曼树的节点 
	 * @author xush
	 * @since  2019年7月23日
	 */
	public static class TreeNode {
		public String value;
		public Integer weight;
		public TreeNode left, right;
		public TreeNode() {
			
		}
		public TreeNode(Integer weight) {
			super();
			this.weight = weight;
		}
		public TreeNode(String value , Integer weight) {
			super();
			this.value = value;
			this.weight = weight;
		}
	
	}
}

