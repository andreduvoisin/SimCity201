package restaurant.restaurant_xurex.test;

import restaurant.restaurant_xurex.agents.CookAgent;
import restaurant.restaurant_xurex.test.mock.MockMarket;
import restaurant.restaurant_xurex.test.mock.MockWaiter;
import restaurant.restaurant_xurex.test.mock.MockWaiterGui;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CookAgent's basic interaction
 * with waiters and markets.
 *
 *
 * @author Rex Xu
 */
public class CookTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CookAgent cook;
	
	MockWaiter waiter;
	MockMarket market1;
	MockMarket market2;
	
	MockWaiterGui waiterGui;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		
	}	
	
	public void testOne_OneCustomer(){
	//  setUp()
		
	//	Preconditions
		
	}
	
}
