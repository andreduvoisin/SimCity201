package restaurant.restaurant_cwagoner.interfaces;

import java.util.*;

import restaurant.restaurant_cwagoner.roles.CwagonerCookRole.Order;

public interface CwagonerCook {
	public List<Order> Orders = new ArrayList<Order>();
	
	public abstract void msgHeresAnOrder(CwagonerWaiter w, int tableNum, String food);
	
	public abstract void msgOutOfFood(CwagonerMarket m);
	
	public abstract void msgCantFulfillOrder(CwagonerMarket m);
	
	public abstract void msgOrderFulfilled(HashMap<String, Integer> fulfillList);

	public abstract void msgDontOrderFrom(CwagonerMarket m);
}