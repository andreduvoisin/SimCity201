package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacHost;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import test.mock.Mock;

public class TranacMockHost extends Mock implements TranacHost {

	public void msgIWantFood(TranacCustomer c) {

	}

	public void msgWillWait(TranacCustomer c) {

	}

	public void msgLeavingEarly(TranacCustomer c) {

	}

	public void msgAtWaitingArea(TranacCustomer c) {

	}

	public void msgCustomerSeated(TranacCustomer c) {
		
	}

	public void msgTableIsFree(int t) {
		
	}

	public void addWaiter(TranacWaiter w) {

	}
}
