package com.xush.map;

import java.util.HashMap;
import java.util.Map;

public class MapDemo {
	public static void main(String []agrs) {
		show();
	}
	
	public static void show() {
		Map map = new HashMap<String, Integer>();
		//�ɶԷ�����key-value��
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		map.put("d", 4);
		map.put("e", 5);
		//��ȡmap���ϵ�����key��ɵļ��ϣ�ͨ������key��ʵ�ֱ������е�key-value��
		for(Object key : map.keySet()) {
			System.out.println(key + "--->"+map.get(key));
		}
		System.out.println(map);
	
	}

}
