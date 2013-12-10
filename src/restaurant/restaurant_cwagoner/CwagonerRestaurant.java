package restaurant.restaurant_cwagoner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.interfaces.Role;
import restaurant.restaurant_cwagoner.gui.CwagonerGui;
import restaurant.restaurant_cwagoner.interfaces.CwagonerWaiter;
import restaurant.restaurant_cwagoner.roles.*;

public class CwagonerRestaurant {

	public static CwagonerHostRole host;
	public static CwagonerCashierRole cashier;
	public static CwagonerCookRole cook;
	public static List<CwagonerCustomerRole> Customers =
			Collections.synchronizedList(new ArrayList<CwagonerCustomerRole>());
	public static List<CwagonerWaiter> Waiters =
			Collections.synchronizedList(new ArrayList<CwagonerWaiter>());
    public static List<CwagonerGui> guis =
    		Collections.synchronizedList(new ArrayList<CwagonerGui>());

    static int numTables = 4;

    public static void addGui(CwagonerGui gui) {
    	synchronized(guis) {
    		guis.add(gui);
    	}
    }

    public void removeGui(CwagonerGui gui) {
    	synchronized(guis) {
    		guis.remove(gui);
    	}
    }

    public static void addPerson(Role subRole) {
    	if (subRole instanceof CwagonerHostRole) {
    		host = (CwagonerHostRole)subRole;
    		host.setNumTables(numTables);
    		for (CwagonerWaiter iWaiter : Waiters) {
    			host.addWaiter(iWaiter);
    		}
    	}
    	else if (subRole instanceof CwagonerCashierRole) {
    		cashier = (CwagonerCashierRole)subRole;
    	}
    	else if (subRole instanceof CwagonerCookRole) {
    		cook = (CwagonerCookRole)subRole;
    		cook.setCashier(cashier);
    	}
    	else if (subRole instanceof CwagonerCustomerRole) {	
    		Customers.add((CwagonerCustomerRole) subRole);
    		((CwagonerCustomerRole)subRole).setHost(host);
    		((CwagonerCustomerRole) subRole).setCashier(cashier);
    	}
    	else if (subRole instanceof CwagonerSharedWaiterRole) {
    		Waiters.add((CwagonerSharedWaiterRole)subRole);
    		((CwagonerSharedWaiterRole) subRole).setHost(host);
    		if (host != null) host.addWaiter((CwagonerWaiter)subRole);
    	}
    	else if (subRole instanceof CwagonerWaiterRole) {
    		Waiters.add((CwagonerWaiterRole) subRole);
    		((CwagonerWaiterRole)subRole).setHost(host);
    		if (host != null) host.addWaiter((CwagonerWaiter)subRole);
    	}
    }
}
