package restaurant.restaurant_maggiyan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import restaurant.restaurant_maggiyan.gui.MaggiyanCookGui;
import restaurant.restaurant_maggiyan.gui.MaggiyanCustomerGui;
import restaurant.restaurant_maggiyan.gui.MaggiyanGui;
import restaurant.restaurant_maggiyan.gui.MaggiyanWaiterGui;
import restaurant.restaurant_maggiyan.roles.MaggiyanCashierRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanHostRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;

public class MaggiyanRestaurant {
	
    public static MaggiyanHostRole mHost = null;
    public static MaggiyanCookRole mCook = null;
    public static MaggiyanCashierRole mCashier = null;
    private static Vector<MaggiyanCustomerRole> mCustomers;
     
    public static List<MaggiyanGui> guis;
    public static int waiterPosCounter = 1; 

    public MaggiyanRestaurant(){
    	guis = Collections.synchronizedList(new ArrayList<MaggiyanGui>());
    	mCustomers = new Vector<MaggiyanCustomerRole>();
    }
    
    public static void addCook(MaggiyanCookRole c){
    	mCook = c; 
    	MaggiyanCookGui g = new MaggiyanCookGui(mCook); 
    	mHost.setCook(mCook); 
    	guis.add(g); 
    }
    
    public static void addCustomer(MaggiyanCustomerRole c){
    	MaggiyanCustomerGui g = new MaggiyanCustomerGui(c); 
    	guis.add(g); 
    	c.setGui(g); 
    	c.setHost(mHost); 
    	c.setCashier(mCashier);
    	mCustomers.add(c); 
    	c.gotHungry();
	
    }
    
    public static void addWaiter(MaggiyanWaiterRole w){
    	MaggiyanWaiterGui g = new MaggiyanWaiterGui(w); 
    	guis.add(g);
    	w.setGui(g); 
    	w.setHost(mHost);
		w.setCook(mCook); 
		w.setCashier(mCashier);
		mHost.msgIAmHere(w);
		g.atWork(waiterPosCounter);
		waiterPosCounter++; 
    }
    
    public static void addSharedWaiter(MaggiyanSharedWaiterRole w){
    	MaggiyanWaiterGui g = new MaggiyanWaiterGui(w); 
    	guis.add(g);
    	w.setGui(g); 
    	w.setHost(mHost);
		w.setCook(mCook); 
		w.setCashier(mCashier);
		mHost.msgIAmHere(w);
		g.atWork(waiterPosCounter);
		waiterPosCounter++; 
    }
}
