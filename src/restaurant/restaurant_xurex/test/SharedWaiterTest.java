package restaurant.restaurant_xurex.test;

import restaurant.restaurant_xurex.WaiterAgent2;
import restaurant.restaurant_xurex.test.mock.MockCook;
import restaurant.restaurant_xurex.test.mock.MockCustomer;
import restaurant.restaurant_xurex.utilities.CustomerState;
import restaurant.restaurant_xurex.utilities.WaiterState;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and a market.
 *
 *
 * @author Rex Xu
 */
public class SharedWaiterTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	WaiterAgent2 waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockCook cook;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		waiter = new WaiterAgent2("waiter");
		customer1 = new MockCustomer("customer1");
		customer2 = new MockCustomer("customer2");
		cook = new MockCook("cook");
		waiter.setCook(cook);
	}	
	
	public void testOne_OneCustomer(){
	//  setUp()
		
	//	Preconditions
		assertTrue("Waiter has no customers", waiter.customers.isEmpty());
		assertTrue("Waiter state is good", waiter.state == WaiterState.good);

	//	1: add one customer
		waiter.PleaseSeatCustomer(customer1, 2);
		
	//	Check
		assertTrue("Waiter has one customer", waiter.customers.size() == 1);
		assertTrue("Waiter state is working", waiter.state == WaiterState.working);
		assertTrue("Customer state is waiting", waiter.customers.get(0).s == CustomerState.waiting);
		
	//	2: p.a.e.a. (SeatCustomer(c))
		assertTrue("p.a.e.a. seatCustomer(c)", waiter.pickAndExecuteAnAction());
		
	//	Check
		//assertTrue("")
	}
	
}
