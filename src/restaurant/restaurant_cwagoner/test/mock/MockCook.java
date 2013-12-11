package restaurant.restaurant_cwagoner.test.mock;

import restaurant.restaurant_cwagoner.interfaces.*;
import test.mock.LoggedEvent;
import test.mock.Mock;

public class MockCook extends Mock implements CwagonerCook{

	public void msgHereIsOrder(CwagonerWaiter w, String c, int t) {
		log.add(new LoggedEvent("Received msgHereIsOrder."));
	}

	public void msgOrderPickedUp(CwagonerWaiter w, String c) {
		log.add(new LoggedEvent("Received msgOrderPickedUp."));
	}

	@Override
	public void msgHeresAnOrder(CwagonerWaiter w, int tableNum, String food) {
		
	}
}
