package market.test.mock;

import java.util.Map;

import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;
import base.Item.EnumMarketItemType;
import market.*;
import market.interfaces.MarketCustomer;
import test.mock.*;

public class MockCustomer extends Mock implements MarketCustomer, Role {
	
	public MockCustomer() {
		super();
	}
	
	public void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> cannotFulfill, MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgInvoiceToPerson"));
	}
		
	public void msgHereIsCustomerOrder(MarketOrder order) {
		log.add(new LoggedEvent("Received msgHereIsCustomerOrder."));
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
