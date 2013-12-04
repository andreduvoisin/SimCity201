package restaurant.restaurant_jerryweb.interfaces;

import java.util.Map;

import restaurant.restaurant_jerryweb.JerrywebCookRole.Food;
import restaurant.restaurant_jerryweb.JerrywebCookRole.Order;
import restaurant.restaurant_jerryweb.JerrywebMarketRole;

public interface Cook {
	
	public abstract void msgOutOfStock(JerrywebMarketRole market, String choice);
	
	public abstract void msgHereIsOrder(String choice, Map<String, Food> fm);
	
	public abstract void msgCookThis(Waiter w, String choice, int table);
	
	public abstract void msgfoodDone(Order order);
	
	public abstract void msgGiveMeOrder(int t);
	
}
