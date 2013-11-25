package restaurant.restaurant_smileham.gui;

import java.util.Timer;
import java.util.Vector;

import javax.swing.JPanel;

import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.interfaces.Customer;
import restaurant.restaurant_smileham.interfaces.Host;
import restaurant.restaurant_smileham.interfaces.Waiter;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import base.BaseRole;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
@SuppressWarnings("serial")
public class SmilehamAgentPanel extends JPanel {

	//instance variables
    private static SmilehamRestaurantPanel mRestPanel;
    private Timer mTimer;
    
	//HACKS
	
//    private SmilehamRestaurantGui mGUI;
    private SmilehamAnimationPanel mAnimationPanel;

    /**
     * Constructor for AgentPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     */
    public SmilehamAgentPanel(SmilehamRestaurantPanel rp, SmilehamAnimationPanel animationPanel) {
    	//initializations
        mRestPanel = rp;
        mAnimationPanel = animationPanel;
        mTimer = new Timer();
    }
    
    public void pauseSimulation(){
    	Vector<BaseRole> roles = new Vector<BaseRole>();
    	
		SmilehamHostRole host = mRestPanel.getHost();
		roles.add(host);
		roles.add((SmilehamCookRole) SmilehamRestaurantPanel.getCook());
		for (Customer customer : host.getWaitingCustomers()){
			SmilehamCustomerRole customeragent = (SmilehamCustomerRole) customer;
			roles.add(customeragent);
		}
    	for (Waiter waiter : host.getWaiters()){
    		SmilehamWaiterRole waiteragent = (SmilehamWaiterRole) waiter;
    		roles.add(waiteragent);
    		for (Order order : waiteragent.getOrders()){
    			roles.add((SmilehamCustomerRole) order.mCustomer);
    		}
    	}

//    	if (!host.isPaused()){
//    		for (Agent agent : roles){
//    			agent.pause();
//    		}
//    	}else{
//    		for (Agent agent : roles){
//    			agent.restart();
//    		}
//    	}
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public static void addPerson(BaseRole role) {
    	
    	if (role instanceof SmilehamCustomerRole){
    		SmilehamCustomerRole customer = (SmilehamCustomerRole) role;
//    		String name = mAddCustomerField.getText();
//    		boolean hungry = mHungryCB.isSelected();
//    		boolean hungry = true;
//            if (hungry) 
            	customer.msgGotHungry();
            
            //Add Row
//            CustomerRowButton crb = new CustomerRowButton(customer, name, hungry);
//            mCustomerView.add(crb);
            
            
//            if (hungry) customer.msgGotHungry();
    		mRestPanel.getCustomers().add(customer);
    	}
    	else if (role instanceof SmilehamWaiterRole){
    		SmilehamWaiterRole waiter = (SmilehamWaiterRole) role;
//    		String name = waiter.getName();
    		Host host = waiter.getHost();
            host.msgAddWaiter((Waiter)waiter);
            
//            WaiterRowButton wrb = new WaiterRowButton(waiter, name);
//            mWaiterView.add(wrb);
    	}
    	else{
    	}
//    	validate();
    }
    
    
    public String toString() {
		return "[AgentPanel]";
	}
    
    public void print(String string){
    	System.out.println(this + " " + string);
    }
    
}

