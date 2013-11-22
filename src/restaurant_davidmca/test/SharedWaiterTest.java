package restaurant_davidmca.test;

import junit.framework.TestCase;
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
		customer2= new MockCustomer("c2");
		customer3 = new MockCustomer("c3");
		cook = new MockCook("Cook");
	}
	
	public void testSingleCustomer() {
		
	}
	
	public void testMultipleCustomers() {
		
	}
}
