package restaurant.restaurant_duvoisin.test.mock;

import java.util.List;

import restaurant.restaurant_duvoisin.interfaces.Cook;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.roles.AndreCookRole.Order;

public class MockCook extends Mock implements Cook {
	public MockCook(String name) {
		super(name);
	}

	@Override
	public void msgHereIsOrder(Waiter w, String choice, int table) {
		
	}

	@Override
	public void msgGotFood(int position) {
		
	}

	@Override
	public List<Order> getRevolvingStand() {
		// TODO Auto-generated method stub
		return null;
	}
}
