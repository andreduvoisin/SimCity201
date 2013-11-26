package restaurant.restaurant_duvoisin.interfaces;

import java.util.List;

import restaurant.restaurant_duvoisin.CookAgent.Order;

public interface Cook {
	public abstract void msgHereIsOrder(Waiter w, String choice, int table);
	public abstract void msgFailedToFulfillRequest(Market ma, String item, int amount);
	public abstract void msgReplenishFood(String item, int amount);
	public abstract void msgGotFood(int position);
	public abstract List<Order> getRevolvingStand();
}