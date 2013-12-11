package restaurant.restaurant_maggiyan.test;

import base.PersonAgent;
import junit.framework.TestCase;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_maggiyan.Order;
import restaurant.restaurant_maggiyan.Order.state;
import restaurant.restaurant_maggiyan.gui.MaggiyanCookGui;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;
import restaurant.restaurant_maggiyan.test.mock.MockCashier;
import restaurant.restaurant_maggiyan.test.mock.MockCustomer;
import restaurant.restaurant_maggiyan.test.mock.MockHost;
import restaurant.restaurant_maggiyan.test.mock.MockWaiter;

/**
 * Tests MaggiyanCookRole
 */
public class CookTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.

	MaggiyanCookRole cook; 
	
	MockWaiter waiter1; 
	MockCustomer customer1;
	MockCustomer customer2;
	MockCashier cashier; 
	MockHost host; 
	
	public void setUp() throws Exception{
		super.setUp();	
		
		PersonAgent p = new PersonAgent(); 
		RestaurantCookRole cRole = new RestaurantCookRole(p, 3);  
		cook = new MaggiyanCookRole(p, cRole);
		waiter1 = new MockWaiter("Waiter 1");
		customer1 = new MockCustomer("Customer 1"); 
		customer2 = new MockCustomer("Customer 2"); 
		host = new MockHost("Host"); 
		cashier = new MockCashier("Cashier"); 
		
		MaggiyanCookGui c = new MaggiyanCookGui(cook);
		cook.setGui(c);
		
	}	
	
	public void testOneCook()
	{
		/**
		 * Tests that cook can successfully process orders from revolving stand 
		 */
		
		//PRECONDITIONS
		assertTrue("Cook should not have any orders in order list", cook.orders.size() == 0); 
		
		//ORDER IS ADDED TO REVOLVING STAND
		cook.getRevolvingStand().add(new Order(waiter1, "Steak", 1));
		
		//CHECK
		assertTrue("Cook should not have any orders in order list", cook.orders.size() == 0); 
		assertTrue("Revolving stand should have one order", cook.rStandOrders.size() == 1); 
		
		//MAKE IT TIME TO CHECK REVOLVING STAND
		assertTrue("PAEA should return true and move order from revolving stand to orders", cook.pickAndExecuteAnAction()); 
		assertTrue("Cook should have one order in order list", cook.orders.size() == 1); 
		assertTrue("Revolving stand should have zero orders", cook.rStandOrders.size() == 0); 
		
		assertTrue("PAEA should return true and cook order", cook.pickAndExecuteAnAction()); 
		
		//FINISH COOKING
		cook.orders.get(0).s = state.done;
		
		assertTrue("PAEA should return true and plate order", cook.pickAndExecuteAnAction()); 
		
		//POST CONDITIONS
		assertTrue("Cook should have one order in order list still but not active", cook.orders.size() == 1); 
		assertTrue("Revolving stand should still have zero orders", cook.rStandOrders.size() == 0); 
		assertFalse("PAEA should return false because order is finished", cook.pickAndExecuteAnAction()); 
		
		
		
		
		
	}
}

