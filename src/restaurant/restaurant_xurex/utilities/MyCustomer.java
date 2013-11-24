package restaurant.restaurant_xurex.utilities;

import restaurant.restaurant_xurex.interfaces.Customer;

public class MyCustomer{
	public Customer c;
	public int table;
	public float bill;
	public String choice;
	public CustomerState s;
	//Constructor
	public MyCustomer(Customer c, int table){
		this.c=c; this.table=table; s=CustomerState.waiting;
	}
};