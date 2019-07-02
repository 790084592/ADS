package com.xush.heap;

/**
 * 最小堆
 * @author xush
 *
 */
public class Min_Heap {
	
	private int heap[];
	private int maxSize; ////最多可容纳数目
	private int n;///元素数目
	
	
	/** 
	 * @param num 堆的大小
	 */
	public Min_Heap(int num){
		n=0;
		maxSize=num;
		heap=new int[maxSize];
	}
	

	
	/**
	 * 元素入堆
	 * @param v
	 */
	public void append(int v){
		if(isFull()){
			System.out.println("heap is full ,can't append elements !");
			return ;
		}
		////元素插在末尾
		heap[n]=v;
		n++;
		///向上调整
		adjustUp(n-1);
	}
	
	
	/**
	 * 取出堆顶元素
	 * @return
	 */
	public int serve(){
		if(isEmpty()){
			System.out.println("heap is empty!");
			return Integer.MIN_VALUE;
		}
		
		int temp=heap[0];
		////用最后一个元素代替第一个元素
		heap[0]=heap[n-1];
		n--;
		//向下调整
		adjustDown(0, n-1);
		
		return temp;
	}	
	
	
	/**
	 * 求最大的n个数据
	 * @param data
	 * @param n
	 * @return null if n is bigger than the heap size, otherwise 
	 */
	public int[] getTopN(int []data,int n){
		heap=new int[n];
		maxSize=n;
		this.n=0;
	
		///构建初始堆
		for(int i=0;i<n;i++){
			append(data[i]);
		}
		
		for(int i=n;i<data.length;i++){
			//假设比堆顶元素大，则替换堆顶元素，并调整
			if(data[i]>heap[0]){
				heap[0]=data[i];
				adjustDown(0, n-1);
			}
		}
		
		return heap;
	}
	
	
	/**
	 * 对元素i进行向下调整，用于删除元素时调整
	 * @param i
	 * @param j
	 */
	private void adjustDown(int i, int j) {
		// TODO Auto-generated method stub
		int child=2*i+1;
		int temp=heap[i];///记录待调整的节点的值	
		while(child<j){
			////在范围内进行遍历调整
			
			if(heap[child]> heap[child+1]){
				///假设左孩子比右孩子大, 则指向较小的右孩子
				child++;
			}
			
			if(heap[child]>temp){
				///假设较小的孩子都比自己大。则退出
				break;
			}
			
			heap[(child-1)/2]=heap[child];
			child=child*2+1;
		}
		////循环结束。child指向终于位置的节点的左孩子
		heap[(child-1)/2]=temp;
		
	}
	
	
	
	
	/**
	 * 将i处的元素向上调整为堆，用于插入时候
	 * @param i
	 */
	private void adjustUp(int i){
		int temp=heap[i];
		while(i>0&&heap[(i-1)/2]> temp){
			///当父节点的值比temp大的时候，交换值
			heap[i]=heap[(i-1)/2];
			i=(i-1)/2;
		}
		heap[i]=temp;
	}
	
	
	/**
	 * 堆是否满了
	 * @return
	 */
	public boolean isFull(){
		return n>=maxSize;
	}
	
	/**
	 * 是否为空堆
	 * @return
	 */
	public boolean isEmpty(){
		return 0==n;
	}
}