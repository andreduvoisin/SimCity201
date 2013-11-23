package restaurant_tranac.test.mock;

import restaurant_tranac.interfaces.*;
import test.mock.*;

/**
 * MockMarket built for unit testing.
 *
 * @author Angelica Huyen Tran
 */

public class MockMarket extends Mock implements Market {
	
	public MockMarket() {
		super();
	}

	public void msgHereIsPayment(String i, double p) {
		log.add(new LoggedEvent("Received msgHereIsPayment from cashier. Item = " + i + ". Payment = " + p));
	}
	
	public void msgWillPaySoon(String i, double p) {
		log.add(new LoggedEvent("Received msgWillPaySoon from cashier. Item = " + i + ". Payment = " + p));
	}
//empty messages; not necessary to test cashier
	public void msgOrderFood(Cook c, String f, int n) {	}
}
