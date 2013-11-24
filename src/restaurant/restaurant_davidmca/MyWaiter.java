package restaurant.restaurant_davidmca;

import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant.restaurant_davidmca.roles.HostRole.WaiterState;

public class MyWaiter {
	public Waiter w;
	public int numCustomers;
	public WaiterState state;

	public MyWaiter(Waiter waiter) {
		this.w = waiter;
		numCustomers = 0;
		state = WaiterState.Normal;
	}
};
