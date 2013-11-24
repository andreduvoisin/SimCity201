package base;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.interfaces.Customer;

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
