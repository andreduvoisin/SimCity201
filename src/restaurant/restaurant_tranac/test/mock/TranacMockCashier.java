package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.interfaces.TranacCashier;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacMarket;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import test.mock.LoggedEvent;
import test.mock.Mock;

/**
 * MockCashier built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class TranacMockCashier extends Mock implements TranacCashier {
	
	public TranacMockCashier() {
		super();
	}

	public void msgComputeCheck(TranacWaiter w, TranacCustomer c, String item) {
		log.add(new LoggedEvent("Received msgComputeBill from waiter. Item = " + item));
	}
	
	public void msgHereIsPayment(TranacCustomer c, double p) {
		log.add(new LoggedEvent("Received msgHereIsPayment from customer. Payment = " + p));
	}
	
	public void msgHereIsBill(TranacMarket m, String i, double c) {
		log.add(new LoggedEvent("Received msgHereIsBill from market. Item = " + i + ". Price = " + c));
	}
}
