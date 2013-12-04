package restaurant.restaurant_cwagoner.gui;

import restaurant.restaurant_cwagoner.roles.*;
import base.interfaces.Role;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CwagonerRestaurantPanel extends JPanel {
	// THIS!
	static CwagonerRestaurantPanel instance;

    private CwagonerRestaurantGui mainGui; // Reference to main GUI
    
    public CwagonerHostRole host = new CwagonerHostRole(null);
    public CwagonerCashierRole cashier = new CwagonerCashierRole(null);
    public CwagonerCookRole cook = new CwagonerCookRole(null);
    private List<CwagonerCustomerRole> Customers = new ArrayList<CwagonerCustomerRole>();
    private List<CwagonerWaiterRole> Waiters = new ArrayList<CwagonerWaiterRole>();

	public CwagonerRestaurantPanel(CwagonerRestaurantGui g, int numTables) {
    	super();
        instance = this;
        mainGui = g;
        host.setNumTables(numTables);

        CwagonerCookGui cg = new CwagonerCookGui(cook, mainGui);
        mainGui.animationPanel.addGui(cg);
        cook.setGui(cg);
        
        cashier.setCook(cook);
        cook.setCashier(cashier);
    }

    public static CwagonerRestaurantPanel getInstance() {
		return instance;
	}

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param name name of person
     */
    public void addPerson(Role subRole) {

    	if (subRole instanceof CwagonerCustomerRole) {
    		((CwagonerCustomerRole) subRole).setHost(host);
    		((CwagonerCustomerRole) subRole).setCashier(cashier);
    		((CwagonerCustomerRole) subRole).setGui(new CwagonerCustomerGui((CwagonerCustomerRole) subRole, mainGui));	
    		((CwagonerCustomerRole) subRole).getGui().setPresent(true);
    		Customers.add((CwagonerCustomerRole) subRole);
    		mainGui.animationPanel.addGui(((CwagonerCustomerRole) subRole).getGui());
    	}
    	else if (subRole instanceof CwagonerWaiterRole) {
    		((CwagonerWaiterRole) subRole).setHost(host);
    		((CwagonerWaiterRole) subRole).setCashier(cashier);
    		((CwagonerWaiterRole) subRole).setCook(cook);
    		((CwagonerWaiterRole) subRole).setGui(new CwagonerWaiterGui((CwagonerWaiterRole) subRole, mainGui));	
    		Waiters.add((CwagonerWaiterRole) subRole);
    		mainGui.animationPanel.addGui(((CwagonerWaiterRole) subRole).getGui());
    		host.addWaiter((CwagonerWaiterRole)subRole);
    	}
    	// CHASE add other roles
    }
}