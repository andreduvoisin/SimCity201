package restaurant.restaurant_tranac.test;

import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_tranac.test.mock.TranacMockCustomer;
import restaurant.restaurant_tranac.test.mock.TranacMockWaiter;
import base.PersonAgent;
import junit.framework.TestCase;

/** Tests the two different interactions of the cook with the waiters.*/
public class CookTest extends TestCase {
	PersonAgent person;
	RestaurantCookRole cookRole;
	TranacCookRole cook;
	TranacMockWaiter waiter;
	
	TranacMockCustomer customer;
	
	public void setUp() throws Exception {
		super.setUp();
		
		person = new PersonAgent();
		cookRole = new RestaurantCookRole(person,6);
		cook = new TranacCookRole(person,cookRole);
		waiter = new TranacMockWaiter();
	}
	
	public void testRegularWaiter() { 
		//assert preconditions
		assertEquals("Cook should have no orders.", cook.orders.size(),0);
		
		cook.msgHereIsOrder(waiter,"mockOrder",0);
		assertEquals("Cook should have one order.", cook.orders.size(),1);
	}
	
	public void testRevolvingStandWaiter() {
		//assert preconditions
		assertEquals("Cook should have no orders.", cook.orders.size(),0);
		
		//put order on stand
		cook.addOrderToStand(waiter,"mockOrder",0);
		assertEquals("Cook should have no orders.", cook.orders.size(),0);
		assertEquals("Cook's stand should have one order.",cook.revolvingStand.size(),1);
		
		//assert cook took order from stand
		cook.checkStand();
		assertEquals("Cook should have one order.", cook.orders.size(),1);
	}
	
//	public void testBothWaiters() { ANGELICA
//		TranacMockWaiter waiter2;
//		
//		//assert pre
//		
//		//assert revolving stand
//		
//		//assert revolving stand
//		
//		//assert msg from waiter
//		
//		//assert cook orders
//		
//		//assert cook checks revolving stand
//		
//		//assert cook orders
//	}
}