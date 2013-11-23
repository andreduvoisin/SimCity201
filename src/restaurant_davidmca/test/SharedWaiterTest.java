package restaurant_davidmca.test;

import junit.framework.TestCase;
import restaurant_davidmca.Table;
import restaurant_davidmca.agents.WaiterAgentSharedData;
import restaurant_davidmca.test.mock.MockCook;
import restaurant_davidmca.test.mock.MockCustomer;

public class SharedWaiterTest extends TestCase {
	WaiterAgentSharedData waiter;
	MockCustomer customer;
	MockCustomer customer2;
	MockCustomer customer3;
	MockCook cook;

	public void setUp() throws Exception {
		super.setUp();
		waiter = new WaiterAgentSharedData("Shared Data Waiter");
		customer = new MockCustomer("c1");
		customer2 = new MockCustomer("c2");
		customer3 = new MockCustomer("c3");
		cook = new MockCook("Cook");
	}

	public void testSingleCustomer() {
		assertEquals("Check precondition: waiter has no customers",
				waiter.myCustomers.size(), 0);
		Table t = new Table(1, 1, 1, 1);
		waiter.msgSeatAtTable(customer, t, 0);
		waiter.pickAndExecuteAnAction();
		assertEquals("Check that waiter now has one customer",
				waiter.myCustomers.size(), 1);
		assertTrue(
				"Check that customer got msgFollowMe" + customer.log.toString(),
				customer.log.containsString("msgFollowMe"));
	}

	public void testMultipleCustomers() {

	}
}
