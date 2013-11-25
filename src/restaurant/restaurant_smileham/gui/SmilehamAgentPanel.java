package restaurant.restaurant_smileham.gui;

import javax.swing.JPanel;

import restaurant.restaurant_smileham.interfaces.Host;
import restaurant.restaurant_smileham.interfaces.Waiter;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
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

    public SmilehamAgentPanel(SmilehamRestaurantPanel rp) {
    	//initializations
        mRestPanel = rp;
    }
    
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

