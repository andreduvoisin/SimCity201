package restaurant.restaurant_tranac;

import restaurant.restaurant_tranac.interfaces.*;

public class Check {
	Waiter waiter;
	Customer customer;
	String item;
	double amount;
	double payment;
	double change;
	
	public Check(Waiter w, Customer c, String i) {
		waiter = w;
		customer = c;
		item = i;
		amount = 0;
		payment = 0;
		change = 0;
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
	
	public Waiter getWaiter() {
		return waiter;
	}
	
	public Customer getCustomer() {
		return customer;
	}
}
