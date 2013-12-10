package restaurant.restaurant_jerryweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import restaurant.restaurant_jerryweb.gui.CustomerGui;
import restaurant.restaurant_jerryweb.gui.Gui;
import restaurant.restaurant_jerryweb.gui.WaiterGui;
import restaurant.restaurant_jerryweb.interfaces.Waiter;
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
	public static void addRSWaiter(JerrywebRSWaiterRole role){
		WaiterGui gui = new WaiterGui((Waiter) role, host);
		((JerrywebRSWaiterRole) role).setGui(gui);
		guis.add(gui);
		JerrywebRSWaiterRole rswaiter = (JerrywebRSWaiterRole) role;
		rswaiter.setHost(host);
		rswaiter.setCook(cook);
		rswaiter.setCashier(cashier);
		//JerrywebHostRole host = rswaiter.getHost();
		host.addWaiter((JerrywebRSWaiterRole)rswaiter);
		NumOfWatiers++;
	}
	
	public static void addPerson(BaseRole role){
    	if (role instanceof JerrywebCustomerRole){
    		//CustomerGui gui = new CustomerGui((JerrywebCustomerRole) role);
    		JerrywebCustomerRole customer = (JerrywebCustomerRole) role;
    		CustomerGui gui = new CustomerGui(customer);
    		guis.add(gui);
    		customer.setHost(host);
    		//JerrywebHostRole host = customer.getHost();
    		customer.setCashier(cashier);
    		//customer.gotHungry();
    		customers.add(customer);
    		customer.setGui(gui);
    		gui.setHungry();
    		NumOfCustomers++;
    	}
    	else if (role instanceof JerrywebWaiterRole){
    		WaiterGui gui = new WaiterGui((Waiter) role, host);
    		((JerrywebWaiterRole) role).setGui(gui);
    		guis.add(gui);
    		JerrywebWaiterRole waiter = (JerrywebWaiterRole) role;
    		waiter.setHost(host);
    		waiter.setCook(cook);
    		waiter.setCashier(cashier);
    		//JerrywebHostRole host = waiter.getHost();
    		host.addWaiter((JerrywebWaiterRole)waiter);
    		NumOfWatiers++;
    	}
    	else if (role instanceof JerrywebRSWaiterRole){
    		WaiterGui gui = new WaiterGui((Waiter) role, host);
    		((JerrywebRSWaiterRole) role).setGui(gui);
    		guis.add(gui);
    		JerrywebRSWaiterRole rswaiter = (JerrywebRSWaiterRole) role;
    		rswaiter.setHost(host);
    		rswaiter.setCook(cook);
    		//JerrywebHostRole host = rswaiter.getHost();
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
