package restaurant.restaurant_jerryweb.test;

import java.util.Map;

import restaurant.restaurant_jerryweb.CashierRole;
import restaurant.restaurant_jerryweb.CashierRole.mBillState;
import restaurant.restaurant_jerryweb.CookRole;
//import restaurant.CashierAgent.cashierBillState;
//import restaurant.WaiterAgent.Bill;
import restaurant.restaurant_jerryweb.test.mock.MockCustomer;
import restaurant.restaurant_jerryweb.test.mock.MockWaiter;
import restaurant.restaurant_jerryweb.test.mock.MockMarket;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierRole cashier;
	MockWaiter waiter;
	
	MockCustomer customer;
	MockCustomer customer1;
	MockCustomer customer2;
	MockCustomer customer3;
	
	MockMarket market;
	MockMarket market2;
	MockMarket market3;
	
	CookRole cook;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierRole("cashier");		
		customer = new MockCustomer("mock customer");
		customer1 = new MockCustomer("mock customer1");
		customer2 = new MockCustomer("mock customer2");
		customer3 = new MockCustomer("mock customer3");
		waiter = new MockWaiter("mock waiter");
		market = new MockMarket("K-Mart");
		market2 = new MockMarket("Sams Club");
		market3 = new MockMarket("Albertson's");
		cook = new CookRole("Cook");
	}	

	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;		
	
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.Bills.size(), 0);		
		//assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: ");
		
	
		cashier.msgComputeBill(waiter, customer, "steak");//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.Bills.size(), 1);
		
		assertTrue("Cashier's scheduler should have returned true in order to compute the bill, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("MockWaiter should not have an empty event log after Cashier's scheduler is called for the first time. The MockWaiter's event log reads: "
				+ waiter.log.toString(), 1, waiter.log.size());
		
				
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
				cashier.Bills.get(0).c == customer);
		
		//step 2
		waiter.msgHereIsBill(customer, 15.99);
		
		assertEquals("MockWaiter should have two events in event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 2, waiter.log.size());
		

		
		customer.msgHereIsCheck();
		//assertEquals("Cashier should have 1 bills in it. It doesn't.",cashier.Bills.size(), 1);	
		
		assertEquals("MockCustomer should have one event in the event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 1, customer.log.size());
		
		
		/*
		 * 
		 * 
		//step 2 of the test
		cashier.msgPayment(customer, 20);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.Bills.get(0).s == OrderState.customerApproached);
		
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received ReadyToPay"));

		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
				+ cashier.bills.get(0).bill.netCost, cashier.bills.get(0).bill.netCost == 7.98);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).bill.customer == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
	
			
		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
		
		
		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.bills.get(0).changeDue, cashier.bills.get(0).changeDue == 0);
		
		
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.done);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		*/
	
	}//end one normal customer scenario
	
	public void testThreeNormalCustomersScenario()
	{

		customer1.cashier = cashier;		
		customer2.cashier = cashier;	
		customer3.cashier = cashier;
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.Bills.size(), 0);		
		
		assertEquals("The amount of money that the cashier should start with is 200.0 dollars, but is not: " + cashier.money, 200.0, cashier.money);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Customer should have 20 dollars when entering the restaurant, but he/she didn't.", 20.0, customer1.cash);

		
		cashier.msgComputeBill(waiter, customer1, "steak");//send the message from a waiter
		cashier.msgComputeBill(waiter, customer2, "salad");
		cashier.msgComputeBill(waiter, customer3, "pizza");
		

		
		assertEquals("Cashier should have 3 bills in it. It doesn't.", cashier.Bills.size(), 3);
		
		assertTrue("Cashier's scheduler should have returned true in order to compute the bill, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("MockWaiter should not have an empty event log after Cashier's scheduler is called for the first time. The MockWaiter's event log reads: "
				+ waiter.log.toString(), 1, waiter.log.size());
		
				
		assertEquals("MockCustomer1 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer1.log.toString(), 0, customer1.log.size());
		assertEquals("MockCustomer2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer2.log.toString(), 0, customer2.log.size());
		assertEquals("MockCustomer3 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer3.log.toString(), 0, customer3.log.size());
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't." + cashier.Bills.get(0).c.getName(), 
				cashier.Bills.get(0).c == customer1);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't." + cashier.Bills.get(1).c.getName(), 
				cashier.Bills.get(1).c == customer2);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't."+ cashier.Bills.get(2).c.getName(), 
				cashier.Bills.get(2).c == customer3);
		
		//step 2*/
		
		
		cashier.msgPayment(customer1, customer1.cash);
		
		assertEquals("customer1.cash should be 20", cashier.Bills.get(0).payment, customer1.cash);
		
		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.makeChange(cashier.Bills.get(0), 0);
		assertEquals("The amount of money that the cashier has after the first customer should be 215.99 dollars, but is not: " + cashier.money, 215.99, cashier.money);
		
		assertEquals("" + customer1.getName() + " should recieve $4.01, but does not:  " + cashier.money, 4.01, cashier.change);
		
		//Since the bill is removed, the cahsier's bill list should only have two now
		//assertEquals("Since the bill is removed, the cahsier's bill list should only have two now, but it doesn't: " + cashier.Bills.size(),2,  cashier.Bills.size());
		
		cashier.msgPayment(customer2, customer2.cash);
		
		assertEquals("customer2.cash should be 20", cashier.Bills.get(1).payment, customer2.cash);

		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.makeChange(cashier.Bills.get(1), 1);
		
		assertEquals("The amount of money that the cashier has after the second customer should be 221.98 dollars, but is not: ", 221.98000000000002, cashier.money);
		
		assertEquals("" + customer2.getName() + " should recieve $14.01, but does not:  ", 14.01, cashier.change);
		
		
		//Since the bill is removed, the cahsier's bill list should only have two now
				//assertEquals("Since the bill is removed, the cahsier's bill list should only have one now, but it doesn't: " + cashier.Bills.size(), 1,  cashier.Bills.size());
				
		cashier.msgPayment(customer3, customer3.cash);
		
		assertEquals("customer3.cash should be 20", cashier.Bills.get(2).payment, customer3.cash);

		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		//cashier.makeChange(cashier.Bills.get(0), 0);
		
		assertEquals("The amount of money that the cashier has after the third customer should be $230.97000000000003 dollars, but is not: ", 230.97000000000003, cashier.money);
		
		assertEquals("" + customer3.getName() + " should have a debt of $11.01, but does not:  ", 11.01, cashier.change);
		
	}
	
	
	public void testOneNonNormalCustomerScenario(){
		//One customer is out of money
		
		customer.cashier = cashier;		
		customer.cash = 5;
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.Bills.size(), 0);
		
		assertEquals("Customer should have 20 dollars when entering the restaurant, but he/she didn't.", 5.0, customer.cash);

		
		
		cashier.msgComputeBill(waiter, customer, "steak");
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.Bills.size(), 1);

		assertTrue("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

		assertEquals("MockWaiter should not have an empty event log after Cashier's scheduler is called for the first time. The MockWaiter's event log reads: "
				+ waiter.log.toString(), 1, waiter.log.size());

		
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("CashierBill should contain one bill in it. It doesn't. " + cashier.Bills.size(), cashier.Bills.size() == 1);

		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", cashier.Bills.get(0).c == customer);
		
		

		
		cashier.msgPayment(customer, customer.cash);
		
		assertEquals("customer.cash should be 0", cashier.Bills.get(0).payment, customer.cash);
		
		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		//cashier.makeChange(cashier.Bills.get(0), 0);
		assertEquals("The amount of money that the cashier has after the first customer should be 205 dollars, but is not: " + cashier.money, 205.00, cashier.money);
		
		assertEquals("" + customer.getName() + " should have a debt of 10.99, but does not:  " + cashier.money, -10.99, cashier.change);
		
		//Since the bill is removed, the cahsier's bill list should only have two now
		//assertEquals("Since the bill is removed, the cahsier's bill list should only have two now, but it doesn't: " + cashier.Bills.size(),2,  cashier.Bills.size());
		
	
	}
	
	public void testTwoNonNormalCustomersScenario(){
		
		customer1.cashier = cashier;
		customer1.cash = 0.0;
		customer2.cashier = cashier;
		customer2.cash = 0.0;
		customer3.cashier = cashier;
		customer3.cash = 0.0;
		
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.Bills.size(), 0);
		
		assertEquals("Customer1 should have 0 dollars when entering the restaurant, but he/she didn't.", 0.0, customer1.cash);
		assertEquals("Customer2 should have 0 dollars when entering the restaurant, but he/she didn't.", 0.0, customer2.cash);
		assertEquals("Customer3 should have 0 dollars when entering the restaurant, but he/she didn't.", 0.0, customer3.cash);
		
		
		cashier.msgComputeBill(waiter, customer1, "steak");//send the message from a waiter
		cashier.msgComputeBill(waiter, customer2, "salad");
		cashier.msgComputeBill(waiter, customer3, "pizza");
	
		assertEquals("Cashier should have 3 bills in it. It doesn't.", cashier.Bills.size(), 3);
		
		assertTrue("Cashier's scheduler should have returned true in order to compute the bill, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("MockWaiter should not have an empty event log after Cashier's scheduler is called for the first time. The MockWaiter's event log reads: "
				+ waiter.log.toString(), 1, waiter.log.size());
		
				
		assertEquals("MockCustomer1 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer1.log.toString(), 0, customer1.log.size());
		assertEquals("MockCustomer2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer2.log.toString(), 0, customer2.log.size());
		assertEquals("MockCustomer3 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer3.log.toString(), 0, customer3.log.size());
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't." + cashier.Bills.get(0).c.getName(), 
				cashier.Bills.get(0).c == customer1);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't." + cashier.Bills.get(1).c.getName(), 
				cashier.Bills.get(1).c == customer2);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't."+ cashier.Bills.get(2).c.getName(), 
				cashier.Bills.get(2).c == customer3);
		
		cashier.msgPayment(customer1, customer1.cash);
		
		assertEquals("customer1.cash should be 0", cashier.Bills.get(0).payment, customer1.cash);
		
		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.makeChange(cashier.Bills.get(0), 0);
		assertEquals("The amount of money that the cashier has after the first customer should be 200 dollars, but is not: " + cashier.money, 200.00, cashier.money);
		
		assertEquals("" + customer1.getName() + " should have a debt of 10.99, but does not:  " + cashier.money, -15.99, cashier.change);
		
		//Since the bill is removed, the cahsier's bill list should only have two now
		//assertEquals("Since the bill is removed, the cahsier's bill list should only have two now, but it doesn't: " + cashier.Bills.size(),2,  cashier.Bills.size());
		
		cashier.msgPayment(customer2, customer2.cash);
		
		assertEquals("customer2.cash should be 20", cashier.Bills.get(1).payment, customer2.cash);

		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.makeChange(cashier.Bills.get(1), 1);
		
		assertEquals("The amount of money that the cashier has after the second customer should be 200 dollars, but is not: ", 200.0, cashier.money);
		
		assertEquals("" + customer2.getName() + " should have a debt of $14.01, but does not:  ", -5.99, cashier.change);
		
		
		//Since the bill is removed, the cahsier's bill list should only have two now
				//assertEquals("Since the bill is removed, the cahsier's bill list should only have one now, but it doesn't: " + cashier.Bills.size(), 1,  cashier.Bills.size());
				
		cashier.msgPayment(customer3, customer3.cash);
		
		assertEquals("customer3.cash should be 0", cashier.Bills.get(2).payment, customer3.cash);

		assertTrue("Cashier's scheduler should have returned true in order to create change for the customer, but didn't.", cashier.pickAndExecuteAnAction());
		
		//cashier.makeChange(cashier.Bills.get(0), 0);
		
		assertEquals("The amount of money that the cashier has after the third customer should be $200 dollars, but is not: ", 200.0, cashier.money);
		
		assertEquals("" + customer3.getName() + " should have a debt of $11.01, but does not:  ", -8.99, cashier.change);
		
	}
	
	
	public void testNormalMarketBillingScenario(){
		
		market.cashier = cashier;		
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.Bills.size(), 0);
		
		assertEquals("The amount of money that the cashier should start with is 200.0 dollars, but is not: " + cashier.money, 200.0, cashier.money);
		
		assertEquals("Market shouldn't have any events in it's event log, but there are(is): " + market.log.toString(), 0, market.log.size());
		
		market. msgGiveMeOrder("steak", cook.foodMap);
		
		//assertEquals("Market should have one item in the restocking list... there are not: " + market.getRestockList().size(), 1, market.getRestockList().size());
		//One steak from the market costs 7 dollars
		
		cashier.msgPayMarket(market, 7, 0);
		
		assertTrue("Cashier's scheduler should have returned true in order to process the bill from the market, but didn't.", cashier.pickAndExecuteAnAction());
	
		assertEquals("Market should now have two events in the event log, however there are(is): " + market.log.toString(), 2, market.log.size());
		
		//check to see if the bill from the market contains the correct total
		assertTrue("CashierBill should contain a bill from the corresponding market, which possess the price of the order. It doesn't.", 
				cashier.MarketBills.get(0).amount == 7);
		//check to see if the bill from the market is in the correct state
		assertTrue("CashierBill should contain a bill from the corresponding market, which is still in the OutStanding state. It doesn't." + cashier.MarketBills.get(0).s, 
				cashier.MarketBills.get(0).s == mBillState.SentPayment);
		
		
		//this is the next part confirming that the bill from the market was paid.
		cashier.msgMarketPaid(market);
		
		assertEquals("Market should still only have two events in the event log, however there are(is): " + market.log.toString(), 2, market.log.size());
		//assertTrue("The cashier should have 193 dollars left . Instead, he has: " + cashier.money, cashier.money == 193.0);
		
		
	}
	
	public void testThreeNormalMarketBillsScenario(){
		market.cashier = cashier;
		market2.cashier = cashier;
		market3.cashier = cashier;
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.Bills.size(), 0);
		
		assertEquals("The amount of money that the cashier should start with is 200.0 dollars, but is not: " + cashier.money, 200.0, cashier.money);
		
		assertEquals("Market shouldn't have any events in it's event log, but there are(is): " + market.log.toString(), 0, market.log.size());
		
		assertEquals("Market2 shouldn't have any events in it's event log, but there are(is): " + market2.log.toString(), 0, market2.log.size());

		//assertEquals("Market3 shouldn't have any events in it's event log, but there are(is): " + market3.log.toString(), 0, market3.log.size());
		
		
		market. msgGiveMeOrder("steak", cook.foodMap);
		market2.msgGiveMeOrder("salad", cook.foodMap);
		//market3. msgGiveMeOrder("pizza", cook.foodMap);
		
		cashier.msgPayMarket(market, 7, 0);
		cashier.msgPayMarket(market2, 3, 0);
		//cashier.msgPayMarket(market3, 4, 0);
		
		assertTrue("Cashier's scheduler should have returned true in order to process the bill from the market, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Market should now have two events in the event log, however there are(is): " + market.log.toString(), 2, market.log.size());
		assertEquals("Market2 should still only have one event in the event log, however there are(is): " + market2.log.toString(), 1, market2.log.size());
		//assertEquals("Market3 should now have one event in the event log, however there are(is): " + market3.log.toString(), 1, market3.log.size());
		
		//assertTrue("The cashier should have 193 dollars left . Instead, he has: " + cashier.money, cashier.money == 193.0);
		
		//check to see if the bill from the market contains the correct total
		assertTrue("CashierBill should contain a bill from the corresponding market, which possess the price of the order. It doesn't.", 
				cashier.MarketBills.get(0).amount == 7);
		//check to see if the bill from the market is in the correct state
		assertTrue("CashierBill should contain a bill from the corresponding market, which is still in the SentPayment state. It doesn't." + cashier.MarketBills.get(0).s, 
				cashier.MarketBills.get(0).s == mBillState.SentPayment);
		
		
		//this is the next part confirming that the bill from the market was paid.
		cashier.msgMarketPaid(market);
		
		
		assertTrue("Cashier's scheduler should have returned true in order to process the bill from the market, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Market should now have two events in the event log, however there are(is): " + market2.log.toString(), 2, market2.log.size());
		
		//check to see if the bill from the market contains the correct total
		assertTrue("CashierBill should contain a bill from the corresponding market, which possess the price of the order. It doesn't.", 
				cashier.MarketBills.get(1).amount == 3);
		//check to see if the bill from the market is in the correct state
		assertTrue("CashierBill should contain a bill from the corresponding market, which is still in the SentPayment state. It doesn't." + cashier.MarketBills.get(1).s, 
				cashier.MarketBills.get(1).s == mBillState.SentPayment);
	
		//this is the next part confirming that the bill from the market was paid.
		cashier.msgMarketPaid(market2);
		
				
		/*
		//check to see if the bill from the market contains the correct total
		assertTrue("CashierBill should contain a bill from the corresponding market, which possess the price of the order. It doesn't.", 
				cashier.MarketBills.get(2).amount == 4);
		//check to see if the bill from the market is in the correct state
		assertTrue("CashierBill should contain a bill from the corresponding market, which is still in the SentPayment state. It doesn't." + cashier.MarketBills.get(2).s + " " +
				cashier.MarketBills.get(2).amount + " " + cashier.MarketBills.get(2).market.getName(), 
				cashier.MarketBills.get(2).s == mBillState.SentPayment);

		//this is the next part confirming that the bill from the market was paid.
		cashier.msgMarketPaid(market3);*/
		
		
	}
	

		
}
