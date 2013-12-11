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

public class WaiterTest extends TestCase {
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
}
