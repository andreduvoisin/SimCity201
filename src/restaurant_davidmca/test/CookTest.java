package restaurant_davidmca.test;

import junit.framework.TestCase;
import restaurant_davidmca.agents.CookAgent;
import restaurant_davidmca.test.mock.MockCustomer;
import restaurant_davidmca.test.mock.MockWaiter;

public class CookTest extends TestCase {
	MockWaiter waiter;
	MockCustomer customer;
	MockCustomer customer2;
	MockCustomer customer3;
	CookAgent cook;

	public void setUp() throws Exception {
		super.setUp();
		waiter = new MockWaiter("Shared Data Waiter");
		customer = new MockCustomer("c1");
		customer2= new MockCustomer("c2");
		customer3 = new MockCustomer("c3");
		cook = new CookAgent("Cook", 5);
	}

	public void testSingleCustomer() {

	}

	public void testMultipleCustomers() {

	}
}
