package restaurant.restaurant_xurex.gui;

import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.RexCustomerRole;
import restaurant.restaurant_xurex.RexHostRole;
import restaurant.restaurant_xurex.agents.CashierAgent;
import restaurant.restaurant_xurex.agents.CookAgent;
import restaurant.restaurant_xurex.agents.CustomerAgent;
import restaurant.restaurant_xurex.agents.HostAgent;
import restaurant.restaurant_xurex.agents.MarketAgent;
import restaurant.restaurant_xurex.agents.WaiterAgent1;
import restaurant.restaurant_xurex.agents.WaiterAgent2;
import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Cook;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Host;
import restaurant.restaurant_xurex.interfaces.Market;
import restaurant.restaurant_xurex.interfaces.Waiter;

import javax.swing.*;

import base.Agent;

import java.awt.*;
//import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RexRestaurantPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	//Reference to Main Gui
    private RexRestaurantGui gui; 
	//Agents upon Creation
    private Host host = new RexHostRole();
    private Cook cook = new RexCookRole(); 
    private Cashier cashier = new RexCashierRole();
    
    private CookGui cookGui = new CookGui(cook);
    
    //MarketAgent Constructor (Name, Cook, Quantity of: Steak, Chicken, Salad, Pizza);
    private MarketAgent market1 = new MarketAgent("Market1", cook, cashier, 7, 7, 7, 7);
    private MarketAgent market2 = new MarketAgent("Market2", cook, cashier, 8, 8, 8, 8);
    private MarketAgent market3 = new MarketAgent("Market3", cook, cashier, 4, 4, 9, 4);

    private Vector<Customer> customers = new Vector<Customer>();
    private Vector<Waiter> waiters = new Vector<Waiter>();
    
    //private Vector<Agent> allAgents = new Vector<Agent>();
    /*
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private JButton pause = new JButton("Pause");
    */
    public RexRestaurantPanel(RexRestaurantGui gui) {
        this.gui = gui;
        /*
    	allAgents.add(host);
    	allAgents.add(cook);
    	allAgents.add(cashier);
    	allAgents.add(market1);
    	allAgents.add(market2);
    	allAgents.add(market3);
    	*/
    	cookGui.setGui(gui);
    	
    	//cook.setGui(gui);
    	cook.setGui(cookGui);
    	cook.addMarket(market1);
    	cook.addMarket(market2);
    	cook.addMarket(market3);
    	
    	gui.animationPanel.addGui(cookGui); 
    	/*
        host.startThread();
        cook.startThread();
        cashier.startThread();
        market1.startThread();
        market2.startThread();
        market3.startThread();
		*/
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        /*
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(getCustomerPanel());
        group.add(getWaiterPanel());

        initRestLabel();
        Dimension restLabelDim = new Dimension(200,350);
        restLabel.setPreferredSize(restLabelDim);
        restLabel.setMinimumSize(restLabelDim);
        restLabel.setMaximumSize(restLabelDim);
        add(restLabel);
        add(group);
        */
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    /*
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName()
                +"<tr><td>Cook:</td><td>" + cook.getName()
                +"<tr><td>Cashier:</td><td>" + cashier.getName()
                +"</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99"
                + "</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        /*
        pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		        	if(pause.getText().equalsIgnoreCase("Pause")){
		        		pause.setText("Restart"); 
		        		gui.animationPanel.pause();
		        		System.out.print("PAUSED\n");
		        		for(Agent a: allAgents){
		        			a.pause();
		        		}
		        	}
		        	else if(pause.getText().equalsIgnoreCase("Restart")){
		        		pause.setText("Pause");
		        		gui.animationPanel.restart();
		        		System.out.print("RESTARTED\n");
		        		for(Agent a: allAgents){
		        			a.restart();
		        		}
		        	}	
			}
		});
        restLabel.add(pause, BorderLayout.NORTH);
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
	*/
    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    /*public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }*/

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RexCustomerRole c = new RexCustomerRole();	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    		//allAgents.add(c);
    	}
    	
    	if (type.equals("Waiters")) {
    		if(waiters.size()%2==0){
    			WaiterAgent1 w = new WaiterAgent1 (name, host, cook);
        		w.setNumber(host.getWaiterNumber());
        		
        		WaiterGui g = new WaiterGui(w, gui);
        		
        		gui.animationPanel.addGui(g);
        		w.setGui(g);
        		
        		waiters.add(w);
        		w.startThread();
        		w.setHost(host); 
        		w.setCashier(cashier);
        		host.addWaiter(w); //creates connection between Waiter and Host
        		//allAgents.add(w);
    		}
    		else{
    			WaiterAgent2 w = new WaiterAgent2 (name, host, cook);
        		w.setNumber(host.getWaiterNumber());
        		
        		WaiterGui g = new WaiterGui(w, gui);
        		
        		gui.animationPanel.addGui(g);
        		w.setGui(g);
        		
        		waiters.add(w);
        		w.startThread();
        		w.setHost(host); 
        		w.setCashier(cashier);
        		host.addWaiter(w); //creates connection between Waiter and Host
        		//allAgents.add(w);
    		}
    	}
    }
    /**
     * Used in ListPanel to change customer state
     * @param i location of desired customer in vector
     * @return
     */
    public Customer getCustomer(int i){
    	if(i<0||i>customers.size())
    		return null;
    	return customers.get(i);
    }
    
    /**
     * Used in ListPanel to change waiter state
     * @param i location of desired waiter in vector
     * @return
     */
    public Waiter getWaiter(int i){
    	if(i<0||i>waiters.size())
    		return null;
    	return waiters.get(i);
    }
    
    /**
     * Allows RestaurantGui to change ListPanel via RestaurantPanel
     * @return
     
	public ListPanel getCustomerPanel() {
		return customerPanel;
	}
	
	
	 * Allows RestaurantGui to change ListPanel via RestaurantPanel
	 * @return
	 
	public ListPanel getWaiterPanel() {
		return waiterPanel;
	}
	
	 * Allows RestaurantGui to get cook to track Inventory
	 * @return cook
	*/
	public Cook getCook(){
		return cook;
	}
	public Market getMarket(int num){
		switch(num){
		case 1:
			return market1;
		case 2:
			return market2;
		case 3:
			return market3;
		}
		//Mandatory Return
		return market1;
	}
}
