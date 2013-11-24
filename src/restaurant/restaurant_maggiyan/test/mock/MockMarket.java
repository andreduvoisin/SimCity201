package restaurant.restaurant_maggiyan.test.mock;


import java.util.Map;

import restaurant.restaurant_maggiyan.CookAgent;
import restaurant.restaurant_maggiyan.interfaces.Cashier;
import restaurant.restaurant_maggiyan.interfaces.Cook;
import restaurant.restaurant_maggiyan.interfaces.Market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {
	
	public Cashier cashier;

	public MockMarket(String name) {
		super(name);

	}

	@Override
	public void msgRequestItems(Cook c, Map<String, Integer> itemRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(Double total) {
		log.add(new LoggedEvent("Received msgHereIsPayment of: " + total)); 
		
	}


}
