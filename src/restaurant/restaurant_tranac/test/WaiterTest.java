package restaurant.restaurant_tranac.test;

import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacWaiterBase;
import restaurant.restaurant_tranac.roles.TranacWaiterBase.CustomerState;
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
	}
	
	public void testRegularWaiter() {
		waiter = new TranacWaiterRole(person);
		waiter.setCook(cook);
		//bypass animation
		waiter.msgAnimationDone();
		waiter.msgAnimationDone();
		waiter.msgAnimationDone();
		
		//add customer to waiter so he has an order to send
		waiter.addCustomer(customer,1,1,CustomerState.Ordered);
		
		//send order to cook;
		waiter.pickAndExecuteAnAction();
		//assert cook has an order
		assertTrue("Cook should have received the message.",
				cook.log.containsString("Received msgHereIsOrder."));
		
		//send order to waiter
		waiter.msgOrderDone("Food",1,1);
		//assert waiter changed order to done
		assertEquals(waiter.customers.get(0).s,CustomerState.FoodDone);
	
		//send order to customer
		waiter.pickAndExecuteAnAction();
	  //assert customer receives message
		assertTrue("Customer should receive message from waiter.",
				customer.log.containsString("Received msgHereIsFood."));
	}
	
	public void testRevolvingStandWaiter() {
		waiter = new TranacWaiterRSRole(person);
		waiter.setCook(cook);
		//bypass animation
		waiter.msgAnimationDone();
		waiter.msgAnimationDone();
		waiter.msgAnimationDone();
		
		//add customer to waiter so he has an order to send
		waiter.addCustomer(customer,1,1,CustomerState.Ordered);
	 //assert waiter has a customer
		assertEquals("Waiter should have one customer.",waiter.customers.size(), 1);
		
		//send order to cook;
		waiter.pickAndExecuteAnAction();
	 //assert cook has an order
		assertTrue("Cook should have added an order to its stand.",
				cook.log.containsString("Waiter added role to stand."));
	 //assert customer state
		assertEquals(waiter.customers.get(0).s,CustomerState.WaitingForFood);
	
		
		//send order to waiter
		waiter.msgOrderDone("Food",1,1);
	//assert waiter changed order to done
		assertEquals(waiter.customers.get(0).s,CustomerState.FoodDone);
	
		//send order to customer
		waiter.pickAndExecuteAnAction();
	  //assert customer receives message
		assertTrue("Customer should receive message from waiter.",
				customer.log.containsString("Received msgHereIsFood."));
	}
}
