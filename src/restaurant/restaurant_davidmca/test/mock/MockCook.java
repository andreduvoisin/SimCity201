package restaurant.restaurant_davidmca.test.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_davidmca.Order;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.CookGui;
import restaurant.restaurant_davidmca.interfaces.Cook;
import restaurant.restaurant_davidmca.interfaces.Waiter;

public class MockCook extends Mock implements Cook {
	
	public List<Order> revolvingStand = Collections
			.synchronizedList(new ArrayList<Order>());
	
	public void ProcessOrders() {
		synchronized (revolvingStand) {
			for (Order o: revolvingStand) {
				o.waiter.msgOrderIsReady(o);
			}
		}
	}

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setGui(CookGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAnOrder(Waiter w, String choice, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAnimating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Order> getRevolvingStand() {
		return revolvingStand;
	}

}
