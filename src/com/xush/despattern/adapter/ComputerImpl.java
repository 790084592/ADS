package com.xush.despattern.adapter;

public class ComputerImpl implements Computer {

	@Override
	public void readSD(SDCard sd) {
		sd.read();
	}
}
