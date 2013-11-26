package restaurant.restaurant_jerryweb.test;

import restaurant.restaurant_jerryweb.CashierRole;
import restaurant.restaurant_jerryweb.CookRole;
import restaurant.restaurant_jerryweb.HostRole;
import restaurant.restaurant_jerryweb.RSWaiterRole;
import restaurant.restaurant_jerryweb.RSWaiterRole.CustomerState;
import restaurant.restaurant_jerryweb.RSWaiterRole.MyCustomer;
import restaurant.restaurant_jerryweb.gui.RestaurantGui;
import restaurant.restaurant_jerryweb.gui.WaiterGui;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.test.mock.MockCook;
import restaurant.restaurant_jerryweb.test.mock.MockCustomer;
import junit.framework.TestCase;

public class WaiterRevolvingStandTest extends TestCase {
	CashierRole cashier;
	RestaurantGui gui;
	HostRole host;
	CookRole cook;
	RSWaiterRole rsWaiter;
	MockCustomer mCustomer;
	WaiterGui waiterGui;
	
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierRole("cashier");
		mCustomer = new MockCustomer("mock customer");
		rsWaiter = new RSWaiterRole("rs waiter");
		cook = new CookRole("cook");
		host = new HostRole("host");
		waiterGui = new WaiterGui(rsWaiter, gui, host);
	}
	
	public void testOneNormalRSWaiterScenario(){
		
		mCustomer.cashier = cashier;
		rsWaiter.setCook(cook);
		rsWaiter.addCustomerForTesting(mCustomer, 1);
		rsWaiter.setGui(waiterGui);
		//rsWaiter.Customers.add(new rsWaiter.MyCustomer(mCustomer));
		//rsWaiter.msgHereIsMyOrder(mCustomer, "salad");
		//assertEquals("The Cook's scheduler should not have run after the RSWaiter was given an order, so the cook's log should be empty. It does not:  "
				//+ cook.log.toString(), 0, cook.log.size());
		//check if the cook has any previous orders
		assertEquals("Cook should not have orders in his list of orders. He does.", cook.Orders.size(), 0);
		
		//checking the size of the Waiter's Customers list
		assertEquals("The rsWaiter should have only one customer in it's MyCustomer list. It doesn't. ", rsWaiter.Customers.size(),1);
		
		rsWaiter.sendOrder(rsWaiter.Customers.get(0));
		
		assertEquals("Cook should still not have any orders in Orders. It does: ", 0, cook.Orders.size()); 
		assertEquals("Cook should have one order in RevolvingStandOrders. It doesn't: ", 1, cook.RevolvingStandOrders.size()); 
		
		//cook.addToRevolvingStand(rsWaiter, custOrder, t, orderS);
		//assertEquals("Cook should still not have any orders in Orders. It does: ", 0, cook.Orders.size()); 
	}
}
