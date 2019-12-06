package com.xush.despattern.adapter;

public class SdAdapterTF implements SDCard{
	private TFCard tf;
	
	public SdAdapterTF(TFCard tf) {
		this.tf= tf;
	}

	@Override
	public void read() {
		tf.read();
	}

	@Override
	public void write() {
		tf.write();
	}

}
