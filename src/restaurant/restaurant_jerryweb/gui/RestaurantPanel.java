package restaurant.restaurant_jerryweb.gui;


import restaurant.restaurant_jerryweb.CashierRole;
import restaurant.restaurant_jerryweb.CookRole;
import restaurant.restaurant_jerryweb.CustomerRole;
import restaurant.restaurant_jerryweb.HostRole;
import restaurant.restaurant_jerryweb.MarketRole;
import restaurant.restaurant_jerryweb.RSWaiterRole;
import restaurant.restaurant_jerryweb.WaiterRole;
import restaurant.restaurant_jerryweb.HostRole.MyWaiter;
import restaurant.restaurant_jerryweb.HostRole.WaiterState;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	static final int rows = 1;
	static final int cols = 2;
	static final int hSpacing = 5;
	static final int vSpacing = 5;
	int sel = 0;
    //Host, cook, waiters and customers
    private HostRole host = new HostRole("Sarah");
    private HostGui hostGui = new HostGui(host);

    
    private CookRole cook = new CookRole("Bob Sagget");
    private CashierRole cashier = new CashierRole("Ted Cruz");
    
    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
    private Vector<Waiter> waiters = new Vector<Waiter>();
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterListPanel WaiterPanel = new WaiterListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private JPanel group2 = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        host.setGui(hostGui);

        gui.animationPanel.addGui(hostGui);
        host.startThread();
        
        for(int i=0;i<3; i++){
           //private HostGui hostGui = new HostGui(host);
        	MarketRole market = new MarketRole("Market " + i);
        	market.setCook(cook);
        	cook.addMarket(market);
        	market.setCashier(cashier);
        	market.startThread();
        }
        cook.startThread();
        cashier.startThread();
        
        
        setLayout(new GridLayout(rows, cols,  hSpacing,  vSpacing));
        group.setLayout(new GridLayout(rows, cols,  hSpacing,  vSpacing));
        group2.setLayout(new GridLayout(rows, cols,  hSpacing,  vSpacing));

        group.add(customerPanel);
        group2.add(WaiterPanel);
        initRestLabel();
        add(restLabel);
        add(group2);
        add(group);
        
    }
   /* public void pauseAgents(){
    	host.pausePrint();
	if(!host.paused){
 
    		for(CustomerAgent c: customers){
    			c.paused();
    		}
    		for(WaiterAgent w: waiters){
    			w.paused();
    		}
    		host.paused();
    	}
    	else{
    		host.restart();
        	for(CustomerAgent c: customers){
        		c.restart();
        	}
        	for(WaiterAgent w: waiters){
    			w.restart();
    		}
    	}
    }*/
   public JCheckBox getLpChbx (){
	   	return customerPanel.getChbx();
   }
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }


    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                Waiter temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }
    
    public void RPorginalChbx() {
    	gui.originalChbx();
    }
    
    public void RpBreakChbx(int i){
    	waiters.get(i).msgAskForBreak();
    }
    
    public CustomerRole getCustomer(int index){
    	return customers.get(index);
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		CustomerRole c = new CustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		//c.setWaiter(waiter);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	if(type.equals("Waiters")){
    		sel++;
    		if((sel)%2 == 0){
    			RSWaiterRole rsw = new RSWaiterRole(name);
    			WaiterGui wg = new WaiterGui(rsw, gui, host);//may need to fix this
    			rsw.setGui(wg);
    			host.addWaiter(rsw);//hack to add one waiter
    			rsw.setHost(host);
    			rsw.setCook(cook);
            	rsw.setCashier(cashier);
            	gui.animationPanel.addGui(wg);
            	rsw.startThread();
            	
    		}
    		else{ 
    			WaiterRole w = new WaiterRole(name);
    			WaiterGui wg = new WaiterGui(w, gui, host);//may need to fix this
    			w.setGui(wg);
    			host.addWaiter(w);//hack to add one waiter
    			w.setHost(host);
    			w.setCook(cook);
    			w.setCashier(cashier);
    			gui.animationPanel.addGui(wg);
    			w.startThread();
    		}
    	}
    }

}
