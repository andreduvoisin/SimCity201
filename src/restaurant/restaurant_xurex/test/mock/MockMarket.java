package restaurant.restaurant_xurex.test.mock;


import java.util.Map;

import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Market;


/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	private float assets = 0;
	
	public MockMarket(String name) {
		super(name);
	}

	@Override
	public void HereIsOrder(Map<String, Integer> order) {
		log.add(new LoggedEvent("HereIsOrder"));
	}

	@Override
	public void HereIsPayment(float payment) {
		assets+=payment;
	}

	@Override
	public int getQuantity(String food) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getAssets() {
		return assets;
	}

	

}
