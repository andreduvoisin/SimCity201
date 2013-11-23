package market.test.mock;

import market.MarketOrder;
import market.interfaces.MarketWorker;
import test.mock.*;

public class MockWorker extends Mock implements MarketWorker {

	public MockWorker() {
		super();
	}
	
	public void msgFulfillOrder(MarketOrder o) {
		log.add(new LoggedEvent("Received msgFulfillOrder for " + o.mPersonRole));
	}
	
	public void msgOrderFulfilled(MarketOrder o) {
		log.add(new LoggedEvent("Received msgOrderFulfilled."));
	}
}
