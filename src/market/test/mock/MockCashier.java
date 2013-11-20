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
	
	}

	public void msgPayingForOrder(Invoice invoice) {

	}

	public boolean pickAndExecuteAnAction() {
		return false;
	}

	public int getNumWorkers() {
		return 0;
	}

}
