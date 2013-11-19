package market.test.mock;

import java.util.Map;

import market.Invoice;
import market.Order;
import market.interfaces.Cook;
import test.mock.Mock;
import base.Item;
import base.Item.EnumMarketItemType;

public class MockCookCustomer extends Mock implements Cook {

	public MockCookCustomer(String name) {
		super(name);
	}
	
	public void msgInvoiceToPerson(Map<EnumMarketItemType,Integer> cannotFulfill, Invoice invoice) {
		
	}
	
	public void msgHereIsCookOrder(Order o) {
		
	}
}
