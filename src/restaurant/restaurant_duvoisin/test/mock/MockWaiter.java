package restaurant.restaurant_duvoisin.test.mock;

import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {
	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgSitAtTable(Customer c, int table, int waitingPosition) {
		
	}

	@Override
	public void msgImReadyToOrder(Customer c) {
		
	}

	@Override
	public void msgHereIsMyChoice(Customer c, String choice) {
		
	}

	@Override
	public void msgOrderIsReady(String choice, int table, int position) {
		
	}

	@Override
	public void msgDoneEatingAndLeaving(Customer c) {
		
	}

	@Override
	public void msgOutOfFood(int table, String choice) {
		
	}

	@Override
	public void msgRequestBreak() {
		
	}

	@Override
	public void msgRespondToBreakRequest(Boolean answer) {
		
	}

	@Override
	public void msgRequestCheck(Customer c) {
		
	}

	@Override
	public void msgHereIsCheck(Customer c, double amount) {
		
	}
}
