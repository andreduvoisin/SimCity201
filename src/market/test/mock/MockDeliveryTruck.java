package market.test.mock;

import test.mock.*;
import market.interfaces.*;
import market.other.*;

public class MockDeliveryTruck extends Mock implements DeliveryTruck {

	public MockDeliveryTruck(String name) {
		super(name);
	}
	
	public void msgDeliverOrderToCook(Order o) {
		
	}
}
