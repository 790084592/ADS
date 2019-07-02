package com.xush.linklist;

import org.json.JSONObject;

public class Node {
	public Node parent;
	public Node child;
	public JSONObject data;
	
	public Node(JSONObject data) {
		this.data = data;
	}
}
