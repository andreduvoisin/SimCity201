package restaurant.restaurant_jerryweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import restaurant.restaurant_jerryweb.gui.Gui;
import base.BaseRole;

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
    		waiter.setHost(host);
    		JerrywebHostRole host = waiter.getHost();
    		host.addWaiter((JerrywebWaiterRole)waiter);
    		NumOfWatiers++;
    	}
    	else if (role instanceof JerrywebRSWaiterRole){
    		JerrywebRSWaiterRole rswaiter = (JerrywebRSWaiterRole) role;
    		rswaiter.setHost(host);
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
