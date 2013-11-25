package restaurant.restaurant_cwagoner.gui;

import restaurant.restaurant_cwagoner.roles.*;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel {

    private RestaurantGui mainGui; // Reference to main GUI
    
    // Host, cook, waiters, customers, markets
    private HostRole host = new HostRole("Sarah");
    private CashierRole cashier = new CashierRole();
    private CookRole cook = new CookRole();

    private List<CustomerRole> Customers = new ArrayList<CustomerRole>();
    private List<WaiterRole> Waiters = new ArrayList<WaiterRole>();
    // CHASE: fix markets - see commented out lines
    //private List<MarketRole> Markets = new ArrayList<MarketRole>();

    // Stores restaurant menu
    private JPanel menuPanel = new JPanel();

    public RestaurantPanel(RestaurantGui restaurantGui, int numTables) {
    	super();
		
        mainGui = restaurantGui;

        host.setNumTables(numTables);

        host.startThread();
        
        cashier.startThread();
        
        CookGui cg = new CookGui(cook, mainGui);
        mainGui.animationPanel.addGui(cg);
        cook.setGui(cg);
        cook.startThread();
        
        cashier.setCook(cook);
        cook.setCashier(cashier);

        setLayout(new GridLayout(2, 2));

        initRestLabel();

        add(menuPanel);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3>"
                + "<table><tr><td>host:</td>"
                + "<td>" + host.getName() + "</td></tr></table>"
                + "<h3><u>Menu</u></h3>"
                + "<table><tr><td>Steak</td><td>$8</td></tr>"
                + "<tr><td>Chicken</td><td>$6</td></tr>"
                + "<tr><td>Salad</td><td>$2</td></tr>"
                + "<tr><td>Pizza</td><td>$4</td></tr>"
                + "</table><br></html>");
        menuPanel.add(label);
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, Boolean hungry) {

    	if (type.equals("Customers")) {
    		CustomerRole c = new CustomerRole(name);	
    		c.setHost(host);
    		c.setCashier(cashier);
    		CustomerGui g = new CustomerGui(c, mainGui);
    		c.setGui(g);
    		
    		Customers.add(c);

    		mainGui.animationPanel.addGui(g);
    		c.startThread();
    		if (hungry) {
    			g.setHungry();
    		}
    	}
    	
    	else {	// It's a waiter; ignore "hungry" checkbox (will be "on break" later)
    		WaiterRole w = new WaiterRole(name);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		WaiterGui g = new WaiterGui(w, mainGui, Waiters.size());
    		w.setGui(g);
    		w.startThread();
    		
    		mainGui.animationPanel.addGui(g);
    		Waiters.add(w);
    		
    		host.addWaiter(w);
    	}
    }
}