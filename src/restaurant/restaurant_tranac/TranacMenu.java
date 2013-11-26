package restaurant.restaurant_tranac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranacMenu {
	Map<String,Double> choices = new HashMap<String,Double>();
	List<String> items = new ArrayList<String>();
//	List<Double> prices = new ArrayList<Double>();
	double steakPrice = 15.99;
	double chickenPrice = 10.99;
	double saladPrice = 5.99;
	double pizzaPrice = 8.99;
		
	public TranacMenu() {
		choices.put("Steak",steakPrice);
		choices.put("Chicken",chickenPrice);
		choices.put("Salad",saladPrice);
		choices.put("Pizza",pizzaPrice);
		
		items.add("Steak");
		items.add("Chicken");
		items.add("Salad");
		items.add("Pizza");
	/*	
		prices.add(steakPrice);
		prices.add(chickenPrice);
		prices.add(saladPrice);
		prices.add(pizzaPrice);
		*/
	}
		
	public Map<String,Double> getMenu() {
		return choices;
	}
	
	public String getChoice(int num) {
		return items.get(num);
	}
	
	public int getSize() {
		return choices.size();
	}
	
	public Double getCost(String s) {
		return choices.get(s);
	}
	
	public double getCheapestItem() {
		double p = getCost(items.get(0));
		
		for(String c : items) {
			if(getCost(c) < p)
				p = getCost(c);
		}
		return p;
	}
}
