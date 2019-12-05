package com.xush.despattern.Proxy;

public class BuyHouseProxy implements BuyHouse {

	private BuyHouse buyHouse;

	public BuyHouseProxy(final BuyHouse buyHouse) {
		this.buyHouse = buyHouse;
	}

	@Override
	public void buyHosue() {
		System.out.println("before buy");
		buyHouse.buyHosue();
		System.out.println("after buy");

	}

}
