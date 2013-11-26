package restaurant.restaurant_smileham.agent;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;

public class Check {
	public SmilehamCustomer mCustomer;
	public int mCash;
	public EnumFoodOptions mChoice;
	
	public Check(SmilehamCustomer customer, EnumFoodOptions choice, int cash){
		mCustomer = customer;
		mChoice = choice;
		mCash = cash;
	}
}
