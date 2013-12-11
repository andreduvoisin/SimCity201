package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.interfaces.TranacCook;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import test.mock.LoggedEvent;
import test.mock.Mock;

public class TranacMockCook extends Mock implements TranacCook{

	public void addOrderToStand(TranacWaiter w, String c, int t) {
		log.add(new LoggedEvent("Waiter added role to stand."));
	}

	public void msgHereIsOrder(TranacWaiter w, String c, int t) {
		log.add(new LoggedEvent("Received msgHereIsOrder."));
	}

	public void msgOrderPickedUp(TranacWaiter w, String c) {
		log.add(new LoggedEvent("Received msgOrderPickedUp."));
	}
}
