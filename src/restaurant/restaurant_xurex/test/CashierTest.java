package restaurant.restaurant_xurex.test;

//import restaurant.interfaces.Customer;
//import restaurant.interfaces.Waiter;
import junit.framework.TestCase;
import restaurant.restaurant_xurex.agents.CashierAgent;
import restaurant.restaurant_xurex.agents.CashierAgent.BillState;
import restaurant.restaurant_xurex.test.mock.MockCustomer;
import restaurant.restaurant_xurex.test.mock.MockMarket;
import restaurant.restaurant_xurex.test.mock.MockWaiter;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and a market.
 *
 *
 * @author Rex Xu
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter1;
	MockWaiter waiter2;
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
		cashier = new CashierAgent("cashier");		
		customer1 = new MockCustomer("mockcustomer1");
		customer2 = new MockCustomer("mockcustomer2");
		waiter1 = new MockWaiter("mockwaiter1");
		waiter2 = new MockWaiter("mockwaiter2");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
	}	
	
	public void testOneNormalCustomerScenario(){
		// setUp() runs first //
		customer1.cashier = cashier;
		customer1.SetChoice("Pizza");
		
		// PRECONDITIONS //
		assertEquals("Cashier should not have bills. False.", cashier.bills.size(), 0);
		assertTrue("Customer choice should be pizza. False.", customer1.getChoice().equals("Pizza"));
		
		// STEP 1 //
		cashier.ComputeBill(waiter1, customer1);
		
		// PRE 2 //
		assertTrue("Cashier should contain bill with state == pendingWaiter. False.",
				cashier.bills.get(customer1.getName()).state == BillState.pendingWaiter);
		assertTrue("Cashier should contain bill with order Pizza.", cashier.bills.get(customer1.getName()).order.equals("Pizza"));		

		// STEP 2 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
		
		// PRE 3 //
		assertEquals("MockCustomer empty event log. False.", 0, customer1.log.size());
		assertTrue("MockWaiter should have logged HereIsBill. False.", waiter1.log.containsString("HereIsBill: 9.0"));
		
		// STEP 3 //
		cashier.IWantToPay(customer1, 20);
	
		// PRE 4 //
		assertTrue("Cashier should contain bill with state == pendingCustomer. False.",
						cashier.bills.get(customer1.getName()).state == BillState.pendingCustomer);
		assertTrue("Cashier should contain bill of paid 20. It contains something else instead: $" 
						+ cashier.bills.get(customer1.getName()).paid, cashier.bills.get(customer1.getName()).paid == 20);		
		
		// STEP 4 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
				
		// PRE 5 //
		assertTrue("MockCustomer should have logged HereIsChange, but his last event logged reads instead: " 
		+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("HereIsChange: 11.0"));
	}
	
	public void testTwoMultipleCustomerScenario(){
		// setUp() runs first //
		customer1.cashier = cashier;
		customer2.cashier = cashier;
		customer1.SetChoice("Pizza");
		customer2.SetChoice("Salad");
				
		// PRECONDITIONS //
		assertEquals("Cashier should not have bills. False.", cashier.bills.size(), 0);
		assertTrue("Customer choice should be pizza. False.", customer1.getChoice().equals("Pizza"));
		assertTrue("Customer choice should be salad. False.", customer2.getChoice().equals("Salad"));
				
		// STEP 1 //
		cashier.ComputeBill(waiter1, customer1);
		cashier.ComputeBill(waiter2, customer2);
		
		// PRE 2 //
		assertTrue("Cashier should contain bill with state == pendingWaiter. False.",
				cashier.bills.get(customer1.getName()).state == BillState.pendingWaiter);
		assertTrue("Cashier should contain bill with state == pendingWaiter. False.",
				cashier.bills.get(customer2.getName()).state == BillState.pendingWaiter);
		assertTrue("Cashier should contain bill with order Pizza.", cashier.bills.get(customer1.getName()).order.equals("Pizza"));
		assertTrue("Cashier should contain bill with order Pizza.", cashier.bills.get(customer2.getName()).order.equals("Salad"));

		// STEP 2 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
		
		// PRE 3 //
		assertEquals("MockCustomer empty event log. False.", 0, customer1.log.size());
		assertTrue("MockWaiter should have logged HereIsBill. False.", waiter1.log.containsString("HereIsBill: 9.0"));
		assertEquals("MockCustomer empty event log. False.", 0, customer2.log.size());
		assertTrue("MockWaiter should have logged HereIsBill. False.", waiter2.log.containsString("HereIsBill: 6.0"));
		
		// STEP 3 //
		cashier.IWantToPay(customer1, 10);
		cashier.IWantToPay(customer2, 10);
	
		// PRE 4 //
		assertTrue("Cashier should contain bill with state == pendingCustomer. False.",
						cashier.bills.get(customer1.getName()).state == BillState.pendingCustomer);
		assertTrue("Cashier should contain bill of paid 10. It contains something else instead: $" 
						+ cashier.bills.get(customer1.getName()).paid, cashier.bills.get(customer1.getName()).paid == 10);
		assertTrue("Cashier should contain bill with state == pendingCustomer. False.",
				cashier.bills.get(customer2.getName()).state == BillState.pendingCustomer);
		assertTrue("Cashier should contain bill of paid 10. It contains something else instead: $" 
				+ cashier.bills.get(customer2.getName()).paid, cashier.bills.get(customer2.getName()).paid == 10);
		
		// STEP 4 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
				
		// PRE 5 //
		assertTrue("MockCustomer should have logged HereIsChange, but his last event logged reads instead: " 
		+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("HereIsChange: 1.0"));
		assertTrue("MockCustomer should have logged HereIsChange, but his last event logged reads instead: " 
				+ customer2.log.getLastLoggedEvent().toString(), customer2.log.containsString("HereIsChange: 4.0"));
	}
	
	public void testThreePoorCustomerScenario(){
		// setUp() runs first //
		customer1.cashier = cashier;
		customer1.SetChoice("Pizza");
		
		// PRECONDITIONS //
		assertEquals("Cashier should not have bills. False.", cashier.bills.size(), 0);
		assertTrue("Customer choice should be pizza. False.", customer1.getChoice().equals("Pizza"));
		
		// STEP 1 //
		cashier.ComputeBill(waiter1, customer1);
		
		// PRE 2 //
		assertTrue("Cashier should contain bill with state == pendingWaiter. False.",
				cashier.bills.get(customer1.getName()).state == BillState.pendingWaiter);
		assertTrue("Cashier should contain bill with order Pizza.", cashier.bills.get(customer1.getName()).order.equals("Pizza"));		

		// STEP 2 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
		
		// PRE 3 //
		assertEquals("MockCustomer empty event log. False.", 0, customer1.log.size());
		assertTrue("MockWaiter should have logged HereIsBill. False.", waiter1.log.containsString("HereIsBill: 9.0"));
		
		// STEP 3 //
		cashier.IWantToPay(customer1, 5);
	
		// PRE 4 //
		assertTrue("Cashier should contain bill with state == pendingCustomer. False.",
						cashier.bills.get(customer1.getName()).state == BillState.pendingCustomer);
		assertTrue("Cashier should contain bill of paid 5. It contains something else instead: $" 
						+ cashier.bills.get(customer1.getName()).paid, cashier.bills.get(customer1.getName()).paid == 5);		
		
		// STEP 4 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
				
		// PRE 5 //
		assertTrue("MockCustomer should have logged HereIsChange, but his last event logged reads instead: " 
		+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("HereIsChange: 0"));
		assertTrue("Cashier should contain bill with state == ignore. False.",
						cashier.bills.get(customer1.getName()).state == BillState.ignore);
		assertTrue("Cashier should contain bill with due 4. False.",
						cashier.bills.get(customer1.getName()).due == 4);
	}
	
	/* This scenario involves a returning customer
	 * that has not paid his bill in full. His total
	 * bill is the sum of his current bill and his debt. 
	 */
	public void testFourReturnCustomerScenario(){
		//setUp() runs first//
		testThreePoorCustomerScenario();
		
		customer1.SetChoice("Salad");
		
		// PRECONDITIONS //
		assertEquals("Cashier should have 1 bill. False.", cashier.bills.size(), 1);
		assertTrue("Customer choice should be salad. False.", customer1.getChoice().equals("Salad"));
		
		// STEP 1 //
		cashier.ComputeBill(waiter1, customer1);
		
		// PRE 2 //
		assertTrue("Cashier should contain bill with state == pendingWaiter. False.",
				cashier.bills.get(customer1.getName()).state == BillState.pendingWaiter);
		assertTrue("Cashier should contain bill with order Salad.", cashier.bills.get(customer1.getName()).order.equals("Salad"));
		assertTrue("Cashier should contain bill with due 4. False.",
				cashier.bills.get(customer1.getName()).due == 4);

		// STEP 2 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
		
		// PRE 3 //
		assertTrue("MockCustomer should have logged HereIsChange, but his last event logged reads instead: " 
		+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("HereIsChange: 0.0"));
		assertTrue("MockWaiter should have logged HereIsBill. False.", waiter1.log.containsString("HereIsBill: 10.0"));
		
		// STEP 3 //
		cashier.IWantToPay(customer1, 20);
	
		// PRE 4 //
		assertTrue("Cashier should contain bill with state == pendingCustomer. False.",
						cashier.bills.get(customer1.getName()).state == BillState.pendingCustomer);
		assertTrue("Cashier should contain bill of paid 20. It contains something else instead: $" 
						+ cashier.bills.get(customer1.getName()).paid, cashier.bills.get(customer1.getName()).paid == 20);		
		
		// STEP 4 //
		assertTrue("Cashier's scheduler should have returned true. False.", cashier.pickAndExecuteAnAction());
				
		// PRE 5 //
		assertTrue("MockCustomer should have logged HereIsChange, but his last event logged reads instead: " 
		+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("HereIsChange: 10.0"));
		assertTrue("Cashier should have bill with due 0.0. False.",
						cashier.bills.get(customer1.getName()).due == 0);
	}
	
	public void testFiveNormalMarketScenario(){
		//setUp() runs first //
		
		//Preconditions//
		assertTrue("Cashier should have assets of 100. False.", cashier.getAssets() == 100);
		assertTrue("Market should have assets of 0. False.", market1.getAssets() == 0);
		
		//STEP 1//
		cashier.HereIsBill(market1, 25);
		//market.HereIsPayment(25) 
		
		//PRE//
		assertTrue("Cashier assets 100 = "+cashier.getAssets(), cashier.getAssets() == 100);
		assertTrue("Market should have assets of 25. Instead: "+market1.getAssets(), market1.getAssets() == 00);		
	}
	
	public void testSixMultipleMarketScenario(){
		//setUp() runs first //

		//Preconditions//
		assertTrue("Cashier should have assets of 100. False.", cashier.getAssets() == 100);
		assertTrue("Market should have assets of 0. False.", market1.getAssets() == 0);
		assertTrue("Market should have assets of 0. False.", market2.getAssets() == 0);
		
		//STEP 1//
		cashier.HereIsBill(market1, 25);
		//market.HereIsPayment(25) 
		
		//PRE//
		assertTrue("Cashier should have assets of 75. False.", cashier.getAssets() == 100);
		assertTrue("Market should have assets of 25. Instead: "+market1.getAssets(), market1.getAssets() == 00);
		
		//STEP 2//
		cashier.HereIsBill(market2, 65);
		//market.HereIsPayment(65)
		
		//PRE//
		assertTrue("Cashier should have assets of 10. False.", cashier.getAssets() == 100);
		assertTrue("Market should have assets of 65. Instead: "+market2.getAssets(), market2.getAssets() == 0);
	}
}
