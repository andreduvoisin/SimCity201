package base.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import base.Gui;

public class Restaurant {
	public int mRestaurantID;
	
	//people
	public RestaurantCashierRole mCashier;
	public RestaurantCookRole mCook;
	public RestaurantHostRole mHost;
	public List<RestaurantCustomerRole> mCustomers = Collections.synchronizedList(new ArrayList<RestaurantCustomerRole>());
	public List<RestaurantWaiterRole> mWaiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());

	//guis
	public List<Gui> mGuis = Collections.synchronizedList(new ArrayList<Gui>());
	
	public Restaurant(int n) {
		mRestaurantID = n;
	}

}
