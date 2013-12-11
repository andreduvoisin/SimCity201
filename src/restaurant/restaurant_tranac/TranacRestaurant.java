package restaurant.restaurant_tranac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import restaurant.restaurant_tranac.gui.Gui;
import restaurant.restaurant_tranac.interfaces.TranacCashier;
import restaurant.restaurant_tranac.interfaces.TranacCook;
import restaurant.restaurant_tranac.interfaces.TranacHost;
import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_tranac.roles.TranacCustomerRole;
import restaurant.restaurant_tranac.roles.TranacHostRole;
import restaurant.restaurant_tranac.roles.TranacWaiterBase;
import base.BaseRole;

public class TranacRestaurant {
	public static TranacRestaurant instance;
	public static List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	
    public static TranacCashier mCashier;
    public static TranacCook mCook;
    public static TranacHost mHost;
      
    public static Vector<TranacWaiterBase> mWaiters = new Vector<TranacWaiterBase>();
    public static Vector<TranacCustomerRole> mCustomers = new Vector<TranacCustomerRole>();
    
    public TranacRestaurant() {
    	instance = this;
    }
    
    public static void addGui(Gui gui) {
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
    	else if (role instanceof TranacWaiterBase){
    		TranacWaiterBase waiter = (TranacWaiterBase) role;
    		mWaiters.add(waiter);
    	}
    	else if (role instanceof TranacCashierRole){
    		mCashier = (TranacCashierRole) role;
    	}
    	else if (role instanceof TranacHostRole){
    		mHost = (TranacHostRole) role;
    	}
    	else if (role instanceof TranacCookRole){
    		mCook = (TranacCookRole) role;
    	}
    }
    
    public static TranacRestaurant getInstance() {
    	return instance;
    }
    
    public static int getNumWaiters() {
    	return mWaiters.size();
    }
    
    public static TranacCashier getCashier(){
    	return mCashier;
    }
    
    public static TranacCook getCook(){
    	return mCook;
    }
    
    public static TranacHost getHost(){
    	return mHost;
    }
}
