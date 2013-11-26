package restaurant.restaurant_duvoisin.test;

import restaurant.restaurant_duvoisin.roles.AndreCashierRole;
import restaurant.restaurant_duvoisin.roles.AndreCashierRole.CheckState;
import restaurant.restaurant_duvoisin.test.mock.MockCustomer;
import restaurant.restaurant_duvoisin.test.mock.MockMarket;
import restaurant.restaurant_duvoisin.test.mock.MockWaiter;
import junit.framework.*;

/**
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 *
 * @author Andre Duvoisin
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	AndreCashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockMarket market1;
	MockMarket market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new AndreCashierRole("cashier");
		waiter = new MockWaiter("mockwaiter");
		customer1 = new MockCustomer("mockcustomer1");
		customer2 = new MockCustomer("mockcustomer2");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
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
	
	//Test of Scenario 2 in v2.2A
	public void testMultipleMarketScenario() {
		market1.cashier = cashier;
		market2.cashier = cashier;
		
		// Preconditions
		assertEquals("Cashier should have 0 market checks in it. It doesn't.", cashier.openMarketChecks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgComputeMarketBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		// Step 1
		cashier.msgComputeMarketBill(market1, "chicken", 1);
		
		// Pre/Post
		assertTrue("Cashier should have logged \"msgComputeMarketBill received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgComputeMarketBill received"));
		assertEquals("MockMarket 1 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());
		assertEquals("Cashier should have 1 market check in it. It doesn't.", cashier.openMarketChecks.size(), 1);
		
		// Step 2
		cashier.msgComputeMarketBill(market2, "chicken", 2);
		
		// Pre/Post
		assertTrue("Cashier should have logged \"msgComputeMarketBill received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgComputeMarketBill received"));
		assertEquals("Cashier should have two logs total. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 2, cashier.log.size());
		assertEquals("MockMarket 2 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market2.log.toString(), 0, market2.log.size());
		assertEquals("Cashier should have 2 market checks in it. It doesn't.", cashier.openMarketChecks.size(), 2);
		
		// Step 3
		assertTrue("Cashier's scheduler should have returned true (2 actions to do on a check from Market #1 and Market #2), but didn't.", cashier.pickAndExecuteAnAction());
		
		// Pre/Post
		assertTrue("Cashier should have logged \"Doing HandleMarketCheck\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Doing HandleMarketCheck"));
		assertTrue("MockMarket 1 should have logged \"msgFoodPayment received\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("msgFoodPayment received"));
		assertTrue("MockMarket 1 should have recieved a payment for chicken. It didn't.",
				market1.type.equals("chicken"));
		assertTrue("MockMarket 1 should have recieved a payment of $5.00. It didn't.",
				market1.payment == 5.00);
		assertEquals("Cashier should have 1 market check in it. It doesn't.", cashier.openMarketChecks.size(), 1);
		assertTrue("Cashier should have $10.00 left. It didn't.",
				cashier.money == 10.00);
		
		// Step 4
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a check from Market #2), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should have logged \"Doing HandleMarketCheck\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Doing HandleMarketCheck"));
		assertTrue("MockMarket 2 should have logged \"msgFoodPayment received\" but didn't. His log reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("msgFoodPayment received"));
		assertTrue("MockMarket 2 should have recieved a payment for chicken. It didn't.",
				market2.type.equals("chicken"));
		assertTrue("MockMarket 2 should have recieved a payment of $10.00. It didn't.",
				market2.payment == 10.00);
		assertEquals("Cashier should have 0 market checks in it. It doesn't.", cashier.openMarketChecks.size(), 0);
		assertTrue("Cashier should have $0.00 left. It didn't.",
				cashier.money == 0.00);
		
		// Step 5
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	// One customer is ready to pay the exact bill.
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer1.cashier = cashier; //You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.openChecks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgComputeBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgComputeBill(waiter, customer1, "steak");	//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.openChecks.size(), 1);
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer1.log.toString(), 0, customer1.log.size());
		
		//step 2 of the test
		cashier.msgPayment(customer1, 15.99);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == Paying. It doesn't.",
				cashier.openChecks.get(0).state == CheckState.Paying);
		assertTrue("Cashier should have logged \"msgPayment received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgPayment received"));
		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.openChecks.get(0).amountOwed, cashier.openChecks.get(0).amountOwed == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.openChecks.get(0).customer == customer1);
		
		//step 3
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.openChecks.size(), 0);
		
		//step 4
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	// Two customers are ready to pay the exact bill.
	public void testTwoNormalCustomerScenario()
	{		
		customer1.cashier = cashier;
		customer2.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.openChecks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgComputeBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgComputeBill(waiter, customer1, "steak");	//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.openChecks.size(), 1);
		
		//step 2
		cashier.msgComputeBill(waiter, customer2, "chicken");	//send the message from a waiter

		//pre/post
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 2 bills in it. It doesn't.", cashier.openChecks.size(), 2);
		
		// Step 3
		assertTrue("Cashier's scheduler should have returned true (2 actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		// Pre/Post
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer1.log.toString(), 0, customer1.log.size());
		
		// Step 4
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		// Pre/Post
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockCustomer's event log reads: "
						+ customer2.log.toString(), 0, customer2.log.size());
		
		//step 5 of the test
		cashier.msgPayment(customer1, 15.99);
		
		//pre/post
		assertTrue("CashierBill should contain a bill with state == Paying. It doesn't.",
				cashier.openChecks.get(0).state == CheckState.Paying);
		assertTrue("Cashier should have logged \"msgPayment received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgPayment received"));
		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.openChecks.get(0).amountOwed, cashier.openChecks.get(0).amountOwed == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.openChecks.get(0).customer == customer1);
		
		//step 6 of the test
		cashier.msgPayment(customer2, 10.99);
		
		//pre/post
		assertTrue("CashierBill should contain a bill with state == Paying. It doesn't.",
				cashier.openChecks.get(1).state == CheckState.Paying);
		assertTrue("Cashier should have logged \"msgPayment received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgPayment received"));
		assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
				+ cashier.openChecks.get(1).amountOwed, cashier.openChecks.get(1).amountOwed == 10.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.openChecks.get(1).customer == customer2);
		
		//step 6
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer1's msgPayment), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.openChecks.size(), 1);
		
		//step 7
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer2's msgPayment), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.openChecks.size(), 0);
		
		//step 8
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	// One customer cannot pay the bill.
	public void testOneCustomerTooPoor()
	{		
		customer1.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.openChecks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgComputeBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgComputeBill(waiter, customer1, "steak");	//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.openChecks.size(), 1);
		
		// Step 2
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		// Pre/Post
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer1.log.toString(), 0, customer1.log.size());
		
		//step 3 of the test
		cashier.msgPayment(customer1, 10.99);
		
		//pre/post
		assertTrue("CashierBill should contain a bill with state == Paying. It doesn't.",
				cashier.openChecks.get(0).state == CheckState.Paying);
		assertTrue("Cashier should have logged \"msgPayment received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgPayment received"));
		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.openChecks.get(0).amountOwed, cashier.openChecks.get(0).amountOwed == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.openChecks.get(0).customer == customer1);
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer1's msgPayment), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged \"msgHereIsChange received. Change = -5.0\" to indicate owing money. His log reads instead: " 
				+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("msgHereIsChange received. Change = -5.0"));
		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.openChecks.size(), 0);
		
		//step 5
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	// One customer is ready to pay the exact bill, with a standard Market Bill thrown in the middle.
	public void testOneNormalCustomerAndOneNormalMarketScenario()
	{
		customer1.cashier = cashier;
		market1.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.openChecks.size(), 0);
		assertEquals("Cashier should have 0 market checks in it. It doesn't.",cashier.openMarketChecks.size(), 0);	
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgComputeBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgComputeBill(waiter, customer1, "chicken");	//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.openChecks.size(), 1);
		
		// Step 2
		cashier.msgComputeMarketBill(market1, "pizza", 2);
		
		// Pre/Post
		assertTrue("Cashier should have logged \"msgComputeMarketBill received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgComputeMarketBill received"));
		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());
		assertEquals("Cashier should have 1 market check in it. It doesn't.", cashier.openMarketChecks.size(), 1);
		
		// Step 3
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a bill from a waiter, 1 action to do on a bill from a market), but didn't.", cashier.pickAndExecuteAnAction());
		
		// pre/post
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer1.log.toString(), 0, customer1.log.size());
		
		// Step 4
		assertTrue("Cashier's scheduler should have returned true (1 action to do on a check from Market), but didn't.", cashier.pickAndExecuteAnAction());
		
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
		
		//step 5 of the test
		cashier.msgPayment(customer1, 10.99);
		
		//check postconditions for step 5 / preconditions for step 6
		assertTrue("CashierBill should contain a bill with state == Paying. It doesn't.",
				cashier.openChecks.get(0).state == CheckState.Paying);
		assertTrue("Cashier should have logged \"msgPayment received\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("msgPayment received"));
		assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
				+ cashier.openChecks.get(0).amountOwed, cashier.openChecks.get(0).amountOwed == 10.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.openChecks.get(0).customer == customer1);
		
		//step 6
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 6 / preconditions for step 7
		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.openChecks.size(), 0);
		assertEquals("Cashier should have 0 market checks in it. It doesn't.", cashier.openMarketChecks.size(), 0);
		
		//step 7
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
}
