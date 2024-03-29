package restaurant.restaurant_smileham;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_smileham.roles.SmilehamWaiterBase;
import base.BaseRole;
import base.Gui;

public class SmilehamRestaurant {

	// people
	public static SmilehamCashierRole mCashier;
	public static SmilehamCookRole mCook;
	public static SmilehamHostRole mHost;
	public static List<SmilehamCustomerRole> mCustomers = Collections
			.synchronizedList(new ArrayList<SmilehamCustomerRole>());
	public static List<SmilehamWaiterBase> mWaiters = Collections
			.synchronizedList(new ArrayList<SmilehamWaiterBase>());

	// guis
	public static List<Gui> mGuis = Collections
			.synchronizedList(new ArrayList<Gui>());

	public SmilehamRestaurant() {
	}

	public static void addPerson(BaseRole role) {
		if (role instanceof SmilehamCustomerRole) {
			SmilehamCustomerRole customer = (SmilehamCustomerRole) role;
			mCustomers.add(customer);
			customer.msgGotHungry();
		} else if (role instanceof SmilehamWaiterBase) {
			mHost.msgAddWaiter((SmilehamWaiter) role);
		} else if (role instanceof SmilehamHostRole) {
			mHost = (SmilehamHostRole) role;
		} else if (role instanceof SmilehamCookRole) {
			mCook = (SmilehamCookRole) role;
		} else if (role instanceof SmilehamCashierRole) {
			mCashier = (SmilehamCashierRole) role;
		}
	}
	
	public static void addGui(Gui g) {
		synchronized(mGuis) {
			mGuis.add(g);
		}
	}
	
	public static void removeGui(Gui g) {
		synchronized(mGuis) {
			mGuis.remove(g);
		}
	}

}
