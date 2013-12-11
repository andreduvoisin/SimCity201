package restaurant.restaurant_cwagoner.test.mock;

import restaurant.restaurant_cwagoner.interfaces.*;

public class MockHost extends Mock implements CwagonerHost {
	public EventLog log = new EventLog();
	public MockHost(String name) {
		super(name);
	}
	@Override
	public void msgIWantFood(CwagonerCustomer cust) {
		log.add(new LoggedEvent("Received msgIWantFood"));
	}

	@Override
	public void msgCustomerGoneTableEmpty(CwagonerCustomer c, int tableNum) {
		log.add(new LoggedEvent("Received msgCustomerGoneTableEmpty"));
	}

}
