package restaurant.restaurant_cwagoner.gui;

import restaurant.restaurant_cwagoner.roles.*;
import base.interfaces.Person;
import base.interfaces.Role;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CwagonerRestaurantPanel extends JPanel {
	// THIS!
	static CwagonerRestaurantPanel instance;

    private CwagonerRestaurantGui mainGui; // Reference to main GUI
    
    private CwagonerHostRole host = new CwagonerHostRole();
    private CwagonerCashierRole cashier = new CwagonerCashierRole();
    private CwagonerCookRole cook = new CwagonerCookRole();
    private List<CwagonerCustomerRole> Customers = new ArrayList<CwagonerCustomerRole>();
    private List<CwagonerWaiterRole> Waiters = new ArrayList<CwagonerWaiterRole>();

    @SuppressWarnings("static-access")
	public CwagonerRestaurantPanel(CwagonerRestaurantGui g, int numTables) {
    	super();

        this.instance = this;
		
        mainGui = g;
        
//        host.setNumTables(numTables);
//        
//        CwagonerCookGui cg = new CwagonerCookGui(cook, mainGui);
//        mainGui.animationPanel.addGui(cg);
////        cook.setGui(cg);
//        
////        cashier.setCook(cook);
//        cook.setCashier(cashier);
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
    	Person person = subRole.getPerson();

    	if (person instanceof CwagonerCustomerRole) {
    		CwagonerCustomerRole c = new CwagonerCustomerRole(person);	
    		c.setHost(host);
    		c.setCashier(cashier);
    		CwagonerCustomerGui g = new CwagonerCustomerGui(c, mainGui);
    		c.setGui(g);
    		
    		Customers.add(c);

    		mainGui.animationPanel.addGui(g);
    	}
    	
    	else if (person instanceof CwagonerWaiterRole) {
    		CwagonerWaiterRole w = new CwagonerWaiterRole(person);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		CwagonerWaiterGui g = new CwagonerWaiterGui(w, mainGui);
    		w.setGui(g);
    		
    		mainGui.animationPanel.addGui(g);
    		Waiters.add(w);
    		
    		host.addWaiter(w);
    	}
    }
}