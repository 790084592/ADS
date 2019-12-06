package com.xush.despattern.adapter;

public class SDCardImpl implements SDCard{

	@Override
	public void read() {
		System.out.print("---reading-sd--");	
	}

	@Override
	public void write() {
		System.out.print("---writting-sd--");	
	}

}
