package restaurant_cwagoner.test.mock;

import java.util.HashMap;

import restaurant_cwagoner.interfaces.*;

public class MockMarket extends Mock implements Market {

	public Cashier cashier;

	public EventLog log;

	public MockMarket(String name) {
		super(name);
		log = new EventLog();
	}
	
	public void msgNeedFood(Cook c, Cashier ca, HashMap<String, Integer> orderedStock) {
		log.add(new LoggedEvent("Received msgNeedFood"));
	}
	
	public void msgPayment(Cashier ca, double payment) {
		log.add(new LoggedEvent("Received msgPayment; payment = " + payment));
	}
	
	public String getName() {
		return name;
	}

}
