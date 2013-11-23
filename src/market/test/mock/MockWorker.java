package market.test.mock;

import market.Order;
import market.interfaces.Worker;
import test.mock.*;

public class MockWorker extends Mock implements Worker {

	public MockWorker() {
		super();
	}
	
	public void msgFulfillOrder(Order o) {
		log.add(new LoggedEvent("Received msgFulfillOrder for " + o.mPersonRole));
	}
	
	public void msgOrderFulfilled(Order o) {
		log.add(new LoggedEvent("Received msgOrderFulfilled."));
	}
}
