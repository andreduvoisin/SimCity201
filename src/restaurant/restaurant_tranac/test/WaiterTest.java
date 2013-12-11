package restaurant.restaurant_tranac.test;

import java.util.concurrent.Semaphore;

import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacWaiterBase;
import restaurant.restaurant_tranac.roles.TranacWaiterBase.CustomerState;
import restaurant.restaurant_tranac.roles.TranacWaiterBase.MyCustomer;
import restaurant.restaurant_tranac.roles.TranacWaiterRSRole;
import restaurant.restaurant_tranac.roles.TranacWaiterRole;
import restaurant.restaurant_tranac.test.mock.TranacMockCook;
import restaurant.restaurant_tranac.test.mock.TranacMockCustomer;
import restaurant.restaurant_tranac.test.mock.TranacMockHost;
import base.PersonAgent;
import junit.framework.TestCase;

public class WaiterTest extends TestCase {
	PersonAgent person;
	TranacWaiterBase waiter;
	TranacMockCook cook;
	TranacMockCustomer customer;
	TranacMockHost host;
	TranacRestaurant restaurant;
	
	public void setUp() throws Exception {
		super.setUp();
		restaurant = new TranacRestaurant();
		
		host = new TranacMockHost();
		
		person = new PersonAgent();
		cook = new TranacMockCook();
		customer = new TranacMockCustomer();
		
		restaurant.mHost = host;
		restaurant.mCook = cook;
	}
	
	public void testRegularWaiter() {
		waiter = new TranacWaiterRole(person);
		waiter.setCook(cook);
		//bypass animation
		waiter.inTransit = new Semaphore(1000000000,true);
	 ///assert preconditions
		
		//add customer to waiter so he has an order to send
		waiter.addCustomer(customer,0,0,CustomerState.Ordered);
		
		//send order to cook;
		waiter.pickAndExecuteAnAction();
	 //assert cook has an order
		assertTrue("Cook should have received the message.",
				cook.log.containsString("Received msgHereIsOrder."));
		
		//send msg of order to waiter
		
		//assert waiter has order
	}
	
	public void testRevolvingStandWaiter() {
		waiter = new TranacWaiterRSRole(person);
		//assert waiter has an order to send to the cook
		
		//send order to cook
		
		//assert cook has order
		
		//send msg of order to waiter
		
		//assert waiter has order
	}
}
