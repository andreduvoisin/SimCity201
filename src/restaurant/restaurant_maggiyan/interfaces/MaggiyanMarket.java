package restaurant.restaurant_maggiyan.interfaces;

import java.util.Map;

import restaurant.restaurant_maggiyan.roles.MaggiyanCook;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface MaggiyanMarket {
	
	public void msgRequestItems(MaggiyanCook c, Map<String, Integer> itemRequest);

	public String getName();

	public void msgHereIsPayment(Double total);

}