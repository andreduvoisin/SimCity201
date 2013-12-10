package restaurant.restaurant_cwagoner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_cwagoner.interfaces.CwagonerWaiter;
import restaurant.restaurant_cwagoner.roles.CwagonerCashierRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCustomerRole;
import restaurant.restaurant_cwagoner.roles.CwagonerHostRole;
import restaurant.restaurant_cwagoner.roles.CwagonerSharedWaiterRole;
import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;
import base.Gui;
import base.interfaces.Role;

public class CwagonerRestaurant {

	public static CwagonerHostRole host;
	public static CwagonerCashierRole cashier;
	public static CwagonerCookRole cook;
	public static List<CwagonerCustomerRole> Customers =
			Collections.synchronizedList(new ArrayList<CwagonerCustomerRole>());
	public static List<CwagonerWaiter> Waiters =
			Collections.synchronizedList(new ArrayList<CwagonerWaiter>());
    public static List<Gui> guis =
    		Collections.synchronizedList(new ArrayList<Gui>());

    static int numTables = 4;

    public static void addGui(Gui gui) {
    	synchronized(guis) {
    		guis.add(gui);
    	}
    }

    public static void removeGui(Gui gui) {
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
