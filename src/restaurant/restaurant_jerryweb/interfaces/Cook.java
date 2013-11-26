package restaurant.restaurant_jerryweb.interfaces;

import java.util.Map;

import restaurant.restaurant_jerryweb.MarketRole;
import restaurant.restaurant_jerryweb.WaiterRole;
import restaurant.restaurant_jerryweb.CookRole.Food;
import restaurant.restaurant_jerryweb.CookRole.Order;

public interface Cook {
	
	public abstract void msgOutOfStock(MarketRole market, String choice);
	
	public abstract void msgHereIsOrder(String choice, Map<String, Food> fm);
	
	public abstract void msgCookThis(Waiter w, String choice, int table);
	
	public abstract void msgfoodDone(Order order);
	
	public abstract void msgGiveMeOrder(int t);
	
}
