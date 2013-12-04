package restaurant.restaurant_tranac.test.mock;

import restaurant.restaurant_tranac.interfaces.TranacCook;
import restaurant.restaurant_tranac.interfaces.TranacMarket;
import test.mock.LoggedEvent;
import test.mock.Mock;

/**
 * MockMarket built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class TranacMockMarket extends Mock implements TranacMarket {
	
	public TranacMockMarket() {
		super();
	}

	public void msgHereIsPayment(String i, double p) {
		log.add(new LoggedEvent("Received msgHereIsPayment from cashier. Item = " + i + ". Payment = " + p));
	}
	
	public void msgWillPaySoon(String i, double p) {
		log.add(new LoggedEvent("Received msgWillPaySoon from cashier. Item = " + i + ". Payment = " + p));
	}
//empty messages; not necessary to test cashier
	public void msgOrderFood(TranacCook c, String f, int n) {	}
}
