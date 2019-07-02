package com.xush.main;

import java.util.ArrayList;
/**
 * 
 * @author xush
 * @since  2019年7月2日
 */
public class PrintStr {

	public static void main(String[] args) {
		print("(the cost(of (word)) ()gasolin(e)()(!)");
	}
	
	/**
	 * 打印匹配的()中间的值
	 * 实现思路如下
	 * 1.只需遍历一遍字符串
	 * 2.为"(",则将其在字符串中的位置i放进list中保存
	 * 3.为")",则获取list中最后一个序号，其为正数，则匹配成功，打印两个序号之间的字符串，为空则打印"<empty>"
	 * 4.为")",且list中最后一个序号，不为正数，继续由后list往前找，check++，找到“(”时check--，只当check=0且list中存储的该序号为正数时，匹配成功并打印
	 * 5.匹配成功或到追溯到最后无匹配时，将该")"在字符串中的位置j的负数放进list中保存
	 * @param str
	 */
	public static void print(String str) {
		if(str == null) {
			return;
		}
		int len = str.length();
		if(str.length() <= 1) {
			return;
		}
		
		ArrayList <Integer>list = new ArrayList<Integer>();
		if("(".compareTo(str.charAt(0) + "")==0){
			list.add(0);
		}
		for(int i = 1; i < len; i++) {
			char c = str.charAt(i);
			if("(".compareTo(c + "")==0){
				list.add(i);
			}else if(")".compareTo(c + "")==0) {
				int index = getLeftIndex(list);
				if(index != -1) {
					String result = str.substring(index+1, i);
					if(result.compareTo("")==0) {
						result = "<empty>";
					}
					System.out.println(result);
				}
				list.add(-i);
			}
		}
	}
	
	public static int getLeftIndex(ArrayList <Integer>list) {
		int check = 0, index = -1;
		for(int i = list.size() - 1; i >= 0; i--) {
			index =  list.get(i);
			if(index >= 0) {
				if(check == 0) {
					return index;
				}else {
					check--;
				}
			}else {
				check++;
			}
		}
		return -1;
	}

}
