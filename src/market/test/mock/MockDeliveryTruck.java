package market.test.mock;

import market.Order;
import market.interfaces.DeliveryTruck;
import test.mock.Mock;

public class MockDeliveryTruck extends Mock implements DeliveryTruck {

	public MockDeliveryTruck(String name) {
		super();
	}
	
	public void msgDeliverOrderToCook(Order o) {
		
	}
}
