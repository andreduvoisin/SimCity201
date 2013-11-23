package market.test.mock;

import market.MarketOrder;
import market.interfaces.MarketDeliveryTruck;
import test.mock.LoggedEvent;
import test.mock.Mock;

public class MockDeliveryTruck extends Mock implements MarketDeliveryTruck {

	public MockDeliveryTruck() {
		super();
	}
	
	public void msgDeliverOrderToCook(MarketOrder o) {
		log.add(new LoggedEvent("Received msgDeliverOrderToCook."));
	}
}
