package restaurant.restaurant_jerryweb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import base.BaseRole;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_jerryweb.gui.Gui;

public class JerrywebRestaurant {
	/*
	 * Data
	 */
	public static JerrywebHostRole host;
	public static JerrywebCookRole cook;
	public static JerrywebCashierRole cashier;
	public static Vector<JerrywebCustomerRole> customers;
	public static List<Gui> guis;
	static int NumOfCustomers = 0;
	static int NumOfWatiers = 0;
	//public static Collection<Table> tables;
	
	
	public JerrywebRestaurant() {
		customers = new Vector<JerrywebCustomerRole>();
		guis = Collections.synchronizedList(new ArrayList<Gui>());
		
	}
	
	public static void addPerson(BaseRole role){
    	if (role instanceof JerrywebCustomerRole){
    		JerrywebCustomerRole customer = (JerrywebCustomerRole) role;
    		
    		customers.add(customer);
    		customer.gotHungry();
    		NumOfCustomers++;
    	}
    	else if (role instanceof JerrywebWaiterRole){
    		JerrywebWaiterRole waiter = (JerrywebWaiterRole) role;
    		JerrywebHostRole host = waiter.getHost();
    		host.addWaiter((JerrywebWaiterRole)waiter);
    		NumOfWatiers++;
    	}
    	else if (role instanceof JerrywebWaiterRole){
    		JerrywebRSWaiterRole rswaiter = (JerrywebRSWaiterRole) role;
    		JerrywebHostRole host = rswaiter.getHost();
    		host.addWaiter((JerrywebRSWaiterRole)rswaiter);
    		NumOfWatiers++;
    	}
    	else if (role instanceof JerrywebHostRole){
    		host = (JerrywebHostRole) role;
    	}
    	else if (role instanceof JerrywebCookRole){
    		cook = (JerrywebCookRole) role;
    	}
    	else if (role instanceof JerrywebCashierRole){
    		cashier = (JerrywebCashierRole) role;
    	}
    }
	

}
