package restaurant_davidmca;

import restaurant_davidmca.CookAgent.OrderState;
import restaurant_davidmca.interfaces.Waiter;

public class Order {
	Waiter waiter;
	String choice;
	int table;
	OrderState status = OrderState.Received;

	public Order(Waiter w, String c, Table t) {
		waiter = w;
		choice = c;
		table = t.tableNumber;
	}
}
