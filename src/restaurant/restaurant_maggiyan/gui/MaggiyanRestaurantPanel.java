package restaurant.restaurant_maggiyan.gui;

import java.awt.BorderLayout;

import base.PersonAgent.EnumJobType;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import base.PersonAgent;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;
import restaurant.restaurant_maggiyan.roles.MaggiyanCashierRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanHostRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanMarketRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanSharedWaiterRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanWaiterRole;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class MaggiyanRestaurantPanel extends JPanel {
	
	static MaggiyanRestaurantPanel me; 
	
    //Host, cook, waiters and customers
    public MaggiyanHostRole host = new MaggiyanHostRole("Host");
    public MaggiyanCookRole cook = new MaggiyanCookRole("Cook"); 
    public MaggiyanCashierRole cashier = new MaggiyanCashierRole("Cashier", false);
    private MaggiyanMarketRole market1 = new MaggiyanMarketRole("Market 1"); 
    private MaggiyanMarketRole market2 = new MaggiyanMarketRole("Market 2"); 
    private MaggiyanMarketRole market3 = new MaggiyanMarketRole("Market 3"); 
    private MaggiyanCookGui cookGui = new MaggiyanCookGui(cook); 
    //private WaiterGui waiterGui = new WaiterGui(host);
 

    public Vector<MaggiyanCustomerRole> customers = new Vector<MaggiyanCustomerRole>();
    public Vector<MaggiyanWaiter> waiters = new Vector<MaggiyanWaiter>();

    private JPanel group = new JPanel();

    private MaggiyanRestaurantGui gui; //reference to main gui

    public MaggiyanRestaurantPanel(MaggiyanRestaurantGui gui) {
        this.gui = gui;
        this.me = this; 
//        
//        cook.setMarket(market1);
//        cook.setMarket(market2);
//        cook.setMarket(market3);
//        market1.setCashier(cashier); 
//        market2.setCashier(cashier); 
//        market3.setCashier(cashier); 
        
        cook.setGui(cookGui); 
        gui.animationPanel.addGui(cookGui);
        
        //TEST(); 
    }
    
    public void TEST(){
    	
    	//People
    	PersonAgent host = new PersonAgent(EnumJobType.RESTAURANT, 1000.00, "Maggi Host"); 
    	PersonAgent customer = new PersonAgent(EnumJobType.RESTAURANT, 400.00, "Maggi Customer");
    	PersonAgent cook = new PersonAgent(EnumJobType.RESTAURANT, 300.00, "Maggi Cook");
    	PersonAgent waiter = new PersonAgent(EnumJobType.RESTAURANT, 300.00, "Maggi Waiter");
    	PersonAgent cashier = new PersonAgent(EnumJobType.RESTAURANT, 800.00, "Maggi Cashier");
    	
    	//Roles 
    	MaggiyanHostRole hostRole = new MaggiyanHostRole(host); 
    	MaggiyanCustomerRole custRole = new MaggiyanCustomerRole(customer); 
    	MaggiyanCookRole cookRole = new MaggiyanCookRole(cook); 
    	MaggiyanWaiterRole waiterRole = new MaggiyanWaiterRole(waiter, cookRole, hostRole); 
    	MaggiyanCashierRole cashierRole = new MaggiyanCashierRole(cashier); 
    	
    	
    	//Guis
    	MaggiyanCustomerGui custgui = new MaggiyanCustomerGui(custRole, gui); 
    	MaggiyanWaiterGui waitergui = new MaggiyanWaiterGui(waiterRole, gui); 
    	MaggiyanCookGui cookgui = new MaggiyanCookGui(cookRole);
    	
    	//Preliminary setters
    	customer.addRole(custRole, true);
    	gui.animationPanel.addGui(custgui);
    	custRole.setHost(hostRole);
    	custRole.setCashier(cashierRole); 
    	custRole.setGui(custgui);
    	customers.add(custRole); 
    	custgui.DoGoToFrontOfLine();
    	
    	//Restaurant simulation
    	custRole.gotHungry();
    	hostRole.msgIWantFood(custRole);
    	hostRole.msgIAmHere(waiterRole);
    	
    }
    
    public static MaggiyanRestaurantPanel getRestPanel(){
    	return me; 
    }
  
    
    public Vector<MaggiyanCustomerRole> getCust(){
    	return customers;
    }
    
    public Vector<MaggiyanWaiter> getWaiter(){
    	return waiters;
    }
    
    public Vector<MaggiyanWaiter> getWaiters(){
    	return waiters;
    }
    
    public MaggiyanWaiterGui getWaiterGui(String name){
    	for(MaggiyanWaiter waiter: waiters){
    		if(waiter.getName() == name){
    			return waiter.getGui(); 
    		}
    	}
    	return null; 
    }
    
    public MaggiyanHostRole getHost(){
    	return host; 
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
                MaggiyanCustomerRole temp = customers.get(i);
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
    public void addCustomer(MaggiyanCustomerRole c){
    	MaggiyanCustomerGui g = new MaggiyanCustomerGui(c, gui);

		gui.animationPanel.addGui(g);
		c.setHost(host);
		c.setGui(g);
		c.setCashier(cashier);
		customers.add(c);
    }
    
    public void addWaiter(MaggiyanWaiterRole w){
		MaggiyanWaiterGui waiterGui = new MaggiyanWaiterGui(w, gui);
		
		gui.animationPanel.addGui(waiterGui);
		w.setCashier(cashier); 
		w.setGui(waiterGui);
		waiters.add(w);

    }
    
    public void addSharedWaiter(MaggiyanSharedWaiterRole w){
		MaggiyanWaiterGui waiterGui = new MaggiyanWaiterGui(w, gui);
		
		gui.animationPanel.addGui(waiterGui);
		w.setCashier(cashier); 
		w.setGui(waiterGui);
		waiters.add(w);

    }

}
