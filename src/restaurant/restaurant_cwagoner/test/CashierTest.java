package restaurant.restaurant_cwagoner.test;

import junit.framework.TestCase;
import restaurant.restaurant_cwagoner.roles.CwagonerCashierRole;
import restaurant.restaurant_cwagoner.test.mock.MockCustomer;
import restaurant.restaurant_cwagoner.test.mock.MockMarket;
import restaurant.restaurant_cwagoner.test.mock.MockWaiter;

public class CashierTest extends TestCase {
	
	//these are instantiated for each test separately via the setUp() method.
	CwagonerCashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market1, market2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();		
		cashier = new CwagonerCashierRole(null);		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
	}	
	
	
	
	/**
	 *	One customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario() {
		//setUp() runs first before this test!
		
		customer.cwagonerCashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.Bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//public Bill(Customer customer, double price) {
		CwagonerCashierRole.Bill bill = new CwagonerCashierRole.Bill(waiter, customer, 7.98);
		cashier.HereIsBill(bill);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.Bills.size(), 1);
		
		assertFalse("Cashier's scheduler should have returned false"
					+ " (no actions to do on a bill from a waiter), but didn't.",
					cashier.pickAndExecuteAnAction());
		
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called"
					 + " for the first time. Instead, the MockWaiter's event log reads: "
					 + waiter.log.toString(),
					 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//step 2 of the test
		cashier.msgReadyToPay(customer);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.Bills.get(0).state == CwagonerCashierRole.Bill.State.customerApproached);
		
		assertTrue("Cashier should have logged \"Received msgReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgReadyToPay"));

		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
				+ cashier.Bills.get(0).netCost, cashier.Bills.get(0).netCost == 7.98);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.Bills.get(0).customer == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
	
			
		assertTrue("Cashier should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment"));
		
		
		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.Bills.get(0).changeDue, cashier.Bills.get(0).changeDue == 0);
		
		
		cashier.pickAndExecuteAnAction();

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario
	
	
}
