package restaurant.restaurant_davidmca;

import restaurant.restaurant_davidmca.agents.CookAgent.OrderState;
import restaurant.restaurant_davidmca.interfaces.Waiter;

public class Order {
	public Waiter waiter;
	public String choice;
	public int table;
	public OrderState status = OrderState.Received;

	public Order(Waiter w, String c, Table t) {
		waiter = w;
		choice = c;
		table = t.tableNumber;
	}
}
