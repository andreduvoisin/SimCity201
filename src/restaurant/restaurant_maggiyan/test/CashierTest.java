package restaurant.restaurant_maggiyan.test;

import java.util.ArrayList;
import java.util.List;

import restaurant.restaurant_maggiyan.roles.MaggiyanCashierRole;
import restaurant.restaurant_maggiyan.test.mock.MockCustomer;
import restaurant.restaurant_maggiyan.test.mock.MockMarket;
import restaurant.restaurant_maggiyan.test.mock.MockWaiter;
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
	MaggiyanCashierRole cashier;
	MockWaiter waiter;
	MockWaiter waiter1; 
	MockWaiter waiter2;
	MockCustomer customer; 
	MockCustomer customer1;
	MockCustomer customer2; 
	MockCustomer customer3; 
	MockCustomer customer4;
	MockCustomer customer5; 
	MockMarket market;
	MockMarket market1;
	MockMarket market2;
	MockMarket market3; 
	MockMarket market4; 
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new MaggiyanCashierRole("Mockcashier", true);	
		customer = new MockCustomer("Mockcustomer");
		customer1 = new MockCustomer("Mockcustomer1");
		customer2 = new MockCustomer("Mockcustomer2");	
		customer3 = new MockCustomer("Mockcustomer3");
		customer4 = new MockCustomer("Mockcustomer4");	
		customer5 = new MockCustomer("Mockcustomer5");
		waiter = new MockWaiter("Mockwaiter");
		waiter1 = new MockWaiter("Mockwaiter1");
		waiter2 = new MockWaiter("Mockwaiter2");
		market = new MockMarket("Mockmarket");
		market1 = new MockMarket("Mockmarket1");
		market2 = new MockMarket("Mockmarket2"); 
		market3 = new MockMarket("Mockmarket3");
		market4 = new MockMarket("Mockmarket4");
	}	
	
	public void testOneCashierCalculatesCorrectCheckForWaiter()
	{
		/**
		 * Test is set up to check that the cashier properly calculates check and then sends it back to the waiter 
		 */
	
		waiter.cashier = cashier;//You can do almost anything in a unit test.	
		
		cashier.msgPleaseCalculateBill(waiter, customer, "Steak");
		assertEquals("Waiter should not have received any messages", waiter.log.size(), 0); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Waiter should have received message", waiter.log.containsString("Received msgHereIsBill of: 15.99"));
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		assertEquals("Waiter should have received message", waiter.log.size(), 1); 
		assertEquals("Cashier should have one check waiting to be paid", cashier.getChecksSize(), 1); 
		assertEquals("MockCustomer should not have received any messages", customer.log.size(), 0); 
	}
	
	public void testTwoCashierReceivesAndPaysOneMarketInFull(){
		/**
		 * This test checks that the market gives the cashier the order and then the cashier calculates the bill
		 */
		List<String> bill = new ArrayList<String>();
		bill.add("Steak");
		bill.add("Steak");
		bill.add("Salad"); 
		
		cashier.msgDeliverBill(market, bill); 
		assertEquals("Cashier should receive bill and create MarketPayment to be calculated and paid", cashier.getMarketPaymentSize(), 1);
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Market should have received message", market.log.containsString("Received msgHereIsPayment of: 37.97")); 
		assertEquals("Cashier should have removed MarketPayment from marketpayments after making payment", cashier.getMarketPaymentSize(), 0);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		assertEquals("Market should have received only one message", market.log.size(), 1); 
		assertEquals("MockCustomer should not have received any messages", customer.log.size(), 0); 
	}
	
	public void testThreeCashierReceivesAndPaysTwoMarketsInFull(){
		/**
		 * This test checks that the cashier receives bill from two markets and pays both in full.
		 */
		List<String> bill1 = new ArrayList<String>();
		bill1.add("Steak");
		bill1.add("Chicken");
		bill1.add("Salad"); 
		
		List<String> bill2 = new ArrayList<String>();
		bill2.add("Pizza");
		bill2.add("Steak");
		bill2.add("Salad"); 
		
		cashier.msgDeliverBill(market1, bill1);
		assertEquals("Cashier should receive bill from Market1 and create MarketPayment to be calculated and paid", cashier.getMarketPaymentSize(), 1);
		cashier.msgDeliverBill(market2, bill2);
		assertEquals("Cashier should receive bill from Market2 and create MarketPayment to be calculated and paid", cashier.getMarketPaymentSize(), 2);
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Market1 should have received message", market1.log.containsString("Received msgHereIsPayment of: 32.97")); 
		assertEquals("Cashier should have removed MarketPayment from marketpayments after making payment", cashier.getMarketPaymentSize(), 1);
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Market2 should have received message", market2.log.containsString("Received msgHereIsPayment of: 30.97")); 
		assertEquals("Cashier should have removed MarketPayment from marketpayments after making payment", cashier.getMarketPaymentSize(), 0);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		assertEquals("Market1 should have received only one message", market1.log.size(), 1); 
		assertEquals("Market2 should have received only one message", market2.log.size(), 1); 
		assertEquals("MockCustomer should not have received any messages", customer.log.size(), 0); 
		
		
	}
	
	public void testFourCashierReceivesAndCompletesTwoCustomerPayments(){
		/**
		 * This tests that the cashier appropriately handles two customers coming to pay their bill
		 */
		
		cashier.msgPleaseCalculateBill(waiter, customer1, "Steak");
		cashier.msgPleaseCalculateBill(waiter, customer2, "Salad");
		
		cashier.msgHereIsPayment(customer1, 15.99);
		assertEquals("Cashier should create and add Payment to payments", cashier.getPaymentsSize(), 1); 
		cashier.msgHereIsPayment(customer2, 5.99);
		assertEquals("Cashier should create and add Payment to payments", cashier.getPaymentsSize(), 2); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should add payment amount to his total money", cashier.getTotalMoney(), 515.99);
		assertEquals("Cashier should remove payment after transaction", cashier.getPaymentsSize(), 1);
		assertEquals("Cashier should remove check after transaction", cashier.getChecksSize(), 1);
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should add payment amount to his total money", cashier.getTotalMoney(), 521.98);
		assertEquals("Cashier should remove payment after transaction", cashier.getPaymentsSize(), 0);
		assertEquals("Cashier should remove check after transaction", cashier.getChecksSize(), 0);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		
	}
	
	public void testFiveNormativeScenario(){
		/**
		 * This tests that the cashier takes care of transactions for calculating bill for waiter, customer payment, and market bill in full
		 */
		List<String> bill = new ArrayList<String>();
		bill.add("Steak");
		bill.add("Steak");
		bill.add("Salad"); 
		
		cashier.msgPleaseCalculateBill(waiter1, customer3, "Steak");
		assertEquals("Waiter should not have received any messages", waiter1.log.size(), 0); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Waiter should have received message", waiter1.log.containsString("Received msgHereIsBill of: 15.99"));
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		assertEquals("Waiter should have received message", waiter1.log.size(), 1); 
		assertEquals("Cashier should have one check waiting to be paid", cashier.getChecksSize(), 1);  
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsPayment(customer3, 15.99);
		assertEquals("Cashier should create and add Payment to payments", cashier.getPaymentsSize(), 1); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should add payment amount to his total money", cashier.getTotalMoney(), 515.99);
		assertEquals("Cashier should remove payment after transaction", cashier.getPaymentsSize(), 0);
		assertEquals("Cashier should remove check after transaction", cashier.getChecksSize(), 0);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		
		cashier.msgDeliverBill(market3, bill); 
		assertEquals("Cashier should receive bill and create MarketPayment to be calculated and paid", cashier.getMarketPaymentSize(), 1);
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Market should have received message", market3.log.containsString("Received msgHereIsPayment of: 37.97")); 
		assertEquals("Cashier should deduct payment amount from his total money", cashier.getTotalMoney(), 478.02);
		assertEquals("Cashier should have removed MarketPayment from marketpayments after making payment", cashier.getMarketPaymentSize(), 0);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		
		assertEquals("Market should have received only one message", market3.log.size(), 1); 
		assertEquals("Waiter should have received message", waiter1.log.size(), 1); 
		
	}
	
	public void testSixNormalScenario(){
		/**
		 * This tests that the cashier receives two checks, pays market bill, then completes one customer transaction, leaving one check from a different customer who has not yet paid
		 */
		List<String> bill = new ArrayList<String>();
		bill.add("Steak");
		bill.add("Chicken");
		bill.add("Salad"); 
		
		cashier.msgPleaseCalculateBill(waiter2, customer4, "Salad");
		cashier.msgPleaseCalculateBill(waiter2, customer5, "Steak");
		assertEquals("Waiter should not have received any messages", waiter1.log.size(), 0); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Waiter should have received message", waiter2.log.containsString("Received msgHereIsBill of: 5.99"));
		assertEquals("Waiter should have received message", waiter2.log.size(), 1); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Waiter should have received message", waiter2.log.containsString("Received msgHereIsBill of: 15.99"));
		assertEquals("Waiter should have received message", waiter2.log.size(), 2); 
		assertEquals("Cashier should have two checks waiting to be paid", cashier.getChecksSize(), 2);  
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
	
		cashier.msgDeliverBill(market4, bill); 
		assertEquals("Cashier should receive bill and create MarketPayment to be calculated and paid", cashier.getMarketPaymentSize(), 1);
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertTrue("Market should have received message", market4.log.containsString("Received msgHereIsPayment of: 32.97")); 
		assertEquals("Cashier should deduct payment amount from his total money", cashier.getTotalMoney(), 467.03);
		assertEquals("Cashier should have removed MarketPayment from marketpayments after making payment", cashier.getMarketPaymentSize(), 0);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		assertEquals("Market should have received only one message", market4.log.size(), 1); 
		assertEquals("Cashier should still have two checks waiting to be paid", cashier.getChecksSize(), 2);  
		
		cashier.msgHereIsPayment(customer4, 5.99);
		assertEquals("Cashier should create and add Payment to payments", cashier.getPaymentsSize(), 1); 
		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should add payment amount to his total money", cashier.getTotalMoney(), 473.02);
		assertEquals("Cashier should remove payment after transaction", cashier.getPaymentsSize(), 0);
		assertEquals("Cashier should remove check after transaction", cashier.getChecksSize(), 1);
		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
		
	}
}

