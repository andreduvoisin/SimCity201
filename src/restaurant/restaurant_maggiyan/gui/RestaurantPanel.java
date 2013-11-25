package restaurant_maggiyan.gui;

import restaurant_maggiyan.CashierAgent;
import restaurant_maggiyan.CookAgent;
import restaurant_maggiyan.CustomerAgent;
import restaurant_maggiyan.HostAgent;
import restaurant_maggiyan.MarketAgent;
import restaurant_maggiyan.SharedDataWaiterAgent;
import restaurant_maggiyan.WaiterAgent;
import restaurant_maggiyan.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Host");
    private CookAgent cook = new CookAgent("Cook"); 
    private CashierAgent cashier = new CashierAgent("Cashier", false);
    private MarketAgent market1 = new MarketAgent("Market 1"); 
    private MarketAgent market2 = new MarketAgent("Market 2"); 
    private MarketAgent market3 = new MarketAgent("Market 3"); 
    private CookGui cookGui = new CookGui(cook); 
    //private WaiterGui waiterGui = new WaiterGui(host);
 

    public Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    public Vector<Waiter> waiters = new Vector<Waiter>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
        host.startThread();
        cook.setMarket(market1);
        cook.setMarket(market2);
        cook.setMarket(market3);
        market1.setCashier(cashier); 
        market2.setCashier(cashier); 
        market3.setCashier(cashier); 
        cook.setGui(cookGui); 
        gui.animationPanel.addGui(cookGui);
        
        setLayout(new GridLayout(1, 2, 0, 20));
        group.setLayout(new GridLayout(1, 2, 0, 10));
        setBackground(Color.white); 
     
        
        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }
    
    public ListPanel getCustPanel(){
    	return customerPanel; 
    }
    
    public void pause(){
    	cook.pause(); 
    	host.pause();
    	cashier.pause();
    	for(Waiter waiter: waiters){
    		waiter.pause();
    	}
    	for(CustomerAgent cust: customers){
    		cust.pause(); 
    	}
    	market1.pause();
    	market2.pause();
    	market3.pause();
    	System.out.println("Paused");
    }
    
    public void restart(){
    	cook.restart(); 
    	host.restart();
    	cashier.restart();
    	for(Waiter waiter: waiters){
    		waiter.restart();
    	}
    	for(CustomerAgent cust: customers){
    		cust.restart(); 
    	}
    	market1.restart();
    	market2.restart();
    	market3.restart();
    	System.out.println("Restarted");
    }
    
    public Vector<CustomerAgent> getCust(){
    	return customers;
    }
    
    public Vector<Waiter> getWaiter(){
    	return waiters;
    }
    
    public Vector<Waiter> getWaiters(){
    	return waiters;
    }
    
    public WaiterGui getWaiterGui(String name){
    	for(Waiter waiter: waiters){
    		if(waiter.getName() == name){
    			return waiter.waiterGui; 
    		}
    	}
    	return null; 
    }
    
    public HostAgent getHost(){
    	return host; 
    }
    
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    
    private void initRestLabel() {
        JLabel label = new JLabel();
        
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
        

        
        //restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.setBounds(0, 0, 20, 10);
        //restLabel.setBackground(Color.white);
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
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		c.setCashier(cashier);
    		customers.add(c);
    		c.startThread();
    	}
    	
    	else if (type.equals("Waiter")){
    		int waiterTypeNum = 1 + (int)(Math.random()*10); 
    		//Create new waiter agent and gui
    		if(waiterTypeNum%2 == 0){
	    		WaiterAgent w = new WaiterAgent(name, cook, host); 
	    		WaiterGui waiterGui = new WaiterGui(w, gui);
	    		
	    		gui.animationPanel.addGui(waiterGui);
	    		w.setCashier(cashier); 
	    		w.setGui(waiterGui);
	    		waiters.add(w); 
	    		w.startThread(); 
    		}
    		else{
    			SharedDataWaiterAgent w = new SharedDataWaiterAgent(name, cook, host); 
	    		WaiterGui waiterGui = new WaiterGui(w, gui);
	    		
	    		gui.animationPanel.addGui(waiterGui);
	    		w.setCashier(cashier); 
	    		w.setGui(waiterGui);
	    		waiters.add(w); 
	    		w.startThread(); 
    		}
    		
    	}
    }

}
