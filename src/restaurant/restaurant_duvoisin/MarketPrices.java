package restaurant.restaurant_duvoisin;

import java.util.*;

/**
 * Restaurant Menu
 */
public class MarketPrices {
	// Array of Items
	Map<String, Double> currentRate = new HashMap<String, Double>();
	
	public MarketPrices() {
		currentRate.put("steak", 10.00);
		currentRate.put("chicken", 5.00);
		currentRate.put("salad", 2.00);
		currentRate.put("pizza", 3.00);
	}
}