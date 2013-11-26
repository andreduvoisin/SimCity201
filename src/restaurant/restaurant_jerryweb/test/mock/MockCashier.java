package restaurant.restaurant_jerryweb.test.mock;


import java.util.ArrayList;
import java.util.List;

import restaurant.restaurant_jerryweb.interfaces.Cashier;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCashier extends Mock implements Cashier {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Waiter waiter;
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockCashier(String name) {
		super(name);

	}


	@Override
	public void msgPayment(Customer customer, double cash) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Customer " + customer.getName() + " cash amount is " + cash));
	}

	@Override
	public void msgComputeBill(Waiter waiter, Customer customer, String choice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Waiter wants me to compute the bill for " + choice + " for customer " + customer.getName()));
	}

}
