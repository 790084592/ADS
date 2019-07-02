package com.xush.map;

import java.util.HashMap;
import java.util.Map;

public class MapDemo {
	public static void main(String []agrs) {
		show();
	}
	
	public static void show() {
		Map map = new HashMap<String, Integer>();
		//成对放入多个key-value对
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		map.put("d", 4);
		map.put("e", 5);
		//获取map集合的所有key组成的集合，通过遍历key来实现遍历所有的key-value对
		for(Object key : map.keySet()) {
			System.out.println(key + "--->"+map.get(key));
		}
		System.out.println(map);
	
	}

}
