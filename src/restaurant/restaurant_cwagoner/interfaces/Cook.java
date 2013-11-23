package restaurant.restaurant_cwagoner.interfaces;

import java.util.*;

import restaurant.restaurant_cwagoner.CookAgent.Order;

public interface Cook {
	public List<Order> Orders = new ArrayList<Order>();
	
	public abstract void msgHeresAnOrder(Waiter w, int tableNum, String food);
	
	public abstract void msgOutOfFood(Market m);
	
	public abstract void msgCantFulfillOrder(Market m);
	
	public abstract void msgOrderFulfilled(HashMap<String, Integer> fulfillList);

	public abstract void msgDontOrderFrom(Market m);
}