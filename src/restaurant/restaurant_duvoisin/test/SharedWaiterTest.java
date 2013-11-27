package restaurant.restaurant_duvoisin.test;

import base.PersonAgent;
import restaurant.restaurant_duvoisin.roles.*;
import restaurant.restaurant_duvoisin.test.mock.*;
import junit.framework.*;

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
	MockCook cook;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		waiter = new AndreSharedWaiterRole(new PersonAgent());
		cook = new MockCook("cook");
	}
	
	//Test of Scenario 1 in v2.2A
	public void testSingleMarketScenario() {
		market1.cashier = cashier;
		
		// Precondition
		assertEquals("Cashier should have 0 market checks in it. It doesn't.", cashier.openMarketChecks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgComputeMarketBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		// Step 1
		cashier.msgComputeMarketBill(market1, "pizza", 2);
		
		// Pre/Post
		assertTrue("Cashier should have logged \"msgComputeMarketBill received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgComputeMarketBill received"));
		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());
		assertEquals("Cashier should have 1 market check in it. It doesn't.", cashier.openMarketChecks.size(), 1);
		
		// Step 2
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a check from Market #1), but didn't.", cashier.pickAndExecuteAnAction());
		
		// Pre/Post
		assertTrue("Cashier should have logged \"Doing HandleMarketCheck\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Doing HandleMarketCheck"));
		assertTrue("MockMarket should have logged \"msgFoodPayment received\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("msgFoodPayment received"));
		assertTrue("MockMarket should have recieved a payment for pizza. It didn't.",
				market1.type.equals("pizza"));
		assertTrue("MockMarket should have recieved a payment of $6.00. It didn't.",
				market1.payment == 6.00);
		assertEquals("Cashier should have 0 market checks in it. It doesn't.", cashier.openMarketChecks.size(), 0);
		assertTrue("Cashier should have $9.00 left. It didn't.",
				cashier.money == 9.00);
		
		// Step 3
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
}
