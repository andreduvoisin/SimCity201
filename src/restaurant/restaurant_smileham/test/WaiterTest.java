package restaurant.restaurant_smileham.test;

import java.util.concurrent.Semaphore;

import junit.framework.TestCase;
import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.Order.EnumOrderStatus;
import restaurant.restaurant_smileham.roles.SmilehamWaiterBase;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRoleShared;
import restaurant.restaurant_smileham.test.mock.SmilehamMockCook;
import restaurant.restaurant_smileham.test.mock.SmilehamMockCustomer;
import base.PersonAgent;

public class WaiterTest extends TestCase {
	PersonAgent person;
	SmilehamWaiterBase waiter;
	SmilehamMockCook cook;
	SmilehamMockCustomer customer;
	Order order;
	Table table;
	
	public void setUp() throws Exception {
		super.setUp();
		person = new PersonAgent();
		//waiter is different for each test
		cook = new SmilehamMockCook("cook");
		customer = new SmilehamMockCustomer("customer");
		table = new Table(0);
		order = new Order(waiter, table, customer, EnumOrderStatus.PENDING);
		
	}
	
	//test deliver order method for both waiters
	
	//test deliverOrder for regular waiter
	public void testRegularWaiter() {
		waiter = new SmilehamWaiterRole(person);
		waiter.semAtCook = new Semaphore(100);
		waiter.mCook = cook;
		
	}
	
	public void testSharedWaiter(){
		waiter = new SmilehamWaiterRoleShared(person);
		waiter.semAtCook = new Semaphore(100);
	}
	
	
}
