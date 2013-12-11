package restaurant.restaurant_smileham.test;

import junit.framework.TestCase;
import restaurant.intermediate.RestaurantCookRole;
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
	
	public void setUp() throws Exception {
		super.setUp();
		
		person = new PersonAgent();
		cookRole = new RestaurantCookRole(person, 5);
		cook = new SmilehamCookRole(person, cookRole);
		waiter = new SmilehamMockWaiter("Waiter");
	}
	
}
