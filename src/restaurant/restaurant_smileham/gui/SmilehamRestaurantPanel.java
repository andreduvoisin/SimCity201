package restaurant.restaurant_smileham.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class SmilehamRestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private static SmilehamHostRole mHost;
    private static SmilehamCookRole mCook;
    private static SmilehamCashierRole mCashier;
    
    private Vector<SmilehamCustomerRole> mCustomers = new Vector<SmilehamCustomerRole>();

//    private SmilehamAgentPanel mAgentPanel;
    
    static public SmilehamAnimationPanel mAnimationPanel; //reference to main gui

    //CONSTRUCTOR
    public SmilehamRestaurantPanel(SmilehamAnimationPanel animationPanel) {
    	//super
    	mAnimationPanel = animationPanel;
    	this.setLayout(new GridLayout(1, 2, 20, 20));
    	
    	//Host
    	mHost = new SmilehamHostRole("Shane", mAnimationPanel);
    	
		//Add Cook
		mCook = new SmilehamCookRole("Mr. Ramen", mAnimationPanel);
		
		//Add Cashier
		mCashier = new SmilehamCashierRole("Ke$$$ha", mAnimationPanel);
//        
//	    //mCustomerWrapperPanel
//	    mAgentPanel = new SmilehamAgentPanel(this);
//	        
//        add(mAgentPanel); //on left
    }
    
    
    
    //GETTERS AND SETTERS

    static public SmilehamHostRole getHost() {
		return mHost;
	}

	public Vector<SmilehamCustomerRole> getCustomers() {
		return mCustomers;
	}
	
//	public SmilehamAgentPanel getAgentPanel(){
//		return mAgentPanel;
//	}

	static public SmilehamCookRole getCook() {
		return mCook;
	}

	static public SmilehamCashierRole getCashier() {
		return mCashier;
	}

}
