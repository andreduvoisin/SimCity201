package restaurant.restaurant_smileham;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamCook;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import base.BaseRole;
import base.Gui;

public class SmilehamRestaurant {
	public int mRestaurantID;

	// people
	public static SmilehamCashierRole mCashier;
	public static SmilehamCookRole mCook;
	public static SmilehamHostRole mHost;
	public static List<SmilehamCustomerRole> mCustomers = Collections
			.synchronizedList(new ArrayList<SmilehamCustomerRole>());
	public static List<SmilehamWaiterRole> mWaiters = Collections
			.synchronizedList(new ArrayList<SmilehamWaiterRole>());

	// guis
	public static List<Gui> mGuis = Collections
			.synchronizedList(new ArrayList<Gui>());

	public SmilehamRestaurant(int n) {
		mRestaurantID = n;
	}

	public static void addPerson(BaseRole role) {
		if (role instanceof SmilehamCustomerRole) {
			SmilehamCustomerRole customer = (SmilehamCustomerRole) role;
			mCustomers.add(customer);
			customer.msgGotHungry();
		} else if (role instanceof SmilehamWaiterRole) {
			SmilehamWaiterRole waiter = (SmilehamWaiterRole) role;

			SmilehamHost host = waiter.getHost();
			host.msgAddWaiter((SmilehamWaiter) waiter);

		} else if (role instanceof SmilehamHostRole) {
			mHost = (SmilehamHostRole) role;
		} else if (role instanceof SmilehamCookRole) {
			mCook = (SmilehamCookRole) role;
		} else if (role instanceof SmilehamCashierRole) {
			mCashier = (SmilehamCashierRole) role;
		}
	}

	public static SmilehamHost getHost() {
		return mHost;
	}

	public static SmilehamCashier getCashier() {
		return mCashier;
	}

	public static SmilehamCook getCook() {
		return mCook;
	}

}
