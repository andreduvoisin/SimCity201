package restaurant.test;

import junit.framework.TestCase;
import restaurant.Food;
import restaurant.Food.EnumFoodOptions;
import restaurant.Order;
import restaurant.Order.EnumOrderStatus;
import restaurant.Table;
import restaurant.agents.CashierAgent;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import agent.Check;


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
	CashierAgent cashier;
	Waiter waiter;
	Customer customer;
	Market market1;
	Market market2;
	
	int cash;
	int bill;
	Table table;
	EnumFoodOptions choice;
	Food food;
	Check check;
	Order order;
	
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		RestaurantGui gui = new RestaurantGui();
		cashier = new CashierAgent("real cashier", gui);
		customer = new MockCustomer("mock customer");		
		waiter = new MockWaiter("mock waiter");
		market1 = new MockMarket("mock market 1");
		market2 = new MockMarket("mock market 2");
		
		cash = 0;
		bill = 10;
		table = new Table(0);
		choice = EnumFoodOptions.CHICKEN;
		food = new Food(choice);
		check = new Check(customer, choice, cash);
		order = new Order(waiter, table, customer, EnumOrderStatus.DONE);
			order.mFood = food;
			order.mCheck = check;
			
		//clear logs
		Cashier.log.clear();
		Customer.log.clear();
		Waiter.log.clear();
		Market.log.clear();
	}
	
	//TESTS
	
	public void testOne_OneBillOneMarket(){
		//setUp()
		
		//1 msgMarketBill
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 10;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market1));
		
		//2 PAEA - Market Payment
		//preconditions
		assertEquals("market paying should be correct market", market1, cashier.getMarketBills().keySet().toArray()[0]);
		assertEquals("Cash should be correct", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		assertEquals("Market log should be 0", 0, Market.log.size());
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		//payMarket
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		assertEquals("Cash should be 50 now", CashierAgent.cRESTAURANT_CASH - 10, cashier.getCash());
		assertEquals("Market log should be 1 now", 1, Market.log.size());
		assertEquals("mMarketBills should have no markets", 0, cashier.getMarketBills().size());

	}
	
	public void testTwo_TwoBillsTwoMarkets(){
		//setUp()
		
		//1 msgMarketBill - market1
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 10;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market1));
		
		//2 msgMarketBill - market2
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should be 1", 1, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 20;
		cashier.msgMarketBill(market2, bill);
		//postconditions
		assertEquals("mMarketBills should have 2 markets", 2, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market2));
		
		//3 PAEA - Market Payment
		//preconditions
		assertEquals("Cash should be correct", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		assertEquals("Market log should be 0", 0, Market.log.size());
		assertEquals("mMarketBills should have 2 market", 2, cashier.getMarketBills().size());
		//payMarket
		Market market = (Market) cashier.getMarketBills().keySet().toArray()[0];
		int amount = cashier.getMarketBills().get(market);
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertEquals("Cash should be 10 or 20 lower now", CashierAgent.cRESTAURANT_CASH - amount, cashier.getCash());
		assertEquals("Market log should be 1 now", 1, Market.log.size());
		assertEquals("mMarketBills should have 1 market1", 1, cashier.getMarketBills().size());
		
		//3 PAEA - Market Payment
		//preconditions
		assertEquals("Market log should be 1", 1, Market.log.size());
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		//payMarket
		market = (Market) cashier.getMarketBills().keySet().toArray()[0];
		amount = cashier.getMarketBills().get(market);
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertEquals("Cash should be 30 lower now", CashierAgent.cRESTAURANT_CASH - 30, cashier.getCash());
		assertEquals("Market log should be 2 now", 2, Market.log.size());
		assertEquals("mMarketBills should have 0 markets", 0, cashier.getMarketBills().size());
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));

	}


	public void testThree_TwoBillsOneMarket(){
		//setUp()
		
		//1 msgMarketBill
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 10;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be 10", bill == cashier.getMarketBills().get(market1));
		
		//2 msgMarketBill
		//preconditions
		//msgMarketBill
		bill = 20;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be 30", 30 == cashier.getMarketBills().get(market1));
		
		//3 PAEA - Market Payment
		//preconditions
		assertEquals("market paying should be correct market", market1, cashier.getMarketBills().keySet().toArray()[0]);
		assertEquals("Cash should be correct", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		assertEquals("Market log should be 0", 0, Market.log.size());
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		//payMarket
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		assertEquals("Cash should be 30 now", CashierAgent.cRESTAURANT_CASH - 30, cashier.getCash());
		assertEquals("Market log should be 1 now", 1, Market.log.size());
		assertEquals("mMarketBills should have no markets", 0, cashier.getMarketBills().size());

	}
	
	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testFour_NormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		//1 msgMakeCheck
		//preconditions
		assertEquals("Cashier name should be real cashier", "real cashier", cashier.getName());
		assertEquals("mOrders size should be 0", 0, cashier.getOrders().size());
		assertEquals("mChecksPaid size should be 0", 0, cashier.getChecksPaid().size());
		assertEquals("mCash should be an int", 0, cashier.getCash()%1);
		assertEquals("mMarketBills size should be 0", 0, cashier.getMarketBills().size());
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		//msgMakeCheck
		cashier.msgMakeCheck(order);
		//postconditions
		assertTrue(cashier.getOrders().size() == 1);
		assertEquals(cashier.getOrders().get(0).mCash, cash);
		assertEquals(cashier.getOrders().get(0).mCheck, check);
		assertEquals("mOrders size should be 1", 1, cashier.getOrders().size());

		
		//2 PAEA - makeCheck
		//preconditions
		//PAEA
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertEquals("Waiter log should be one", 1, Waiter.log.size());
		assertEquals("mOrders size should be 0", 0, cashier.getOrders().size());
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		
		//3 msgPayingCheck
		//preconditions
		assertEquals("mChecksPaid size should be 0", 0, cashier.getChecksPaid().size());
		//msgPayingCheck
		cashier.msgPayingCheck(check);
		//postconditions
		assertEquals("mChecksPaid size should be 1", 1, cashier.getChecksPaid().size());
		assertEquals(cashier.getChecksPaid().get(0), check);
		
		//4 PAEA - giveChange
		//preconditions
		assertEquals("Customer should be check.mCustomer", check.mCustomer, customer);
		assertEquals("Customer log should be 0", 0, Customer.log.size());
		assertEquals("mCash should be 60", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		//PAEA
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//4 postconditions
		assertEquals("mCash should be 70", CashierAgent.cRESTAURANT_CASH + 10, cashier.getCash());
		assertEquals("Customer log should be 1", 1, Customer.log.size());
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		
	}
	
	public void testFive_OneBillOneMarketNotEnoughMoney(){
		//setUp()
		cashier.setCash(0);
		
		//1 msgMarketBill
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 10;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market1));
		
		//2 PAEA - Market Payment
		//preconditions
		assertEquals("market paying should be correct market", market1, cashier.getMarketBills().keySet().toArray()[0]);
		assertEquals("Cash should be correct", 0, cashier.getCash());
		assertEquals("Market log should be 0", 0, Market.log.size());
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		//payMarket
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		assertEquals("Cash should be -10 now", -10, cashier.getCash());
		assertEquals("Market log should be 1 now", 1, Market.log.size());
		assertEquals("mMarketBills should have no markets", 0, cashier.getMarketBills().size());

	}
	
	public void testSix_TwoBillsTwoMarketsNotEnoughMoney(){
		//setUp()
		
		//1 msgMarketBill - market1
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 10;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market1));
		
		//2 msgMarketBill - market2
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should be 1", 1, cashier.getMarketBills().size());
		//msgMarketBill
		bill = CashierAgent.cRESTAURANT_CASH;
		cashier.msgMarketBill(market2, bill);
		//postconditions
		assertEquals("mMarketBills should have 2 markets", 2, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market2));
		
		//3 PAEA - Market Payment
		//preconditions
		assertEquals("Cash should be correct", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		assertEquals("Market log should be 0", 0, Market.log.size());
		assertEquals("mMarketBills should have 2 market", 2, cashier.getMarketBills().size());
		//payMarket
		Market market = (Market) cashier.getMarketBills().keySet().toArray()[0];
		int amount = cashier.getMarketBills().get(market);
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertEquals("Cash should be 90 or 0 now", CashierAgent.cRESTAURANT_CASH-amount, cashier.getCash());
		assertEquals("Market log should be 1 now", 1, Market.log.size());
		assertEquals("mMarketBills should have 1 market1", 1, cashier.getMarketBills().size());
		
		//3 PAEA - Market Payment
		//preconditions
		assertEquals("Market log should be 1", 1, Market.log.size());
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		//payMarket
		market = (Market) cashier.getMarketBills().keySet().toArray()[0];
		amount = cashier.getMarketBills().get(market);
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertEquals("Cash should be -10 now", -10, cashier.getCash());
		assertEquals("Market log should be 2 now", 2, Market.log.size());
		assertEquals("mMarketBills should have 0 markets", 0, cashier.getMarketBills().size());
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));

	}
	
	public void testSeven_FullCustomerMarketScenario()
	{
		//setUp() runs first before this test!
		
		//1 msgMakeCheck
		//preconditions
		assertEquals("Cashier name should be real cashier", "real cashier", cashier.getName());
		assertEquals("mOrders size should be 0", 0, cashier.getOrders().size());
		assertEquals("mChecksPaid size should be 0", 0, cashier.getChecksPaid().size());
		assertEquals("mCash should be an int", 0, cashier.getCash()%1);
		assertEquals("mMarketBills size should be 0", 0, cashier.getMarketBills().size());
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		//msgMakeCheck
		cashier.msgMakeCheck(order);
		//postconditions
		assertTrue(cashier.getOrders().size() == 1);
		assertEquals(cashier.getOrders().get(0).mCash, cash);
		assertEquals(cashier.getOrders().get(0).mCheck, check);
		assertEquals("mOrders size should be 1", 1, cashier.getOrders().size());

		
		//2 PAEA - makeCheck
		//preconditions
		//PAEA
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertEquals("Waiter log should be one", 1, Waiter.log.size());
		assertEquals("mOrders size should be 0", 0, cashier.getOrders().size());
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		
		//3 msgPayingCheck
		//preconditions
		assertEquals("mChecksPaid size should be 0", 0, cashier.getChecksPaid().size());
		//msgPayingCheck
		cashier.msgPayingCheck(check);
		//postconditions
		assertEquals("mChecksPaid size should be 1", 1, cashier.getChecksPaid().size());
		assertEquals(cashier.getChecksPaid().get(0), check);
		
		//4 PAEA - giveChange
		//preconditions
		assertEquals("Customer should be check.mCustomer", check.mCustomer, customer);
		assertEquals("Customer log should be 0", 0, Customer.log.size());
		assertEquals("mCash should be 60", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		//PAEA
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//4 postconditions
		assertEquals("mCash should be 70", CashierAgent.cRESTAURANT_CASH + 10, cashier.getCash());
		assertEquals("Customer log should be 1", 1, Customer.log.size());
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		
		
		//4 msgMarketBill
		//preconditions
		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
		assertEquals("Market log should be empty: ", 0, Market.log.size());
		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
		//msgMarketBill
		bill = 10;
		cashier.msgMarketBill(market1, bill);
		//postconditions
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market1));
		
		//5 PAEA - Market Payment
		//preconditions
		assertEquals("market paying should be correct market", market1, cashier.getMarketBills().keySet().toArray()[0]);
		assertEquals("Cash should be correct", CashierAgent.cRESTAURANT_CASH + 10, cashier.getCash());
		assertEquals("Market log should be 0", 0, Market.log.size());
		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
		//payMarket
		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
		//postconditions
		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
		assertEquals("Cash should be 50 now", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
		assertEquals("Market log should be 1 now", 1, Market.log.size());
		assertEquals("mMarketBills should have no markets", 0, cashier.getMarketBills().size());
		
	}
	
	private void print(String message){
		System.out.println("[CashierTest] " + message);
	}
}
