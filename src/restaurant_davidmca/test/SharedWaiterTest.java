package restaurant_davidmca.test;

import junit.framework.TestCase;
import restaurant_davidmca.Table;
import restaurant_davidmca.agents.WaiterAgentSharedData;
import restaurant_davidmca.test.mock.MockCook;
import restaurant_davidmca.test.mock.MockCustomer;

public class SharedWaiterTest extends TestCase {
	WaiterAgentSharedData waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockCustomer customer3;
	MockCook cook;

	public void setUp() throws Exception {
		super.setUp();
		waiter = new WaiterAgentSharedData("Shared Data Waiter");
		customer1 = new MockCustomer("c1");
		customer2 = new MockCustomer("c2");
		customer3 = new MockCustomer("c3");
		cook = new MockCook("Cook");
	}

	public void testSingleCustomer() {
		// Create tons of permits so that animation isn't a problem
		waiter.setCook(cook);
		waiter.isAnimating.release(500);
		assertEquals("Check precondition: waiter has no customers",
				waiter.myCustomers.size(), 0);
		Table t = new Table(1, 1, 1, 1);
		waiter.msgSeatAtTable(customer1, t, 0);
		assertEquals("Check that waiter now has one customer",
				waiter.myCustomers.size(), 1);
		waiter.pickAndExecuteAnAction();
		assertTrue(
				"Check that customer got msgFollowMe"
						+ customer1.log.toString(),
				customer1.log.containsString("msgFollowMe"));
		waiter.msgReadyToOrder(customer1);
		waiter.pickAndExecuteAnAction();
		assertTrue("Check that customer 1 got msgWhatWouldYouLike"
				+ customer1.log.toString(),
				customer1.log.containsString("msgWhatWouldYouLike"));
		waiter.msgHereIsMyOrder(customer1, "Steak");
		waiter.pickAndExecuteAnAction();
		assertEquals(
				"Check that there is now one order on the revolving stand",
				cook.getRevolvingStand().size(), 1);
	}

	public void testMultipleCustomers() {
		// Create tons of permits so that animation isn't a problem
		waiter.isAnimating.release(500);
		waiter.setCook(cook);
		assertEquals("Check precondition: waiter has no customers",
				waiter.myCustomers.size(), 0);
		Table t1 = new Table(1, 1, 1, 1);
		Table t2 = new Table(2, 1, 1, 1);
		Table t3 = new Table(3, 1, 1, 1);
		waiter.msgSeatAtTable(customer1, t1, 0);
		waiter.msgSeatAtTable(customer2, t2, 0);
		waiter.msgSeatAtTable(customer3, t3, 0);
		assertEquals("Check that waiter now has three customers",
				waiter.myCustomers.size(), 3);
		waiter.pickAndExecuteAnAction();
		assertTrue(
				"Check that customer1 got msgFollowMe"
						+ customer1.log.toString(),
				customer1.log.containsString("msgFollowMe"));
		waiter.pickAndExecuteAnAction();
		assertTrue(
				"Check that customer2 got msgFollowMe"
						+ customer2.log.toString(),
				customer2.log.containsString("msgFollowMe"));
		waiter.pickAndExecuteAnAction();
		assertTrue(
				"Check that customer3 got msgFollowMe"
						+ customer3.log.toString(),
				customer3.log.containsString("msgFollowMe"));
		waiter.msgReadyToOrder(customer1);
		waiter.pickAndExecuteAnAction();
		assertTrue("Check that customer 1 got msgWhatWouldYouLike"
				+ customer1.log.toString(),
				customer1.log.containsString("msgWhatWouldYouLike"));
		waiter.msgReadyToOrder(customer2);
		waiter.pickAndExecuteAnAction();
		assertTrue("Check that customer 2 got msgWhatWouldYouLike"
				+ customer2.log.toString(),
				customer2.log.containsString("msgWhatWouldYouLike"));
		waiter.msgReadyToOrder(customer3);
		waiter.pickAndExecuteAnAction();
		assertTrue("Check that customer 3 got msgWhatWouldYouLike"
				+ customer3.log.toString(),
				customer3.log.containsString("msgWhatWouldYouLike"));
		waiter.msgHereIsMyOrder(customer1, "Steak");
		waiter.pickAndExecuteAnAction();
		assertEquals(
				"Check that there is now one order on the revolving stand",
				cook.getRevolvingStand().size(), 1);
		waiter.msgHereIsMyOrder(customer2, "Pizza");
		waiter.pickAndExecuteAnAction();
		assertEquals(
				"Check that there is now one order on the revolving stand",
				cook.getRevolvingStand().size(), 2);
		waiter.msgHereIsMyOrder(customer3, "Salad");
		waiter.pickAndExecuteAnAction();
		assertEquals(
				"Check that there is now one order on the revolving stand",
				cook.getRevolvingStand().size(), 3);

	}
}
