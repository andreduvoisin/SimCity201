package restaurant.restaurant_maggiyan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.restaurant_maggiyan.interfaces.Waiter;
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
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private MaggiyanHostRole host = new MaggiyanHostRole("Host");
    private MaggiyanCookRole cook = new MaggiyanCookRole("Cook"); 
    private MaggiyanCashierRole cashier = new MaggiyanCashierRole("Cashier", false);
    private MaggiyanMarketRole market1 = new MaggiyanMarketRole("Market 1"); 
    private MaggiyanMarketRole market2 = new MaggiyanMarketRole("Market 2"); 
    private MaggiyanMarketRole market3 = new MaggiyanMarketRole("Market 3"); 
    private CookGui cookGui = new CookGui(cook); 
    //private WaiterGui waiterGui = new WaiterGui(host);
 

    public Vector<MaggiyanCustomerRole> customers = new Vector<MaggiyanCustomerRole>();
    public Vector<Waiter> waiters = new Vector<Waiter>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
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
    
    
    public Vector<MaggiyanCustomerRole> getCust(){
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
    			return waiter.getGui(); 
    		}
    	}
    	return null; 
    }
    
    public MaggiyanHostRole getHost(){
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
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		MaggiyanCustomerRole c = new MaggiyanCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		c.setCashier(cashier);
    		customers.add(c);
    	
    	}
    	
    	else if (type.equals("Waiter")){
    		int waiterTypeNum = waiters.size(); 
    		
    		//Create new waiter agent and gui
    		if(waiterTypeNum%2 == 1){
	    		MaggiyanWaiterRole w = new MaggiyanWaiterRole(name, cook, host); 
	    		WaiterGui waiterGui = new WaiterGui(w, gui);
	    		
	    		gui.animationPanel.addGui(waiterGui);
	    		w.setCashier(cashier); 
	    		w.setGui(waiterGui);
	    		waiters.add(w); 
	
    		}
    		else{
    			MaggiyanSharedWaiterRole w = new MaggiyanSharedWaiterRole(name, cook, host); 
	    		WaiterGui waiterGui = new WaiterGui(w, gui);
	    		
	    		gui.animationPanel.addGui(waiterGui);
	    		w.setCashier(cashier); 
	    		w.setGui(waiterGui);
	    		waiters.add(w); 

    		}
    		
    	}
    }

}
