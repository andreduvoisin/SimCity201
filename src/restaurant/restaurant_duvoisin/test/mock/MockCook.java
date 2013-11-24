package restaurant.restaurant_duvoisin.test.mock;

import restaurant.restaurant_duvoisin.interfaces.Cook;
import restaurant.restaurant_duvoisin.interfaces.Market;
import restaurant.restaurant_duvoisin.interfaces.Waiter;

public class MockCook extends Mock implements Cook {
	public MockCook(String name) {
		super(name);
	}

	@Override
	public void msgHereIsOrder(Waiter w, String choice, int table) {
		
	}

	@Override
	public void msgFailedToFulfillRequest(Market ma, String item, int amount) {
		
	}

	@Override
	public void msgReplenishFood(String item, int amount) {
		
	}

	@Override
	public void msgGotFood(int position) {
		
	}
}
