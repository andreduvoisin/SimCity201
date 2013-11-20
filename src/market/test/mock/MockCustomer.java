package market.test.mock;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.*;
import market.interfaces.Customer;
import test.mock.*;

public class MockCustomer extends Mock implements Customer {
	
	public MockCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> canFulfill, Invoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson"));
	}
		
	public void msgHereIsCustomerOrder(Order order) {
		log.add(new LoggedEvent("Received msgHereIsCustomerOrder."));
	}

}
