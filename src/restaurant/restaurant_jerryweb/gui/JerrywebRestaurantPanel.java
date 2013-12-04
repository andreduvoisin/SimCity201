package restaurant.restaurant_jerryweb.gui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.intermediate.interfaces.RestaurantInterface;
import restaurant.restaurant_jerryweb.JerrywebCashierRole;
import restaurant.restaurant_jerryweb.JerrywebCookRole;
import restaurant.restaurant_jerryweb.JerrywebCustomerRole;
import restaurant.restaurant_jerryweb.JerrywebHostRole;
import restaurant.restaurant_jerryweb.JerrywebRSWaiterRole;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole;
import restaurant.restaurant_jerryweb.interfaces.Waiter;
import base.BaseRole;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class JerrywebRestaurantPanel extends JPanel  {
	static final int rows = 1;
	static final int cols = 2;
	static final int hSpacing = 5;
	static final int vSpacing = 5;
	int sel = 0;
    //Host, cook, waiters and customers
    public static JerrywebHostRole host=  new JerrywebHostRole("Sarah");
    public HostGui hostGui = new HostGui(host);
    //public PersonAgent mPerson = new PersonAgent(EnumJobType.RESTAURANT,20,"Joe");

    
	public static JerrywebCookRole cook;
    public static JerrywebCashierRole cashier = new JerrywebCashierRole("Ted Cruz");
    
    private Vector<JerrywebCustomerRole> customers = new Vector<JerrywebCustomerRole>();
    private Vector<Waiter> waiters = new Vector<Waiter>();
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterListPanel WaiterPanel = new WaiterListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private JPanel group2 = new JPanel();

    public static JerrywebRestaurantGui gui; //reference to main gui
   
    public JerrywebRestaurantPanel(JerrywebRestaurantGui gui){//,JerrywebHostRole h) {
    	//host = h;
        this.gui = gui;
        host.setGui(hostGui);

        gui.animationPanel.addGui(hostGui);
        //host.startThread();
        /*
        for(int i=0;i<3; i++){
           //private HostGui hostGui = new HostGui(host);
        	JerrywebMarketRole market = new JerrywebMarketRole(null);
        	market.setCook(cook);
        	cook.addMarket(market);
        	market.setCashier(cashier);
        	//market.startThread();
        }*/
            
        //JerrywebWaiterRole w = new JerrywebWaiterRole(mPerson);
        //this.addWaiter(w);
        

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
                JerrywebCustomerRole temp = customers.get(i);
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
    
    public JerrywebCustomerRole getCustomer(int index){
    	return customers.get(index);
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param subRole name of person
     */
    public static void addWaiter(JerrywebWaiterRole w){
    	//JerrywebWaiterRole w = (JerrywebWaiterRole) role;
		WaiterGui wg = new WaiterGui(w, gui, host);//may need to fix this
		w.setGui(wg);
		host.addWaiter(w);
		w.setHost(host);
		w.setCook(cook);
    	w.setCashier(cashier);
    	gui.animationPanel.addGui(wg);
    }
    
    public static void addRSWaiter(JerrywebRSWaiterRole rsw){
    	//JerrywebRSWaiterRole rsw = (JerrywebRSWaiterRole) role;
		WaiterGui wg = new WaiterGui(rsw, gui, host);//may need to fix this
		rsw.setGui(wg);
		host.addWaiter(rsw);
		rsw.setHost(host);
		rsw.setCook(cook);
    	rsw.setCashier(cashier);
    	gui.animationPanel.addGui(wg);
    }
    
    public void addPerson(BaseRole role) {

    	if (role instanceof JerrywebCustomerRole) {
    		JerrywebCustomerRole c = (JerrywebCustomerRole) role;	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		g.setHungry();
    	}
    	else if(role instanceof JerrywebWaiterRole){
    		sel++;
    		if((sel)%2 == 0){
    			JerrywebRSWaiterRole rsw = (JerrywebRSWaiterRole) role;
    			WaiterGui wg = new WaiterGui(rsw, gui, host);//may need to fix this
    			rsw.setGui(wg);
    			host.addWaiter(rsw);
    			rsw.setHost(host);
    			rsw.setCook(cook);
            	rsw.setCashier(cashier);
            	gui.animationPanel.addGui(wg);
            	
    		}
    		else{ 
    			JerrywebWaiterRole w = (JerrywebWaiterRole)role;
    			WaiterGui wg = new WaiterGui(w, gui, host);//may need to fix this
    			w.setGui(wg);
    			host.addWaiter(w);
    			w.setHost(host);
    			w.setCook(cook);
    			w.setCashier(cashier);
    			gui.animationPanel.addGui(wg);
    
    		}
    	}
    	
    	else if (role instanceof JerrywebHostRole){
    		host = (JerrywebHostRole) role;
    	}
    	
       	else if (role instanceof JerrywebCookRole){
    		cook = (JerrywebCookRole) role;
    	}
    	
       	else if (role instanceof JerrywebCashierRole){
    		cashier = (JerrywebCashierRole) role;
    	}
    	
    }

}
