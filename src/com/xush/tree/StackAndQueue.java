package com.xush.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class StackAndQueue {
	public static void calc() {
		
		//ջ���Ƚ����
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		for(Integer s : stack){
            System.out.println(s);
        }
		System.out.println(stack.peek());
		System.out.println(stack.peek());
		System.out.println(stack.peek());
		System.out.println(stack.peek());
		
		//���У��Ƚ��ȳ�
		Queue<String> queue = new LinkedList<String>();
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");
        for(String q : queue){
            System.out.println(q);
        }
        queue.poll();
	}
}
