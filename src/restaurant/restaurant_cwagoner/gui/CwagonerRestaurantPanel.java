package restaurant.restaurant_cwagoner.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import restaurant.restaurant_cwagoner.roles.CwagonerCashierRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCustomerRole;
import restaurant.restaurant_cwagoner.roles.CwagonerHostRole;
import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;
import base.interfaces.Role;

@SuppressWarnings("serial")
public class CwagonerRestaurantPanel extends JPanel  {
	// THIS!

    private CwagonerRestaurantGui mainGui; // Reference to main GUI
    
    public CwagonerHostRole host = new CwagonerHostRole(null);
    public CwagonerCashierRole cashier = new CwagonerCashierRole(null);
    public static CwagonerCookRole cook;
    private List<CwagonerCustomerRole> Customers = new ArrayList<CwagonerCustomerRole>();
    private List<CwagonerWaiterRole> Waiters = new ArrayList<CwagonerWaiterRole>();

	public CwagonerRestaurantPanel(CwagonerRestaurantGui g, int numTables) {
    	super();

        mainGui = g;
        host.setNumTables(numTables);

        CwagonerCookGui cg = new CwagonerCookGui(cook, mainGui);
        mainGui.animationPanel.addGui(cg);
        cook.setGui(cg);
        
        cashier.setCook(cook);
        cook.setCashier(cashier);
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