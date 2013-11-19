package market.test.mock;

import test.mock.*;
import market.interfaces.*;
import market.other.*;

public class MockWorker extends Mock implements Worker {

	public MockWorker(String name) {
		super(name);
	}
	
	public void msgFulfillOrder(Order o) {
		
	}
	
	public void msgOrderFulfilled(Order o) {
		
	}
}
