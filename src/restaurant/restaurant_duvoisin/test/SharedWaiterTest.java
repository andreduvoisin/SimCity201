package restaurant.restaurant_duvoisin.test;

import junit.framework.TestCase;
import restaurant.restaurant_duvoisin.gui.CookGui;
import restaurant.restaurant_duvoisin.gui.WaiterGui;
import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.test.mock.MockCustomer;
import base.PersonAgent;

/**
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 *
 * @author Andre Duvoisin
 */
public class SharedWaiterTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	AndreSharedWaiterRole waiter;
	AndreCookRole cook;
	MockCustomer customer1;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		PersonAgent person = new PersonAgent();
		waiter = new AndreSharedWaiterRole(person);
		//cook = new AndreCookRole("cook");
		customer1 = new MockCustomer("customer1");
	}
	
	public void testOneOrderScenario() {
		WaiterGui wg = null;
		CookGui cg = null;
		wg = new WaiterGui(waiter);
		cg = new CookGui(cook);
		waiter.setGui(wg);
		cook.setGui(cg);
		customer1.waiter = waiter;
		//waiter.cook = cook;
		waiter.msgSitAtTable(customer1, 1, 1);
		waiter.msgImReadyToOrder(customer1);
		waiter.msgHereIsMyChoice(customer1, "steak");
		
		// Precondition
		assertEquals("Waiter should have 1 customer. It doesn't.", 1, waiter.customers.size());
		
		// Step 1
		waiter.atCook.release();
		assertTrue("Waiter's scheduler should have returned true (1 action to do on an order from Customer #1), but didn't.", waiter.pickAndExecuteAnAction());
		
		// Pre/Post
		assertTrue("Waiter should have logged \"Doing GiveOrderToCook\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Doing GiveOrderToCook"));
		assertEquals("MockCook should have an empty event log before the Cashier's scheduler is called. Instead, the MockCook's event log reads: "
				+ cook.log.toString(), 0, cook.log.size());
		assertEquals("Revolving Stand should have 1 order in it. It doesn't.", 1, cook.getRevolvingStand().size());
		
		// Step 2
		cook.atStand.release();
		cook.CheckRevolvingStand();
		
		// Pre/Post
		assertTrue("Cook should have logged \"Doing CheckRevolvingStand\" but didn't. His log reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Doing CheckRevolvingStand"));
		assertEquals("Revolving Stand should have 0 orders in it. It doesn't.", 0, cook.getRevolvingStand().size());
		assertEquals("Orders should have 1 order in it. It doesn't.", 1, cook.orders.size());
		
		// Step 3
		cook.atFridge.release();
		cook.atGrill.release();
		assertTrue("Cook's scheduler should have returned true (1 action to do on 1 order), but didn't.", cook.pickAndExecuteAnAction());
		
		// Post
		assertTrue("Cook should have logged \"Doing TryToCookFood\" but didn't. His log reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Doing TryToCookFood"));
	}
}
