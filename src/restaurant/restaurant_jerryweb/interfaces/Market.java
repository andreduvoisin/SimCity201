package restaurant.restaurant_jerryweb.interfaces;

import java.util.Map;

import restaurant.restaurant_jerryweb.JerrywebCookRole;

public interface Market {

	public abstract void  msgGiveMeOrder(String choice, Map<String, JerrywebCookRole.Food> foodMap);
	
	public abstract void msgPayment(double income, int BN);
	
	//public abstract List<Order> getRestockList();
	
	public abstract String getName();
}
