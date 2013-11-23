package restaurant_tranac.test;

import base.PersonAgent;
import restaurant_tranac.roles.RestaurantCashierRole_at;
import restaurant_tranac.test.mock.*;
import junit.framework.*;

/**
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * 
 * @author Angelica Tran
 */

public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent person;
	RestaurantCashierRole_at cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	double baseMoney = 5000;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		person = new PersonAgent();
		cashier = new RestaurantCashierRole_at();
		cashier.setPerson(person);
		customer = new MockCustomer();		
		waiter = new MockWaiter();
		market = new MockMarket();
	}
	
	/** 
	 * Tests the cashier paying one market bill.
	 */
	public void testOneMarket() {
		System.out.println("One Market");
	  //create info for bill
		String item = "mockItem";
		double cost = 15;
	  //check preconditions
		assertEquals("Cashier should have 0 bills. It doesn't.",
				cashier.getNumBills(), 0);
		assertEquals("Cashier should have base money.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		
		
		cashier.msgHereIsBill(market,item,cost);
	  //assert number of bills
		assertEquals("Cashier should have 1 bill. It doesn't.",
				cashier.getNumBills(),1);
	  //assert bill is pending
		assertEquals("Cashier's bill should be pending. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert market has received message from the cashier
		assertTrue("MockMarket should have logged \"Received msgHereIsPayment from cashier."
				+ "Item = " + item + ". Payment = " + cost
				+ "\" but didn't. Instead his log reads: "
				+ market.log.getLastLoggedEvent().toString(),
				market.log.containsString("Received msgHereIsPayment from cashier. "
						+ "Item = " + item + ". Payment = " + cost));
	  //cashier bill should be fulfilled
		assertEquals("Cashier's bill should be fulfilled. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Fulfilled);
	  //assert money no longer has cost
		assertEquals("Cashier should have " + (double)(baseMoney-cost) + ". It doesn't.",
				cashier.money,baseMoney-cost);
}
	
	/**
	 * Tests the cashier paying two market bills.
	 */
	public void testTwoMarkets() {
		System.out.println("Two Markets");
	  //create info for bills
		String item = "mockItem";
		double cost = 15;
		String item2 = "mockItem2";
		double cost2 = 5;
	  //create second market
		MockMarket market2 = new MockMarket();
		
	  //check preconditions
		assertEquals("Cashier should have 0 bills. It doesn't.",
				cashier.getNumBills(), 0);
		assertEquals("Cashier should have base money.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);

		
		cashier.msgHereIsBill(market,item,cost);
	  //assert number of bills
		assertEquals("Cashier should have 1 bill. It doesn't.",
				cashier.getNumBills(),1);
	  //assert bill is pending
		assertEquals("Cashier's bill should be pending. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
			
		
		cashier.msgHereIsBill(market2,item2,cost2);
	  //assert number of bills
		assertEquals("Cashier should have 2 bills. It doesn't.",
				cashier.getNumBills(),2);
	  //assert both bills are pending
		assertEquals("Cashier's bill should be pending. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
		assertEquals("Cashier's bill should be pending. It isn't.",
				cashier.bills.get(1).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert market has received message from the cashier
		assertTrue("MockMarket should have logged \"Received msgHereIsPayment from cashier."
				+ "Item = " + item + ". Payment = " + cost
				+ "\" but didn't. Instead his log reads: "
				+ market.log.getLastLoggedEvent().toString(),
				market.log.containsString("Received msgHereIsPayment from cashier. "
						+ "Item = " + item + ". Payment = " + cost));
	  //assert first bill is fulfilled, second bill is pending
		assertEquals("Cashier's first bill should be fulfilled. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Fulfilled);
		assertEquals("Cashier's second bill should be pending. It isn't.",
				cashier.bills.get(1).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
	  //assert money no longer has cost
		assertEquals("Cashier should have " + (double)(baseMoney-cost) + ". It doesn't.",
					cashier.money,baseMoney-cost);

		
		cashier.pickAndExecuteAnAction();
	  //assert market2 has received message from the cashier
		assertTrue("MockMarket2 should have logged \"Received msgHereIsPayment from cashier."
					+ "Item = " + item + ". Payment = " + cost
					+ "\" but didn't. Instead his log reads: "
					+ market2.log.getLastLoggedEvent().toString(),
					market2.log.containsString("Received msgHereIsPayment from cashier. "
							+ "Item = " + item2 + ". Payment = " + cost2));
	  //assert both bills are fulfilled
		assertEquals("Cashier's first bill should be fulfilled. It isn't.",
					cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Fulfilled);
		assertEquals("Cashier's second bill should be fulfilled. It isn't.",
					cashier.bills.get(1).getStatus(),RestaurantCashierRole_at.BillStatus.Fulfilled);
	  //assert money no longer has cost and cost2
		assertEquals("Cashier should have " + (double)(baseMoney-cost-cost2) + ". It doesn't.",
							cashier.money,baseMoney-cost-cost2);
	}
	
	/**
	 * Tests one customer and one waiter.
	 */
	public void testOneCustomerOneWaiter()
	{
		System.out.println("One Customer");
	  //create info for check
		String item = "Steak";
		double cost = 15.99;
	  //check preconditions
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		assertEquals("Cashier should have base money. It doesn't.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 bills. It doesn't.",cashier.getNumBills(), 0);
		
		
		cashier.msgComputeCheck(waiter, customer, item);
	  //assert there is one check
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert check is pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert waiter has received message from the cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost));
	  //assert check is computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		
		
		cashier.msgHereIsPayment(customer, cost);
	  //assert check is paying
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
	
		
		cashier.pickAndExecuteAnAction();
	  //assert customer has received message from the cashier
		assertTrue("MockCustomer should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));
	  //assert check is finished
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost) + ". It doesn't.",
				cashier.money,baseMoney+cost);
	}
	
	/**
	 * Tests two customers and one waiter asking for checks.
	 */
	public void testTwoCustomersOneWaiter()
	{
		System.out.println("Two Customers");
	  //create second customer and waiter
		MockCustomer customer2 = new MockCustomer();
	  //create test items
		String item = "Steak";
		double cost = 15.99;
		String item2 = "Salad";
		double cost2 = 5.99;
	  //check preconditions
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		assertEquals("Cashier should have base money. It doesn't.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 bills. It doesn't.",cashier.getNumBills(), 0);
		
		cashier.msgComputeCheck(waiter, customer, item);
	  //assert number of checks
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert check is pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);

		
		cashier.msgComputeCheck(waiter, customer2, item2);
	  //assert number of checks
		assertEquals("Cashier should have 2 checks. It doesn't.",cashier.getNumChecks(), 2);
	  //assert both checks are pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert waiter has received message from cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost));		
	  //assert check one is computed, check two is pending
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);

			
		cashier.pickAndExecuteAnAction();
	  //assert waiter has received message from cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost2 + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost2));		
	  //assert both are computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		
		
		cashier.msgHereIsPayment(customer, cost);
	  //assert check one is paying, check two is computed
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);

		
		cashier.msgHereIsPayment(customer2, cost2);
	  //assert both checks are paying
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert customer has received message from cashier
		assertTrue("MockCustomer should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));		
	  //assert check one is finished, check two is paying
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);		
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost) + ". It doesn't.",
				cashier.money,baseMoney+cost);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert customer2 has received message from cashier
		assertTrue("MockCustomer2 should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer2.log.getLastLoggedEvent().toString(),
				customer2.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));
	  //assert both checks are finished
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);		
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost+cost2) + ". It doesn't.",
				cashier.money,baseMoney+cost+cost2);
	}
	
	/**
	 * Tests a flake customer.
	 */
	public void testFlakeCustomerScenario()
	{
		System.out.println("One Flake");
	  //create info for check
		String item = "Steak";
		double cost = 15.99;
	  //check preconditions
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		assertEquals("Cashier should have base money. It doesn't.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 bills. It doesn't.",cashier.getNumBills(), 0);
		
		
		cashier.msgComputeCheck(waiter, customer, item);
	  //assert there is one check
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert bill is pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
			
			
		cashier.pickAndExecuteAnAction();
	  //assert waiter has received message from the cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost));
	  //assert check is computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		
		
		cashier.msgHereIsPayment(customer, 0);
	  //assert check is paying
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
	
			
		cashier.pickAndExecuteAnAction();
	  //assert customer has received message from the cashier
		assertTrue("MockCustomer should have logged \"Received msgPayNextTime from cashier.\" but didn't."
				+ "Instead his log reads: "
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgPayNextTime from cashier."));
	  //assert check is finished
		assertEquals("Check should be unfulfilled. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Unfulfilled);
	  //assert money
		assertEquals("Cashier should have " + baseMoney + ". It doesn't.",
				cashier.money,baseMoney);
	}

	/**
	 * Tests two customers and two waiters asking for checks. Action orders
	 * differ from two customers, one waiter. Second customer is a flake.
	 */
	public void testTwoCustomersTwoWaiters()
	{
		System.out.println("One Regular, One Flake");
  	  //create second customer and waiter
		MockCustomer customer2 = new MockCustomer();
		MockWaiter waiter2 = new MockWaiter();
	  //create test items
		String item = "Steak";
		double cost = 15.99;
		String item2 = "Salad";
		double cost2 = 5.99;
	  //check preconditions
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		assertEquals("Cashier should have base money. It doesn't.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 bills. It doesn't.",cashier.getNumBills(), 0);
		
			
		cashier.msgComputeCheck(waiter, customer, item);
	  //assert number of checks
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert one check is pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);

		
		cashier.pickAndExecuteAnAction();
	  //assert waiter has received message from cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost));		
	  //assert one check is computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
	
		
		cashier.msgComputeCheck(waiter2, customer2, item2);
	  //assert number of checks
		assertEquals("Cashier should have 2 checks. It doesn't.",cashier.getNumChecks(), 2);		
	  //assert one check is computed, one check is pending
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
		
		
		cashier.msgHereIsPayment(customer, cost);
	  //assert one check is pending, one check is paying
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);

		
		cashier.pickAndExecuteAnAction();
	  //assert waiter2 has received message from cashier
		assertTrue("MockWaiter2 should have logged \"Received msgHereIsCheck from cashier. Check = " + cost2 + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter2.log.getLastLoggedEvent().toString(),
				waiter2.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost2));		
	  //assert check one is paying, check two is computed
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
			
			
		cashier.msgHereIsPayment(customer2, 0);
	  //assert both checks are paying
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert customer has received message from cashier
		assertTrue("MockCustomer should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));		
	  //assert check one is finished, check two is paying
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);		
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost) + ". It doesn't.",
				cashier.money,baseMoney+cost);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert customer2 has received message from cashier
		assertTrue("MockCustomer2 should have logged \"Received msgPayNextTime from cashier.\" but didn't."
				+ "Instead his log reads: "
				+ customer2.log.getLastLoggedEvent().toString(),
				customer2.log.containsString("Received msgPayNextTime from cashier."));
	  //assert both checks are finished
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
		assertEquals("Check should be unfulfilled It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Unfulfilled);		
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost) + ". It doesn't.",
				cashier.money,baseMoney+cost);
	}
	
	/**
	 * Tests a full scenario with one market order, two customers, and one waiter.
	 */
	public void testFullInteraction()
	{
		System.out.println("Full Scenario");
	  //create second customer
		MockCustomer customer2 = new MockCustomer();
	  //create test items
		String item = "Steak";
		double cost = 15.99;
		String item2 = "Salad";
		double cost2 = 5.99;
		String item3 = "mockItem";
		double cost3 = 525.60;
	  //check preconditions
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		assertEquals("Cashier should have base money. It doesn't.",cashier.money,baseMoney);
		assertEquals("Cashier should have 0 bills. It doesn't.",cashier.getNumBills(), 0);
		
		
		cashier.msgComputeCheck(waiter, customer, item);
	  //assert number of checks
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert check is pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);	
		
		
		cashier.pickAndExecuteAnAction();	//compute bill
	  //assert number of checks
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert check is computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		
		
		cashier.msgComputeCheck(waiter, customer2, item2);
	  //assert number of checks
		assertEquals("Cashier should have 2 checks. It doesn't.",cashier.getNumChecks(), 2);
	  //assert check one is computed, check two is pending
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
	  //assert waiter has received message from cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost));		

		
		cashier.msgHereIsBill(market, item3, cost3);
	  //assert check one is computed, check two is pending
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
	  //assert number of bills
		assertEquals("Cashier should have 1 bill. It doesn't.",cashier.getNumBills(), 1);		
	  //assert bill is pending
		assertEquals("Cashier's bill should be pending. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
			
		
		cashier.pickAndExecuteAnAction();	//compute bill
	  //assert both checks are computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);		
	  //assert waiter has received message from cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost2 + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost2));		
		
		
		cashier.msgHereIsPayment(customer2, cost2);
	  //assert check one is computed, check two is paying
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
		assertEquals("Check should be paying It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);		
		
		
		cashier.msgHereIsPayment(customer, cost);
	  //assert both checks are paying
		assertEquals("Check should be paying It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		assertEquals("Check should be paying It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);

		
		cashier.pickAndExecuteAnAction();	//cust pay bill
	  //assert check one is finished, check two is paying
		assertEquals("Check should be finished It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
		assertEquals("Check should be paying It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);		
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost) + ". It doesn't.",
				cashier.money,baseMoney+cost);
	  //assert customer received message from cashier
		assertTrue("MockCustomer should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));		
		
		
		cashier.pickAndExecuteAnAction();	//cust pay bill 2
	  //assert both checks are finished
		assertEquals("Check should be finished It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
		assertEquals("Check should be finished It isn't.",
				cashier.checks.get(1).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost+cost2) + ". It doesn't.",
				cashier.money,baseMoney+cost+cost2);
	  //assert customer2 received message from cashier
		assertTrue("MockCustomer2 should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer2.log.getLastLoggedEvent().toString(),
				customer2.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));		

		
		cashier.pickAndExecuteAnAction();	//pay market bill
	  //assert money
		assertEquals("Cashier should have " + (double)(baseMoney+cost+cost2-cost3) + ". It doesn't.",
				cashier.money,baseMoney+cost+cost2-cost3);
	  //assert bill is finished
		assertEquals("Cashier's bill should be fulfilled. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Fulfilled);
	  //assert market has received message from cashier
		assertTrue("MockMarket should have logged \"Received msgHereIsPayment from cashier."
				+ "Item = " + item3 + ". Payment = " + cost3
				+ "\" but didn't. Instead his log reads: "
				+ market.log.getLastLoggedEvent().toString(),
				market.log.containsString("Received msgHereIsPayment from cashier. "
						+ "Item = " + item3 + ". Payment = " + cost3));	
	}
	
	/**
	 * Tests if a cashier cannot fulfill a market bill.
	 */
	public void testBrokeCashier() {
		System.out.println("Bankrupt Restaurant");
	  //set money
		cashier.setMoney(0.0);
	  //create test items
		String item = "Steak";
		double cost = 15.99;
		String item2 = "mockItem";
		double cost2 = 10;
	  //check preconditions
		System.out.println(cashier.money);
		assertEquals("Cashier should have 0 checks. It doesn't.",cashier.getNumChecks(), 0);
		assertEquals("Cashier should have no money. It doesn't.",cashier.money,0.0);
		assertEquals("Cashier should have 0 bills. It doesn't.",cashier.getNumBills(), 0);
		
		
		cashier.msgHereIsBill(market, item2, cost2);
	  //assert number of bills
		assertEquals("Cashier should have 1 bill. It doesn't.",
				cashier.getNumBills(),1);
	  //assert bill is pending
		assertEquals("Cashier's bill should be pending. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Pending);
		
		
		cashier.pickAndExecuteAnAction();
	  //assert bill is outstanding
		assertEquals("Cashier's bill should be outstanding. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Outstanding);
	  //assert market received message from cashier
		assertTrue("MockMarket should have logged \"Received msgWillPaySoon from cashier. "
				+ "Item = " + item2 + ". Payment = " + cost2
				+ "\" but didn't. Instead his log reads: "
				+ market.log.getLastLoggedEvent().toString(),
				market.log.containsString("Received msgWillPaySoon from cashier. "
						+ "Item = " + item2 + ". Payment = " + cost2));
		
		
		cashier.msgComputeCheck(waiter, customer, item);
	  //assert there is one check
		assertEquals("Cashier should have 1 check. It doesn't.",cashier.getNumChecks(), 1);
	  //assert check is pending
		assertEquals("Check should be pending. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Pending);
			
			
		cashier.pickAndExecuteAnAction();
	  //assert waiter has received message from the cashier
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck from cashier. Check = " + cost + "\" but didn't."
				+ "Instead his log reads: "
				+ waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsCheck from cashier. Check = " + cost));
	  //assert check is computed
		assertEquals("Check should be computed. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Computed);
			
			
		cashier.msgHereIsPayment(customer, cost);
	  //assert check is paying
		assertEquals("Check should be paying. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Paying);
		
			
		cashier.pickAndExecuteAnAction();
	  //assert customer has received message from the cashier
		assertTrue("MockCustomer should have logged \"Received msgHereIsChange from cashier. Change = 0.0\" but didn't."
				+ "Instead his log reads: "
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = 0.0"));
	  //assert check is finished
		assertEquals("Check should be finished. It isn't.",
				cashier.checks.get(0).getStatus(),RestaurantCashierRole_at.CheckStatus.Finished);
	  //assert money
		assertEquals("Cashier should have " + (double)(cost) + ". It doesn't.",
				cashier.money,cost);
		

		
		cashier.pickAndExecuteAnAction();
	  //assert bill is finished
		assertEquals("Cashier's first bill should be fulfilled. It isn't.",
				cashier.bills.get(0).getStatus(),RestaurantCashierRole_at.BillStatus.Fulfilled);
	  //assert money
		assertEquals("Cashier should have " + (double)(cost-cost2) + ". It doesn't.",
				cashier.money,cost-cost2);
	  //assert market has received message
		assertTrue("MockMarket should have logged \"Received msgHereIsPayment from cashier."
				+ "Item = " + item2 + ". Payment = " + cost2
				+ "\" but didn't. Instead his log reads: "
				+ market.log.getLastLoggedEvent().toString(),
				market.log.containsString("Received msgHereIsPayment from cashier. "
						+ "Item = " + item2 + ". Payment = " + cost2));

	}
}
