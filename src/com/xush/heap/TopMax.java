package com.xush.heap;
/**
 * 
 * @author xush
 * @since 2019
 */
public class TopMax {

	public static int[] getTopMax(int[] nums, int m) {
		Min_Heap heap = new Min_Heap(m);
		return heap.getTopN(nums, 5);
	}

}
