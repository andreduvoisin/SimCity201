package market.test.mock;

import java.util.Map;

import market.*;
import market.interfaces.MarketCook;
import test.mock.*;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;

public class MockCookCustomer extends Mock implements MarketCook, Role {

	public MockCookCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<String,Integer> cannotFulfill, MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson."));
	}
	
	public void msgHereIsCookOrder(MarketOrder o) {
		log.add(new LoggedEvent("Received msgHereIsCookOrder."));
	}

/*Role Actions*/
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	public void setPerson(Person person) {
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

	public Person getPerson() {
		return null;
	}
}
