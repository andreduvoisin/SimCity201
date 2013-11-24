package restaurant.restaurant_davidmca.test;

import junit.framework.TestCase;
import restaurant.restaurant_davidmca.roles.CookRole;
import restaurant.restaurant_davidmca.test.mock.MockCustomer;
import restaurant.restaurant_davidmca.test.mock.MockWaiter;

public class CookTest extends TestCase {
	MockWaiter waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockCustomer customer3;
	CookRole cook;

	public void setUp() throws Exception {
		super.setUp();
		waiter = new MockWaiter("Shared Data Waiter");
		customer1 = new MockCustomer("c1");
		customer2= new MockCustomer("c2");
		customer3 = new MockCustomer("c3");
		cook = new CookRole("Cook", 5);
	}

	public void testSingleCustomer() {
		
	}

	public void testMultipleCustomers() {
		
	}
}
