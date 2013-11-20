package market.test.mock;

import market.interfaces.Cashier;
import test.mock.*;
import market.*;

public class MockCashier extends Mock implements Cashier {

	@Override
	public void msgOrderPlacement(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayingForOrder(Invoice invoice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getNumWorkers() {
		// TODO Auto-generated method stub
		return 0;
	}

}
