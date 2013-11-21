package market.test.mock;

import java.util.Map;

import market.*;
import market.interfaces.Cook;
import test.mock.*;
import base.Item.EnumMarketItemType;
import base.PersonAgent;
import base.interfaces.Role;

public class MockCookCustomer extends Mock implements Cook, Role {

	public MockCookCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<EnumMarketItemType,Integer> cannotFulfill, Invoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson."));
	}
	
	public void msgHereIsCookOrder(Order o) {
		log.add(new LoggedEvent("Received msgHereIsCookOrder."));
	}

/*Role Actions*/
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	public void setPerson(PersonAgent person) {
	}

	public PersonAgent getPersonAgent() {
		return null;
	}

	public boolean isActive() {
		return false;
	}

	public int getSSN() {
		return 0;
	}
}
