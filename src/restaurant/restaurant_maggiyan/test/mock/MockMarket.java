package restaurant.restaurant_maggiyan.test.mock;


import java.util.Map;

import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCook;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanMarket;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements MaggiyanMarket {
	
	public MaggiyanCashier cashier;

	public MockMarket(String name) {
		super(name);

	}

	@Override
	public void msgRequestItems(MaggiyanCook c, Map<String, Integer> itemRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(Double total) {
		log.add(new LoggedEvent("Received msgHereIsPayment of: " + total)); 
		
	}


}
