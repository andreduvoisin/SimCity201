package market.test.mock;

import java.util.Map;

import base.PersonAgent;
import base.interfaces.Role;
import market.*;
import market.interfaces.Customer;
import test.mock.*;

public class MockCustomer extends Mock implements Customer, Role {
	
	public MockCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<String, Integer> canFulfill, Invoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson"));
	}
		
	public void msgHereIsCustomerOrder(Order order) {
		log.add(new LoggedEvent("Received msgHereIsCustomerOrder."));
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
