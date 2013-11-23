package restaurant_xurex.test;

//import restaurant.interfaces.Customer;
//import restaurant.interfaces.Waiter;
import restaurant_xurex.WaiterAgent2;
import restaurant_xurex.test.mock.MockCook;
import restaurant_xurex.test.mock.MockCustomer;
import restaurant_xurex.test.mock.MockHost;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and a market.
 *
 *
 * @author Rex Xu
 */
public class Waiter2Test extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	WaiterAgent2 waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockHost host;
	MockCook cook;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		
	}	
	
	public void testOneNormalCustomerScenario(){
		// setUp() //
		
	}
	
}
