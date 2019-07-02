package com.xush.stream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StreamDemo {
	public static final String TXT_PATH = "D:/�鼮��Ϣ.txt";

	public static void main(String[] args) throws IOException {
		ArrayList<String> arrayList = new ArrayList<String>();
		FileReader fr = new FileReader(TXT_PATH);
		try {
			BufferedReader bf = new BufferedReader(fr);
			try {
				String str;
				// ���ж�ȡ�ַ���
				while ((str = bf.readLine()) != null) {
					arrayList.add(str);
				}
			} finally {
				bf.close();
			}
		} finally {
			fr.close();
		}

	}

}
