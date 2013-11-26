package restaurant.restaurant_jerryweb.interfaces;

import java.util.List;
import java.util.Map;
import restaurant.restaurant_jerryweb.CookRole;
import restaurant.restaurant_jerryweb.MarketRole.Order;

public interface Market {

	public abstract void  msgGiveMeOrder(String choice, Map<String, CookRole.Food> foodMap);
	
	public abstract void msgPayment(double income, int BN);
	
	//public abstract List<Order> getRestockList();
	
	public abstract String getName();
}
