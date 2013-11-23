package restaurant_davidmca;

import restaurant_davidmca.agents.HostAgent.WaiterState;
import restaurant_davidmca.interfaces.Waiter;

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
