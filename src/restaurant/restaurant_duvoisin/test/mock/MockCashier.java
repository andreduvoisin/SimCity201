package restaurant.restaurant_duvoisin.test.mock;

import restaurant.restaurant_duvoisin.interfaces.Cashier;
import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Market;
import restaurant.restaurant_duvoisin.interfaces.Waiter;

public class MockCashier extends Mock implements Cashier {
	public MockCashier(String name) {
		super(name);
	}

	@Override
	public void msgComputeBill(Waiter w, Customer c, String choice) {
		
	}

	@Override
	public void msgPayment(Customer c, double amount) {
		
	}

	@Override
	public void msgComputeMarketBill(Market m, String type, int amount) {
		
	}
}
