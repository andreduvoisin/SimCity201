package market.test.mock;

import market.Order;
import market.interfaces.Worker;
import test.mock.Mock;

public class MockWorker extends Mock implements Worker {

	public MockWorker(String name) {
		super();
	}
	
	public void msgFulfillOrder(Order o) {
		
	}
	
	public void msgOrderFulfilled(Order o) {
		
	}
}
