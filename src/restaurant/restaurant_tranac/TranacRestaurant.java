package restaurant.restaurant_tranac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_tranac.roles.TranacCustomerRole;
import restaurant.restaurant_tranac.roles.TranacHostRole;
import restaurant.restaurant_tranac.roles.TranacWaiterRole;
import base.BaseRole;
import restaurant.restaurant_tranac.gui.Gui;

public class TranacRestaurant {
	public static TranacRestaurant instance;
	public List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	
    public static TranacCashierRole mCashier;
    public static TranacCookRole mCook;
    public static TranacHostRole mHost;
      
    public static Vector<TranacWaiterRole> mWaiters = new Vector<TranacWaiterRole>();
    public static Vector<TranacCustomerRole> mCustomers = new Vector<TranacCustomerRole>();
    
    public TranacRestaurant() {
    	instance = this;
    }
    
    public void addGui(Gui gui) {
    	synchronized(guis) {
    		guis.add(gui);
    	}
    }
    
    public static void addPerson(BaseRole role){
    	if (role instanceof TranacCustomerRole){
    		TranacCustomerRole customer = (TranacCustomerRole) role;
    		mCustomers.add(customer);
    		customer.setHost(mHost);
    		customer.setCashier(mCashier);
    		customer.msgGotHungry();
    	}
    	else if (role instanceof TranacWaiterRole){
    		TranacWaiterRole waiter = (TranacWaiterRole) role;
    		mWaiters.add(waiter);
        	mHost.addWaiter(waiter);
    	}
    	else if (role instanceof TranacCashierRole){
    		TranacCashierRole cashier = (TranacCashierRole) role;
    		//ANGELICA: 1 add necessary logic here
    	}
    }
    
    public static TranacRestaurant getInstance() {
    	return instance;
    }
    
    public static int getNumWaiters() {
    	return mWaiters.size();
    }
    
    public static TranacCashierRole getCashier(){
    	return mCashier;
    }
    
    public static TranacCookRole getCook(){
    	return mCook;
    }
    
    public static TranacHostRole getHost(){
    	return mHost;
    }
}
