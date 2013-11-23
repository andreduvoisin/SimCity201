package restaurant.restaurant_cwagoner.gui;

import restaurant.restaurant_cwagoner.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel implements ActionListener {

    private RestaurantGui mainGui; // Reference to main GUI
    
    // Host, cook, waiters, customers, markets
    private HostAgent host = new HostAgent("Sarah");
    private CashierAgent cashier = new CashierAgent();
    private CookAgent cook = new CookAgent();

    private List<CustomerAgent> Customers = new ArrayList<CustomerAgent>();
    private List<WaiterAgent> Waiters = new ArrayList<WaiterAgent>();
    private List<MarketAgent> Markets = new ArrayList<MarketAgent>();

    // Stores restaurant menu
    private JPanel menuPanel = new JPanel();    

    private JButton pauseButton = new JButton("Pause");
    
    JPanel buttonPanel = new JPanel();    
    JLabel numMarkets = new JLabel("Number of Markets: 0");    
    JButton addMarketButton = new JButton("Add Market");
    
    // Lists of customers and waiters
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    

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
        
    	JPanel Buttons = new JPanel();
    	Buttons.setLayout(new GridLayout(2, 1));
    	pauseButton.addActionListener(this);
    	Buttons.add(pauseButton);
        
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.add(pauseButton);
        buttonPanel.add(numMarkets);
        addMarketButton.addActionListener(this);
        buttonPanel.add(addMarketButton);

        
        add(menuPanel);
        add(buttonPanel);
        add(customerPanel);
        add(waiterPanel);
    }
    
    public void actionPerformed(ActionEvent e) {
    	// Added market with "Add" button
        if (e.getSource().equals(addMarketButton)) {
        	MarketAgent m = new MarketAgent(Markets.size());
        	m.startThread();
        	
        	Markets.add(m);
        	cook.addMarket(m);
        	
        	numMarkets.setText("Number of markets: " + Markets.size());
        }
        else if (e.getSource().equals(pauseButton)) {
        	if (pauseButton.getText().equals("Pause")) {	// Currently running; pause
        		pauseAgents();
        		pauseButton.setText("Unpause");
        	}
        	else {	// Currently paused; unpause
        		unpauseAgents();
        		pauseButton.setText("Pause");
        	}
        }
    }
    
    public void pauseAgents() {
    	for (CustomerAgent c : Customers) {
    		c.pause();
    	}
    	
    	for (WaiterAgent w : Waiters) {
    		w.pause();
    	}
    	
    	host.pause();
    	cook.pause();
    }
    
    public void unpauseAgents() {
    	for (CustomerAgent c : Customers) {
    		c.unpause();
    	}
    	
    	for (WaiterAgent w : Waiters) {
    		w.unpause();
    	}
    	
    	host.unpause();
    	cook.unpause();
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
     * When a customer or waiter is clicked, this function calls
     * updateInfoPanel() from the main GUI so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, int index) {
    	if (type.equals("Customers")) {
			mainGui.updateInfoPanel(Customers.get(index));
			return;
    	}
    	else {	// It's a waiter
    		mainGui.updateInfoPanel(Waiters.get(index));
    	}
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, Boolean hungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
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
    		WaiterAgent w = new WaiterAgent(name);
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
    
	public void makeHungry(int index) {
		Customers.get(index).getGui().setHungry();
		mainGui.updateInfoPanel(Customers.get(index));
		return;
	}
	
	// Accessors
	public int numCustomers() {
		return customerPanel.numPeople();
	}
	
	public String getCustomerName(int i) {
		return customerPanel.getNameOf(i);
	}
	
	public void enableCustomer(int i) {
		customerPanel.enableCust(i);
	}
    
    /**
     * Gui asks for break
     */
    public void askForBreak(int index) {
    	Waiters.get(index).msgGuiAskedForBreak();
    	mainGui.updateInfoPanel(Waiters.get(index));
    }
	
	public void waiterBreak(boolean onBreak, WaiterAgent w) {
		for (int i = 0; i < Waiters.size(); i++) {
			if (Waiters.get(i).equals(w)) {
				waiterPanel.setWaiterBreak(onBreak, i);
				break;
			}
		}
	}
}
