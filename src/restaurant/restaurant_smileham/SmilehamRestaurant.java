package restaurant.restaurant_smileham;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import base.Gui;

public class SmilehamRestaurant {
	public int mRestaurantID;
	
	//people
	public static RestaurantCashierRole mCashier;
	public static RestaurantCookRole mCook;
	public static RestaurantHostRole mHost;
	public static List<RestaurantCustomerRole> mCustomers = Collections.synchronizedList(new ArrayList<RestaurantCustomerRole>());
	public static List<RestaurantWaiterRole> mWaiters = Collections.synchronizedList(new ArrayList<RestaurantWaiterRole>());

	//guis
	public static List<Gui> mGuis = Collections.synchronizedList(new ArrayList<Gui>());
	
	public SmilehamRestaurant(int n) {
		mRestaurantID = n;
	}

}
