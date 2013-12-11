package restaurant.restaurant_jerryweb.test;

import base.ContactList;
import base.PersonAgent;

import base.interfaces.Person;
import base.interfaces.Role;

import junit.framework.TestCase;
import restaurant.restaurant_jerryweb.JerrywebCashierRole;
import restaurant.restaurant_jerryweb.JerrywebCookRole;
import restaurant.restaurant_jerryweb.JerrywebHostRole;
import restaurant.restaurant_jerryweb.JerrywebRSWaiterRole;
import restaurant.restaurant_jerryweb.JerrywebCookRole.OrderState;
import restaurant.restaurant_jerryweb.gui.WaiterGui;
import restaurant.restaurant_jerryweb.test.mock.MockCustomer;

public class WaiterRevolvingStandTest extends TestCase {
	JerrywebCashierRole cashier;
	//JerrywebRestaurantGui gui;
	JerrywebHostRole host;
	JerrywebCookRole cook;
	JerrywebRSWaiterRole rsWaiter;
	MockCustomer mCustomer;
	WaiterGui waiterGui;
	PersonAgent mPerson;
	PersonAgent mPerson2;
	PersonAgent mPerson3;
	
	public void setUp() throws Exception{
		super.setUp();
		ContactList.setup();
		
		mPerson = new PersonAgent();
		mPerson2 = new PersonAgent();
		mPerson3 = new PersonAgent();
		mCustomer = new MockCustomer("mock customer");
		cashier = new JerrywebCashierRole(mPerson, null);
		rsWaiter = new JerrywebRSWaiterRole("rs waiter");
		cook = new JerrywebCookRole(mPerson2, null);
		mPerson2.addRole((Role)cook, true);
		host = new JerrywebHostRole(mPerson3);
		mPerson3.addRole((Role)host, true);
		waiterGui = new WaiterGui(rsWaiter, host);
	}
	
	public void testOneNormalRSWaiterScenario(){
		
		mCustomer.cashier = cashier;
		rsWaiter.setCook(cook);
		rsWaiter.addCustomerForTesting(mCustomer, 1);
		rsWaiter.setGui(waiterGui);

		assertEquals("Cook should not have orders in his list of orders. He does.", cook.Orders.size(), 0);
		
		//checking the size of the Waiter's Customers list
		assertEquals("The rsWaiter should have only one customer in it's MyCustomer list. It doesn't. ", rsWaiter.Customers.size(),1);
		
		//rsWaiter.sendOrder(rsWaiter.Customers.get(0));
		cook.addToRevolvingStand(rsWaiter, rsWaiter.Customers.get(0).choice, rsWaiter.Customers.get(0).table, OrderState.pending);
		
		assertEquals("Cook should still not have any orders in Orders. It does: ", 0, cook.Orders.size()); 
		assertEquals("Cook should have one order in RevolvingStandOrders. It doesn't: ", 1, cook.RevolvingStandOrders.size()); 
		
		cook.moveRevlovingStandOrders();
		
		assertEquals("Cook should have order in Orders. It doesn't: ", 1, cook.Orders.size()); 
		assertEquals("Cook shouldn't have any Orders in RevolvingStandOrders. It does: ", 0, cook.RevolvingStandOrders.size()); 
		//cook.addToRevolvingStand(rsWaiter, custOrder, t, orderS);
		//assertEquals("Cook should still not have any orders in Orders. It does: ", 0, cook.Orders.size()); 
	}
}
