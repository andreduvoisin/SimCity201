package market.test.mock;

import market.Order;
import market.interfaces.DeliveryTruck;
import test.mock.LoggedEvent;
import test.mock.Mock;

public class MockDeliveryTruck extends Mock implements DeliveryTruck {

	public MockDeliveryTruck() {
		super();
	}
	
	public void msgDeliverOrderToCook(Order o) {
		log.add(new LoggedEvent("Received msgDeliverOrderToCook."));
	}
}
