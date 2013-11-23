package restaurant.restaurant_smileham.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.agents.CashierAgent;
import restaurant.restaurant_smileham.agents.CookAgent;
import restaurant.restaurant_smileham.agents.CustomerAgent;
import restaurant.restaurant_smileham.agents.HostAgent;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent mHost;
    private CookAgent mCook;
    private CashierAgent mCashier;
    
    private Vector<CustomerAgent> mCustomers = new Vector<CustomerAgent>();

    private JPanel mMenuPanel;
    private AgentPanel mAgentPanel;
    
    private RestaurantGui mGUI; //reference to main gui

    //CONSTRUCTOR
    public RestaurantPanel(RestaurantGui gui) {
    	//super
    	mGUI = gui;
    	this.setLayout(new GridLayout(1, 2, 20, 20));
    	
    	//Host
    	mHost = new HostAgent("Shane", mGUI);
    	
		//Add Cook
		mCook = new CookAgent("Mr. Ramen", mGUI);
		
		//Add Cashier
		mCashier = new CashierAgent("Ke$$$ha", mGUI);
		mCashier.startThread();
	    
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
	    mAgentPanel = new AgentPanel(this);
	        
        add(mAgentPanel); //on left
        add(mMenuPanel); //on right
    }
    
    
    
    //GETTERS AND SETTERS

	public HostAgent getHost() {
		return mHost;
	}

	public Vector<CustomerAgent> getCustomers() {
		return mCustomers;
	}
	
	public RestaurantGui getGui(){
		return mGUI;
	}
	
	public AgentPanel getAgentPanel(){
		return mAgentPanel;
	}

	public CookAgent getCook() {
		return mCook;
	}

	public CashierAgent getCashier() {
		return mCashier;
	}

}
