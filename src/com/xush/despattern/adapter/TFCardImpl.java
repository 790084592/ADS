package com.xush.despattern.adapter;

public class TFCardImpl implements TFCard{

	@Override
	public void write() {
		System.out.println("---read-TFCard---");
	}

	@Override
	public void read() {
		System.out.println("---write-TFCard---");
	}

}
