package restaurant.restaurant_duvoisin.gui;

import restaurant.restaurant_duvoisin.CashierAgent;
import restaurant.restaurant_duvoisin.CookAgent;
import restaurant.restaurant_duvoisin.CustomerAgent;
import restaurant.restaurant_duvoisin.HostAgent;
import restaurant.restaurant_duvoisin.MarketAgent;
import restaurant.restaurant_duvoisin.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
	private HostAgent host = new HostAgent("Kevin G");
	private CookAgent cook = new CookAgent("Cooking Mama");
	private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    private CashierAgent cashier = new CashierAgent("Cashier");

    private JPanel restLabel = new JPanel();
    private ListPanel addMembers = new ListPanel(this);
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(addMembers);

        initRestLabel();
        add(restLabel);
        add(group);
        
        CookGui g = new CookGui(cook, gui);
		gui.animationPanel.addGui(g);
		cook.setGui(g);
        
        host.startThread();
        cook.startThread();
        cashier.startThread();
        
        for(int i = 1; i <= 3; i++) {
        	MarketAgent market = new MarketAgent("Market #" + i, cook);
        	market.startThread();
        	market.setCashier(cashier);
        	markets.add(market);
        	cook.addMarket(market);
        }
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr><tr><td>cook:</td><td>" + cook.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("        "), BorderLayout.EAST);
        restLabel.add(new JLabel("        "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showCustomerInfo(String type, String name, ArrayList<JCheckBox> list) {
        for (int i = 0; i < customers.size(); i++) {
            CustomerAgent temp = customers.get(i);
            if (temp.getName() == name)
                gui.updateCustomerPanel(temp, list, customers);
        }
    }
    
    public void showWaiterInfo(String type, String name, ArrayList<JButton> list) {
        for (int i = 0; i < waiters.size(); i++) {
            WaiterAgent temp = waiters.get(i);
            if (temp.getName() == name) { }
                gui.updateWaiterPanel(temp, list, waiters);
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {
    	if (type.equals("Customer")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	} else if(type.equals("Waiter")) {
    		WaiterAgent w = new WaiterAgent(host, cook, cashier, name);
    		WaiterGui g = new WaiterGui(w, gui);
    		
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
    		waiters.add(w);
    		host.addWaiter(w);
            w.startThread();
    	}
    }
    
    public void pauseAgents() {
    	host.pauseBaseAgent();
    	for(WaiterAgent w : waiters)
    		w.pauseBaseAgent();
		cook.pauseBaseAgent();
		for(CustomerAgent c : customers)
			c.pauseBaseAgent();
		for(MarketAgent m : markets)
			m.pauseBaseAgent();
		cashier.pauseBaseAgent();
		
		host.msgPauseScheduler();
		for(WaiterAgent w : waiters)
			w.msgPauseScheduler();
		cook.msgPauseScheduler();
		for(CustomerAgent c : customers)
			c.msgPauseScheduler();
		for(MarketAgent m : markets)
			m.msgPauseScheduler();
		cashier.msgPauseScheduler();
    }
    
    public void resumeAgents() {
    	host.resumeBaseAgent();
    	for(WaiterAgent w : waiters)
    		w.resumeBaseAgent();
		cook.resumeBaseAgent();
		for(CustomerAgent c : customers)
			c.resumeBaseAgent();
		for(MarketAgent m : markets)
			m.resumeBaseAgent();
		cashier.resumeBaseAgent();
		
		host.msgResumeScheduler();
		for(WaiterAgent w : waiters)
			w.msgResumeScheduler();
		cook.msgResumeScheduler();
		for(CustomerAgent c : customers)
			c.msgResumeScheduler();
		for(MarketAgent m : markets)
			m.msgResumeScheduler();
		cashier.msgResumeScheduler();
    }
}
