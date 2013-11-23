package market.test.mock;

import market.interfaces.Cashier;
import test.mock.*;
import market.*;

/**
 * MockCashier for unit testing.
 * 
 * @author Angelica Huyen Tran
 */
public class MockCashier extends Mock implements Cashier {

	public MockCashier() {
		super();
	}
	
	public void msgOrderPlacement(Order order) {
		log.add(new LoggedEvent("Received msgOrderPlacement from " + order.mPersonRole));
	}

	public void msgPayingForOrder(Invoice invoice) {
		log.add(new LoggedEvent("Received msgPayingForOrder " + invoice.mOrder.mPersonRole));
	}
}
