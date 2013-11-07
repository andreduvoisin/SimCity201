package agent;

import restaurant.Food.EnumFoodOptions;
import restaurant.interfaces.Customer;

public class Check {
	public Customer mCustomer;
	public int mCash;
	public EnumFoodOptions mChoice;
	
	public Check(Customer customer, EnumFoodOptions choice, int cash){
		mCustomer = customer;
		mChoice = choice;
		mCash = cash;
	}
}
