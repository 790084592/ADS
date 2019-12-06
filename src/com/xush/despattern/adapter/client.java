package com.xush.despattern.adapter;

public class client {

	public static void main(String[] args) {
		SDCard sd = new SDCardImpl();
		Computer computer = new ComputerImpl();
		computer.readSD(sd);

		TFCard tf = new TFCardImpl();
		computer.readSD(new SdAdapterTF(tf));

	}

}
