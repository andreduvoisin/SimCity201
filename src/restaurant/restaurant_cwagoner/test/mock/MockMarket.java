package restaurant.restaurant_cwagoner.test.mock;

import java.util.HashMap;

import restaurant.restaurant_cwagoner.interfaces.CwagonerCashier;
import restaurant.restaurant_cwagoner.interfaces.CwagonerCook;
import restaurant.restaurant_cwagoner.interfaces.CwagonerMarket;

public class MockMarket extends Mock implements CwagonerMarket {

	public CwagonerCashier cwagonerCashier;

	public EventLog log;

	public MockMarket(String name) {
		super(name);
		log = new EventLog();
	}
	
	public void msgNeedFood(CwagonerCook c, CwagonerCashier ca, HashMap<String, Integer> orderedStock) {
		log.add(new LoggedEvent("Received msgNeedFood"));
	}
	
	public void msgPayment(CwagonerCashier ca, double payment) {
		log.add(new LoggedEvent("Received msgPayment; payment = " + payment));
	}
	
	public String getName() {
		return name;
	}

}
