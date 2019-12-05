package com.xush.despattern.Proxy;

import java.lang.reflect.Proxy;

public class BuyHouseTest {
	public static void main(String[] args) {
		BuyHouse buyHouse = new BuyHouseImpl();
		//静态代理
		//		BuyHouseProxy proxy = new BuyHouseProxy(buyHouse);
		//		proxy.buyHosue();
		//动态代理
		BuyHouse proxyBuyHouse = (BuyHouse) Proxy.newProxyInstance(BuyHouse.class.getClassLoader(),
				new Class[] { BuyHouse.class }, new DynamicProxyHandler(buyHouse));
		proxyBuyHouse.buyHosue();
	}
}
