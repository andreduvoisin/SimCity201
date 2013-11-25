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

    private JPanel mMenuPanel;
    private SmilehamAgentPanel mAgentPanel;
    
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
//		mCashier.startThread();
	    
        //rest label - includes menu, host, and cook info
        mMenuPanel = new JPanel();
        	mMenuPanel.setLayout(new BorderLayout());
        	mMenuPanel.setBorder(BorderFactory.createTitledBorder("Menu Panel"));
        	
        	JLabel staffLabel = new JLabel();
	        staffLabel.setText(
	                "<html><h3><u>Tonight's Staff</u></h3><table>"
	                + "<tr><td>Host:</td><td>" + mHost.getName() + "</td></tr></table>"
	                		+ "<h3><u> Menu</u></h3>"
	                		+ "<table><tr><td>Steak</td><td>$" + Menu.cSTEAK_PRICE + "</td></tr>"
	                		+ "<tr><td>Chicken</td><td>$" + Menu.cCHICKEN_PRICE + "</td></tr>"
	                		+ "<tr><td>Salad</td><td>$" + Menu.cSALAD_PRICE + "</td></tr>"
	                		+ "<tr><td>Pizza</td><td>$" + Menu.cPIZZA_PRICE + "</td></tr></table><br></html>");
	        mMenuPanel.add(staffLabel, BorderLayout.CENTER);
	        mMenuPanel.add(new JLabel("           "), BorderLayout.EAST);
	        mMenuPanel.add(new JLabel("           "), BorderLayout.WEST);
        
	    //mCustomerWrapperPanel
	    mAgentPanel = new SmilehamAgentPanel(this);
	        
        add(mAgentPanel); //on left
        add(mMenuPanel); //on right
    }
    
    
    
    //GETTERS AND SETTERS

    static public SmilehamHostRole getHost() {
		return mHost;
	}

	public Vector<SmilehamCustomerRole> getCustomers() {
		return mCustomers;
	}
	
	public SmilehamAgentPanel getAgentPanel(){
		return mAgentPanel;
	}

	static public SmilehamCookRole getCook() {
		return mCook;
	}

	static public SmilehamCashierRole getCashier() {
		return mCashier;
	}

}
