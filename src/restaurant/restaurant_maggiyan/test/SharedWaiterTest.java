package restaurant.restaurant_maggiyan.test;

import base.PersonAgent;
import junit.framework.TestCase;
import restaurant.restaurant_maggiyan.gui.MaggiyanWaiterGui;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.test.mock.MockCashier;
import restaurant.restaurant_maggiyan.test.mock.MockCook;
import restaurant.restaurant_maggiyan.test.mock.MockCustomer;
import restaurant.restaurant_maggiyan.test.mock.MockHost;

/**
 * Tests MaggiyanSharedWaiter
 */
public class SharedWaiterTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.

	MaggiyanSharedWaiterRole sharedWaiter; 
	MockCustomer customer1;
	MockCustomer customer2;
	MockCashier cashier; 
	MockHost host; 
	MockCook cook; 
	
	public void setUp() throws Exception{
		super.setUp();	
		
		PersonAgent p = new PersonAgent(); 
		sharedWaiter = new MaggiyanSharedWaiterRole(p); 
		customer1 = new MockCustomer("Customer 1"); 
		customer2 = new MockCustomer("Customer 2"); 
		cook = new MockCook("Cook"); 
		host = new MockHost("Host"); 
		cashier = new MockCashier("Cashier"); 
		
		MaggiyanWaiterGui w = new MaggiyanWaiterGui(sharedWaiter) ; 
		sharedWaiter.setGui(w); 
		sharedWaiter.setHost(host); 
		sharedWaiter.setCook(cook); 
		sharedWaiter.setCashier(cashier);
		customer1.waiter = sharedWaiter;  
	}	
	
	public void testOneSharedWaiter()
	{
		/**
		 * Tests that shared waiter can successfully complete customer order shared waiter style 
		 */
		
		//GUI SEMAPHORE RELEASES	
		sharedWaiter.msgAtTable(); 
		sharedWaiter.msgWaiterFree(); 
		sharedWaiter.msgReachedKitchen(); 
		sharedWaiter.msgAnimationReady(); 
		
		//PRECONDITIONS
		sharedWaiter.msgPleaseSeatCustomer(customer1, 1); 
		sharedWaiter.msgAtTable();
		assertTrue("p.a.e.a. should return true and call takeOrder ", sharedWaiter.pickAndExecuteAnAction());
		
		//GUI SEMAPHORE RELEASES	
		sharedWaiter.msgAtTable(); 
		sharedWaiter.msgWaiterFree(); 
		sharedWaiter.msgReachedKitchen(); 
		sharedWaiter.msgAnimationReady(); 
		
		//CUSTOMER NOTIFIES WAITER IS READY TO ORDER
		sharedWaiter.msgReadyToOrder(customer1);
		sharedWaiter.waitingToOrder.release();
		
		assertEquals("Waiter should have one customer", sharedWaiter.customers.size(), 1);
		assertTrue("Cook has received no messages", cook.log.size() == 0); 
		
		assertTrue("p.a.e.a. should return true and call takeOrder ", sharedWaiter.pickAndExecuteAnAction());
		
		//GUI SEMAPHORE RELEASES	
		sharedWaiter.msgAtTable(); 
		sharedWaiter.msgWaiterFree(); 
		sharedWaiter.msgReachedKitchen(); 
		sharedWaiter.msgAnimationReady();
		sharedWaiter.waitingToOrder.release();
		
		//CUSTOMER GIVES ORDER
		sharedWaiter.msgHereIsMyOrder("Steak", customer1);
		
		//GUI SEMAPHORE RELEASES	
		sharedWaiter.msgAtTable(); 
		sharedWaiter.msgWaiterFree(); 
		sharedWaiter.msgReachedKitchen(); 
		sharedWaiter.msgAnimationReady();
		sharedWaiter.waitingToOrder.release();
		
		assertTrue("p.a.e.a. should return true and call takeOrder ", sharedWaiter.pickAndExecuteAnAction());
		
		//GUI SEMAPHORE RELEASES	
		sharedWaiter.msgAtTable(); 
		sharedWaiter.msgWaiterFree(); 
		sharedWaiter.msgReachedKitchen(); 
		sharedWaiter.msgAnimationReady();
		
		//CHECK
		assertTrue("Cook should only have one log", cook.log.size() == 1); 
		assertTrue("Only log in cook should be waiter accessing the revolving stand", cook.log.containsString("Added an order to the revolving stand"));
		assertTrue("Waiter should still have customer", sharedWaiter.customers.size() == 1);
		
		//Send shared waiter orderIsReady(); 
		sharedWaiter.msgOrderDone("Steak", 1, 1);
		
		//GUI SEMAPHORE RELEASES
		sharedWaiter.msgAtTable(); 
		sharedWaiter.msgWaiterFree(); 
		sharedWaiter.msgReachedKitchen(); 
		sharedWaiter.msgAnimationReady();
		sharedWaiter.waitingToOrder.release();
		
		assertTrue("p.a.e.a. should return true and call takeOrder ", sharedWaiter.pickAndExecuteAnAction());

		
		
	}
}

