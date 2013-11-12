package restaurant_tranac.test.mock;

import restaurant_tranac.interfaces.*;

/**
 * MockCashier built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class MockCashier extends Mock implements Cashier {
	
	public MockCashier(String name) {
		super(name);
	}

	public void msgComputeCheck(Waiter w, Customer c, String item) {
		log.add(new LoggedEvent("Received msgComputeBill from waiter. Item = " + item));
	}
	
	public void msgHereIsPayment(Customer c, double p) {
		log.add(new LoggedEvent("Received msgHereIsPayment from customer. Payment = " + p));
	}
	
	public void msgHereIsBill(Market m, String i, double c) {
		log.add(new LoggedEvent("Received msgHereIsBill from market. Item = " + i + ". Price = " + c));
	}
}
