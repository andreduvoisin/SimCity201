package restaurant.restaurant_smileham.test;

import junit.framework.TestCase;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Order.EnumOrderStatus;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_smileham.test.mock.SmilehamMockCustomer;
import restaurant.restaurant_smileham.test.mock.SmilehamMockWaiter;
import base.PersonAgent;

public class CookTest extends TestCase {

	PersonAgent person;
	RestaurantCookRole cookRole;
	SmilehamCookRole cook;
	SmilehamMockWaiter waiter;
	SmilehamMockCustomer customer;
	Order order;
	Table table;
	
	public void setUp() throws Exception {
		super.setUp();
		person = new PersonAgent();
		cookRole = new RestaurantCookRole(person, 5);
		cook = new SmilehamCookRole(person, cookRole);
		waiter = new SmilehamMockWaiter("Waiter");
		table = new Table(0);
		customer = new SmilehamMockCustomer("mock customer");
		order = new Order(waiter, table, customer, EnumOrderStatus.PENDING);
		
	}
	
	
	//TESTS
	
	//gets order from regular waiter
	public void testRegularWaiter(){
		//assert preconditions
		assertEquals("Cook should have 0 orders", cook.mOrders.size(), 0);
		//give cook an order
		cook.msgMakeFood(order);
		//assert postconditions
		assertEquals("Cook should have 1 order", cook.mOrders.size(), 1);
	}
	
	//gets order from revolving stand
	public void testRevolvingStand(){
		//assert preconditions
		assertEquals("Cook should have 0 orders", cook.mOrders.size(), 0);
		//add order to stand
		cook.addOrderToStand(order);
		//assert postconditions
		assertEquals("Cook should have 0 orders", cook.mOrders.size(), 0);
		assertEquals("Cook's stand should have 1 order", cook.revolvingStand.size(), 1);
		
		//check stand
		cook.checkStand();
		//check postconditions
		assertEquals("Cook should have 1 order", cook.mOrders.size(), 1);
		assertEquals("Cook's stand should have 0 orders", cook.revolvingStand.size(), 0);
		
	}
	
}
