package com.xush.main;

import java.util.ArrayList;
/**
 * 
 * @author xush
 * @since  2019��7��2��
 */
public class PrintStr {

	public static void main(String[] args) {
		print("(the cost(of (word)) ()gasolin(e)()(!)");
	}
	
	/**
	 * ��ӡƥ���()�м��ֵ
	 * ʵ��˼·����
	 * 1.ֻ�����һ���ַ���
	 * 2.Ϊ"(",�������ַ����е�λ��i�Ž�list�б���
	 * 3.Ϊ")",���ȡlist�����һ����ţ���Ϊ��������ƥ��ɹ�����ӡ�������֮����ַ�����Ϊ�����ӡ"<empty>"
	 * 4.Ϊ")",��list�����һ����ţ���Ϊ�����������ɺ�list��ǰ�ң�check++���ҵ���(��ʱcheck--��ֻ��check=0��list�д洢�ĸ����Ϊ����ʱ��ƥ��ɹ�����ӡ
	 * 5.ƥ��ɹ���׷�ݵ������ƥ��ʱ������")"���ַ����е�λ��j�ĸ����Ž�list�б���
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
