package base.test;

import junit.framework.TestCase;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.interfaces.Person;

public class PersonTest extends TestCase{
	
	//needed interfaces
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
			
		//clear logs
	}
	
	//TESTS
	
	public void testOne_OneBillOneMarket(){
		//setUp()
		
//		//1 msgMarketBill
//		//preconditions
//		assertEquals("CashierAgent log should be empty: ", 0, Cashier.log.size());
//		assertEquals("Market log should be empty: ", 0, Market.log.size());
//		assertEquals("mMarketBills should have nothing in it", 0, cashier.getMarketBills().size());
//		//msgMarketBill
//		bill = 10;
//		cashier.msgMarketBill(market1, bill);
//		//postconditions
//		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
//		assertTrue("bill amount should be bill", bill == cashier.getMarketBills().get(market1));
//		
//		//2 PAEA - Market Payment
//		//preconditions
//		assertEquals("market paying should be correct market", market1, cashier.getMarketBills().keySet().toArray()[0]);
//		assertEquals("Cash should be correct", CashierAgent.cRESTAURANT_CASH, cashier.getCash());
//		assertEquals("Market log should be 0", 0, Market.log.size());
//		assertEquals("mMarketBills should have 1 market", 1, cashier.getMarketBills().size());
//		//payMarket
//		assertTrue("PAEA should return true", cashier.pickAndExecuteAnAction());
//		//postconditions
//		assertTrue("PAEA should return false now", !(cashier.pickAndExecuteAnAction()));
//		assertEquals("Cash should be 50 now", CashierAgent.cRESTAURANT_CASH - 10, cashier.getCash());
//		assertEquals("Market log should be 1 now", 1, Market.log.size());
//		assertEquals("mMarketBills should have no markets", 0, cashier.getMarketBills().size());

	}
	
	
	private void print(String message){
		System.out.println("[PersonTest] " + message);
	}
}

