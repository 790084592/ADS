package com.xush.despattern.Proxy;

import java.lang.reflect.Proxy;

public class BuyHouseTest {
	public static void main(String[] args) {
		BuyHouse buyHouse = new BuyHouseImpl();
		//��̬����
		//		BuyHouseProxy proxy = new BuyHouseProxy(buyHouse);
		//		proxy.buyHosue();
		//��̬����
		BuyHouse proxyBuyHouse = (BuyHouse) Proxy.newProxyInstance(BuyHouse.class.getClassLoader(),
				new Class[] { BuyHouse.class }, new DynamicProxyHandler(buyHouse));
		proxyBuyHouse.buyHosue();
	}
}
