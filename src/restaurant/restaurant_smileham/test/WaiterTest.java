package restaurant.restaurant_smileham.test;

import junit.framework.TestCase;
import restaurant.restaurant_smileham.roles.SmilehamWaiterBase;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import restaurant.restaurant_smileham.test.mock.SmilehamMockCook;
import restaurant.restaurant_smileham.test.mock.SmilehamMockCustomer;
import base.PersonAgent;

public class WaiterTest extends TestCase {
	PersonAgent person;
	SmilehamWaiterBase waiter;
	SmilehamMockCook cook;
	SmilehamMockCustomer customer;
	
	
	
	public void setUp() throws Exception {
		super.setUp();
		person = new PersonAgent();
		
		
	}
	
	public void testRegularWaiter() {
		waiter = new SmilehamWaiterRole(person);
		
	}
	
	public void testSharedWaiter(){
		
	}
	
	
}
