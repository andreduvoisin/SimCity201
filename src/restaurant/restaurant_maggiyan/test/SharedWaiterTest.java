package restaurant.restaurant_maggiyan.test;

import base.PersonAgent;
import junit.framework.TestCase;
import restaurant.restaurant_maggiyan.gui.MaggiyanWaiterGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.test.mock.MockCook;
import restaurant.restaurant_maggiyan.test.mock.MockCustomer;

/**
 * Tests MaggiyanSharedWaiter
 */
public class SharedWaiterTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.

	MaggiyanSharedWaiterRole sharedWaiter; 
	MaggiyanCustomer customer1;
	MaggiyanCustomer customer2; 
	MockCook cook; 
	
	
	public void setUp() throws Exception{
		super.setUp();	
		
		PersonAgent p = new PersonAgent(); 
		sharedWaiter = new MaggiyanSharedWaiterRole(p); 
		customer1 = new MockCustomer("Customer 1"); 
		customer2 = new MockCustomer("Customer 2"); 
		cook = new MockCook("Cook"); 
		
		MaggiyanWaiterGui w = null; 
		sharedWaiter.setGui(w); 
		((MockCustomer) customer1).waiter = sharedWaiter;  
	}	
	
	public void testOneSharedWaiter()
	{
		/**
		 * Tests that shared waiter can successfully complete customer order shared waiter style 
		 */
		
		//PRECONDITIONS
		sharedWaiter.msgPleaseSeatCustomer(customer1, 1); 
		sharedWaiter.msgReadyToOrder(customer1);
		assertEquals("Waiter should have one customer", sharedWaiter.customers.size(), 1);
		assertTrue("Cook has received no messages", cook.log.size() == 0); 
		
		//CUSTOMER GIVES ORDER
		//((MaggiyanWaiter) sharedWaiter).msgHereIsMyChoice("STEAK", customer1); 
		
		//CHECK
		assertTrue("Cook should not have received any messages", cook.log.size() == 0);
		
	}
}

