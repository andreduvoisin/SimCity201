package restaurant.restaurant_duvoisin;

import java.util.HashMap;
import java.util.Map;

/**
 * Restaurant Menu
 */
public class Menu {
	// Array of Items
	public Map<String, Double> menuItems = new HashMap<String, Double>();
	
	public Menu() {
		menuItems.put("salad", 5.99);
		menuItems.put("pizza", 8.99);
		menuItems.put("chicken", 10.99);
		menuItems.put("steak", 15.99);
	}
	
	public void removeMenuOption(String option) { menuItems.remove(option); }
}