package com.xush.heap;

/**
 * ��С��
 * @author xush
 *
 */
public class Min_Heap {
	
	private int heap[];
	private int maxSize; ////����������Ŀ
	private int n;///Ԫ����Ŀ
	
	
	/** 
	 * @param num �ѵĴ�С
	 */
	public Min_Heap(int num){
		n=0;
		maxSize=num;
		heap=new int[maxSize];
	}
	

	
	/**
	 * Ԫ�����
	 * @param v
	 */
	public void append(int v){
		if(isFull()){
			System.out.println("heap is full ,can't append elements !");
			return ;
		}
		////Ԫ�ز���ĩβ
		heap[n]=v;
		n++;
		///���ϵ���
		adjustUp(n-1);
	}
	
	
	/**
	 * ȡ���Ѷ�Ԫ��
	 * @return
	 */
	public int serve(){
		if(isEmpty()){
			System.out.println("heap is empty!");
			return Integer.MIN_VALUE;
		}
		
		int temp=heap[0];
		////�����һ��Ԫ�ش����һ��Ԫ��
		heap[0]=heap[n-1];
		n--;
		//���µ���
		adjustDown(0, n-1);
		
		return temp;
	}	
	
	
	/**
	 * ������n������
	 * @param data
	 * @param n
	 * @return null if n is bigger than the heap size, otherwise 
	 */
	public int[] getTopN(int []data,int n){
		heap=new int[n];
		maxSize=n;
		this.n=0;
	
		///������ʼ��
		for(int i=0;i<n;i++){
			append(data[i]);
		}
		
		for(int i=n;i<data.length;i++){
			//����ȶѶ�Ԫ�ش����滻�Ѷ�Ԫ�أ�������
			if(data[i]>heap[0]){
				heap[0]=data[i];
				adjustDown(0, n-1);
			}
		}
		
		return heap;
	}
	
	
	/**
	 * ��Ԫ��i�������µ���������ɾ��Ԫ��ʱ����
	 * @param i
	 * @param j
	 */
	private void adjustDown(int i, int j) {
		// TODO Auto-generated method stub
		int child=2*i+1;
		int temp=heap[i];///��¼�������Ľڵ��ֵ	
		while(child<j){
			////�ڷ�Χ�ڽ��б�������
			
			if(heap[child]> heap[child+1]){
				///�������ӱ��Һ��Ӵ�, ��ָ���С���Һ���
				child++;
			}
			
			if(heap[child]>temp){
				///�����С�ĺ��Ӷ����Լ������˳�
				break;
			}
			
			heap[(child-1)/2]=heap[child];
			child=child*2+1;
		}
		////ѭ��������childָ������λ�õĽڵ������
		heap[(child-1)/2]=temp;
		
	}
	
	
	
	
	/**
	 * ��i����Ԫ�����ϵ���Ϊ�ѣ����ڲ���ʱ��
	 * @param i
	 */
	private void adjustUp(int i){
		int temp=heap[i];
		while(i>0&&heap[(i-1)/2]> temp){
			///�����ڵ��ֵ��temp���ʱ�򣬽���ֵ
			heap[i]=heap[(i-1)/2];
			i=(i-1)/2;
		}
		heap[i]=temp;
	}
	
	
	/**
	 * ���Ƿ�����
	 * @return
	 */
	public boolean isFull(){
		return n>=maxSize;
	}
	
	/**
	 * �Ƿ�Ϊ�ն�
	 * @return
	 */
	public boolean isEmpty(){
		return 0==n;
	}
}