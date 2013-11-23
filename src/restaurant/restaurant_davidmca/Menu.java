package restaurant.restaurant_davidmca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Menu {
	private String[] possibleItems = { "Steak", "Chicken", "Salad", "Pizza" };
	private ArrayList<String> items = new ArrayList<String>();
	private Map<String, Double> priceList = new HashMap<String, Double>();
	private boolean reorder = false;

	public Menu() {
		priceList.put("Steak", 15.99);
		priceList.put("Salad", 5.99);
		priceList.put("Chicken", 10.99);
		priceList.put("Pizza", 8.99);
		for (int i = 0; i < possibleItems.length; i++) {
			items.add(possibleItems[i]);
		}
	}

	public String pickItem() {
		Random generator = new Random();
		int choice = generator.nextInt(items.size());
		return items.get(choice);
	}

	public void removeItem(String choice) {
		items.remove(choice);
		priceList.remove(choice);
	}

	public double getPrice(String choice) {
		return priceList.get(choice);
	}

	public ArrayList<String> getItemList() {
		return items;
	}

	public void setReOrder() {
		this.reorder = true;
	}

	public boolean isReOrder() {
		return this.reorder;
	}
}
