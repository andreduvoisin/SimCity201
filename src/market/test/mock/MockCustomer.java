package market.test.mock;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.*;
import market.interfaces.Customer;
import test.mock.*;

public class MockCustomer extends Mock implements Customer {
	
	public MockCustomer() {
		
	}
	
	public void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> canFulfill, Invoice invoice) {
		
	}
	
	public boolean pickAndExecuteAnAction() {
		return true;
	}
}
