package restaurant.restaurant_duvoisin.test.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_duvoisin.interfaces.Cook;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.roles.AndreCookRole.Order;

public class MockCook extends Mock implements Cook {
	public EventLog log = new EventLog();
	public List<Order> revolvingStand = Collections.synchronizedList(new ArrayList<Order>());
	
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
		return revolvingStand;
	}
}
