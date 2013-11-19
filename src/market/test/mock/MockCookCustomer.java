package market.test.mock;

import test.mock.*;
import market.interfaces.*;
import market.other.*;
import java.util.Map;

public class MockCookCustomer extends Mock implements Cook {

	public MockCookCustomer(String name) {
		super(name);
	}
	
	public void msgInvoiceToPerson(Map<Item,Integer> cannotFulfill, Invoice invoice) {
		
	}
	
	public void msgHereIsCookOrder(Order o) {
		
	}
}
