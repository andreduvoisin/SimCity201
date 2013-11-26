package restaurant.restaurant_cwagoner.gui;

import restaurant.restaurant_cwagoner.roles.*;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class CwagonerRestaurantPanel extends JPanel {

    private CwagonerRestaurantGui mainGui; // Reference to main GUI
    
    private CwagonerHostRole host = new CwagonerHostRole("Sarah");
    private CwagonerCashierRole cashier = new CwagonerCashierRole();
    private CwagonerCookRole cook = new CwagonerCookRole();

    private List<CwagonerCustomerRole> Customers = new ArrayList<CwagonerCustomerRole>();
    private List<CwagonerWaiterRole> Waiters = new ArrayList<CwagonerWaiterRole>();

    public CwagonerRestaurantPanel(CwagonerRestaurantGui g, int numTables) {
    	super();
		
        mainGui = g;

        host.setNumTables(numTables);

        host.startThread();
        
        cashier.startThread();
        
        CwagonerCookGui cg = new CwagonerCookGui(cook, mainGui);
        mainGui.animationPanel.addGui(cg);
        cook.setGui(cg);
        cook.startThread();
        
        cashier.setCook(cook);
        cook.setCashier(cashier);
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, Boolean hungry) {

    	if (type.equals("Customers")) {
    		CwagonerCustomerRole c = new CwagonerCustomerRole(name);	
    		c.setHost(host);
    		c.setCashier(cashier);
    		CwagonerCustomerGui g = new CwagonerCustomerGui(c, mainGui);
    		c.setGui(g);
    		
    		Customers.add(c);

    		mainGui.animationPanel.addGui(g);
    		c.startThread();
    		if (hungry) {
    			g.setHungry();
    		}
    	}
    	
    	else {	// It's a waiter; ignore "hungry" checkbox (will be "on break" later)
    		CwagonerWaiterRole w = new CwagonerWaiterRole(name);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		CwagonerWaiterGui g = new CwagonerWaiterGui(w, mainGui, Waiters.size());
    		w.setGui(g);
    		w.startThread();
    		
    		mainGui.animationPanel.addGui(g);
    		Waiters.add(w);
    		
    		host.addWaiter(w);
    	}
    }
}