package restaurant.restaurant_tranac;

import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;

public class TranacCheck {
	TranacWaiter waiter;
	TranacCustomer customer;
	String item;
	double amount;
	double payment;
	double change;
	private int restaurantSSN;
	
	public TranacCheck(TranacWaiter w, TranacCustomer c, String i, int n) {
		waiter = w;
		customer = c;
		item = i;
		amount = 0;
		payment = 0;
		change = 0;
		restaurantSSN = n;
	}
	
	public void setAmount(double a) {
		amount = a;
	}
	
	public void setPayment(double p) {
		payment = p;
	}
	
	public void setChange(double c) {
		change = c;
	}
	
	public void setSSN(int s) {
		setSsn(s);
	}
	
	public void addOutstandingBill(double a) {
		amount += a;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public double getPayment() {
		return payment;
	}
	
	public double getChange() {
		return change;
	}
	
	public String getItem() {
		return item;
	}
	
	public TranacWaiter getWaiter() {
		return waiter;
	}
	
	public TranacCustomer getCustomer() {
		return customer;
	}

	public int getSsn() {
		return restaurantSSN;
	}

	public void setSsn(int ssn) {
		this.restaurantSSN = ssn;
	}
}
