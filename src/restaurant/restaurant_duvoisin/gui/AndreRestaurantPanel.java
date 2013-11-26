package restaurant.restaurant_duvoisin.gui;

import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.roles.AndreCashierRole;
import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import restaurant.restaurant_duvoisin.roles.AndreHostRole;
import restaurant.restaurant_duvoisin.roles.AndreMarketRole;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;

import javax.swing.*;

import base.BaseRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class AndreRestaurantPanel extends JPanel {
	static AndreRestaurantPanel instance;

    //Host, cook, waiters and customers
	private AndreHostRole host = new AndreHostRole("Kevin G");
	private AndreCookRole cook = new AndreCookRole("Cooking Mama");
	private Vector<Waiter> waiters = new Vector<Waiter>();
    private Vector<AndreCustomerRole> customers = new Vector<AndreCustomerRole>();
    private Vector<AndreMarketRole> markets = new Vector<AndreMarketRole>();
    private AndreCashierRole cashier = new AndreCashierRole("Cashier");

    private JPanel restLabel = new JPanel();
    private ListPanel addMembers = new ListPanel(this);
    private JPanel group = new JPanel();

    private AndreRestaurantGui gui; //reference to main gui

    public AndreRestaurantPanel(AndreRestaurantGui gui) {
        this.gui = gui;
        this.instance = this;
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(addMembers);

        initRestLabel();
        add(restLabel);
        add(group);
        
        CookGui g = new CookGui(cook, gui);
		gui.animationPanel.addGui(g);
		cook.setGui(g);
        
        //host.startThread();
        //cook.startThread();
        //cashier.startThread();
        
        for(int i = 1; i <= 3; i++) {
        	AndreMarketRole market = new AndreMarketRole("Market #" + i, cook);
        	//market.startThread();
        	market.setCashier(cashier);
        	markets.add(market);
        	cook.addMarket(market);
        }
    }
    
    public static AndreRestaurantPanel getInstance() {
		return instance;
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
            AndreCustomerRole temp = customers.get(i);
            if (temp.getName() == name)
                gui.updateCustomerPanel(temp, list, customers);
        }
    }
    
    public void showWaiterInfo(String type, String name, ArrayList<JButton> list) {
        for (int i = 0; i < waiters.size(); i++) {
            Waiter temp = waiters.get(i);
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
    public void addPerson(BaseRole role) {
    	//ANDRE: Fix Waiter Adding
    	if (role instanceof AndreCustomerRole) {
    		//AndreCustomerRole c = new AndreCustomerRole(name);	
    		CustomerGui g = new CustomerGui((AndreCustomerRole) role, gui);

    		gui.animationPanel.addGui(g);
    		((AndreCustomerRole)role).setHost(host);
    		((AndreCustomerRole)role).setGui(g);
    		customers.add(((AndreCustomerRole)role));
    		//c.startThread();
    	} else if(role instanceof AndreWaiterRole || role instanceof AndreSharedWaiterRole) {
    		// Odd = Shared, Even = Normal
//    		if(waiters.size() % 2 == 0) {
//    			AndreSharedWaiterRole w = new AndreSharedWaiterRole(host, cook, cashier, name);
//    			WaiterGui g = new WaiterGui(w, gui);
//	    		
//	    		gui.animationPanel.addGui(g);
//	    		w.setGui(g);
//	    		waiters.add(w);
//	    		host.addWaiter(w);
//	            //w.startThread();
//    		} else {
//	    		AndreWaiterRole w = new AndreWaiterRole(host, cook, cashier, name);
//	    		WaiterGui g = new WaiterGui(w, gui);
//	    		
//	    		gui.animationPanel.addGui(g);
//	    		w.setGui(g);
//	    		waiters.add(w);
//	    		host.addWaiter(w);
//	            //w.startThread();
//    		}
    	}
    }
    
    public void pauseAgents() {
    	/*
    	host.pauseBaseAgent();
    	for(Waiter w : waiters)
    		w.pauseBaseAgent();
		cook.pauseBaseAgent();
		for(AndreCustomerRole c : customers)
			c.pauseBaseAgent();
		for(AndreMarketRole m : markets)
			m.pauseBaseAgent();
		cashier.pauseBaseAgent();
		*/
		host.msgPauseScheduler();
		for(Waiter w : waiters)
			w.msgPauseScheduler();
		cook.msgPauseScheduler();
		for(AndreCustomerRole c : customers)
			c.msgPauseScheduler();
		for(AndreMarketRole m : markets)
			m.msgPauseScheduler();
		cashier.msgPauseScheduler();
    }
    
    public void resumeAgents() {
    	/*
    	host.resumeBaseAgent();
    	for(Waiter w : waiters)
    		w.resumeBaseAgent();
		cook.resumeBaseAgent();
		for(AndreCustomerRole c : customers)
			c.resumeBaseAgent();
		for(AndreMarketRole m : markets)
			m.resumeBaseAgent();
		cashier.resumeBaseAgent();
		*/
		host.msgResumeScheduler();
		for(Waiter w : waiters)
			w.msgResumeScheduler();
		cook.msgResumeScheduler();
		for(AndreCustomerRole c : customers)
			c.msgResumeScheduler();
		for(AndreMarketRole m : markets)
			m.msgResumeScheduler();
		cashier.msgResumeScheduler();
    }
}
