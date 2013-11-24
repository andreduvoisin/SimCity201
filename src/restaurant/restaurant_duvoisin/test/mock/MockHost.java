package restaurant.restaurant_duvoisin.test.mock;

import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Host;
import restaurant.restaurant_duvoisin.interfaces.Waiter;

public class MockHost extends Mock implements Host {
	public MockHost(String name) {
		super(name);
	}

	@Override
	public void msgIWantToEat(Customer cust, int waitingPosition) {
		
	}

	@Override
	public void msgLeavingBecauseRestaurantFull(Customer cust) {
		
	}

	@Override
	public void msgTableIsFree(Waiter w, int table) {
		
	}

	@Override
	public void msgRequestGoOnBreak(Waiter w) {
		
	}

	@Override
	public void msgOffBreak(Waiter w) {
		
	}
}
