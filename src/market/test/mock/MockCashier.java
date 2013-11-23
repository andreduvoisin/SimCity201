package market.test.mock;

import market.interfaces.MarketCashier;
import test.mock.*;
import market.*;

/**
 * MockCashier for unit testing.
 * 
 * @author Angelica Huyen Tran
 */
public class MockCashier extends Mock implements MarketCashier {

	public MockCashier() {
		super();
	}
	
	public void msgOrderPlacement(MarketOrder order) {
		log.add(new LoggedEvent("Received msgOrderPlacement from " + order.mPersonRole));
	}

	public void msgPayingForOrder(MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgPayingForOrder " + invoice.mOrder.mPersonRole));
	}
}
