package restaurant_davidmca;

import java.util.HashMap;
import java.util.Map;

import restaurant_davidmca.interfaces.Customer;
import restaurant_davidmca.interfaces.Waiter;

public class Check {

	String choice;
	Customer cust;
	Waiter waiter;
	Map<String, Double> priceList = new HashMap<String, Double>();
	double total;
	double change = 0;

	public Check(Waiter w, Customer c, String ch) {
		priceList.put("Steak", 15.99);
		priceList.put("Salad", 5.99);
		priceList.put("Chicken", 10.99);
		priceList.put("Pizza", 8.99);
		this.choice = ch;
		this.cust = c;
		this.waiter = w;
		this.total = priceList.get(this.choice);
	}

	public String getChoice() {
		return this.choice;
	}

	public double getChange() {
		return this.change;
	}

	public double getTotal() {
		return this.total;
	}

	public Customer getCust() {
		return this.cust;
	}
}
