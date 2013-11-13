package restaurant_tranac.gui;

import base.Agent;
import restaurant_tranac.agents.CashierAgent;
import restaurant_tranac.agents.CookAgent;
import restaurant_tranac.agents.CustomerAgent;
import restaurant_tranac.agents.HostAgent;
import restaurant_tranac.agents.MarketAgent;
import restaurant_tranac.agents.WaiterAgent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	private Vector<Agent> agents = new Vector<Agent>();	//List of all the agents
	
    private HostAgent host = new HostAgent("Sora");
    private HostGui hostGui = new HostGui(host);

    private CookAgent cook = new CookAgent("Riku");
    private CookGui cookGui = new CookGui(cook);
    
    private CashierAgent cashier = new CashierAgent("Kairi");
    private CashierGui cashierGui = new CashierGui(cashier);
    
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();

    private JPanel restLabel = new JPanel();

    private JTabbedPane agentPanel = new JTabbedPane();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private ListPanel marketPanel = new ListPanel(this,"Markets");
    private ScenarioPanel scenarioPanel = new ScenarioPanel(this, customerPanel, waiterPanel, marketPanel);
    
    private RestaurantGui gui; //reference to main gui
    
    private final int GAP = 20;
    private final double cheapCost = 5.99;
    private final int bHeight = 25;
    private final int lHeight = 400;
    private final int width = 165;
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
        agents.add(host);
        agents.add(cook);
        agents.add(cashier);
        
        host.setGui(hostGui);
        gui.animationPanel.addGui(hostGui);
        host.startThread();
        
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        cook.startThread();
        
        cashier.setGui(cashierGui);
        gui.animationPanel.addGui(cashierGui);
        cashier.startThread();

        setLayout(new GridLayout(1, 2, GAP, GAP));
        
        agentPanel.addTab("Scenarios", scenarioPanel);
        agentPanel.addTab("Customers", customerPanel);
        agentPanel.addTab("Waiters", waiterPanel);
        agentPanel.addTab("Markets", marketPanel);
        agentPanel.setBorder(new EmptyBorder(10,0,10,0));
        
        initRestLabel();
        add(restLabel);
        add(agentPanel);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * staff information, and pause button.
     */
    private void initRestLabel() {
    	restLabel.setLayout(new FlowLayout());
        JLabel label = new JLabel();
        label.setText(
                "<html><center><br><br>Welcome to the Kingdom Hearts Restaurant.<br><br>"
                + "Take a trip to another world - and enjoy amazing food on the way.<br> "
                + "<h3>Tonight's Staff</h3><table>"
                + "<tr><td>Host:</td><td>" + host.getName() + "</td></tr>"
                + "<tr><td>Cook:</td><td>" + cook.getName() + "</td></tr>"
                + "<tr><td>Cashier:</td><td>" + cashier.getName() + "</td></tr>"
                + "</table><h3>Menu</h3><table>"
                + "<tr><td>Steak</td><td>$15.99</td></tr>"
                + "<tr><td>Chicken</td><td>$10.99</td></tr>"
                + "<tr><td>Salad</td><td>$5.99</td></tr><tr>"
                + "<td>Pizza</td><td>$8.99</td></tr></table></center></html>");
        
        Dimension d = new Dimension(width, lHeight);
        label.setPreferredSize(d);
        label.setMaximumSize(d);
        label.setMinimumSize(d);
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label);

        d = new Dimension(width, bHeight);
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
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		agents.add(c);
    		c.startThread();
    	}
    	if(type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name, host, cook, cashier);
    		waiters.add(w);
    		agents.add(w);
    		
    		WaiterGui g = new WaiterGui(w, gui, waiters.size()-1);
    		//size of waiters = the numer of the new created waiter
    		
    		gui.animationPanel.addGui(g);
    		w.setGui(g);

    		host.addWaiter(w);
    		w.startThread();
    	}
    	if(type.equals("Markets")) {
    		MarketAgent m = new MarketAgent(name);
    		m.setCashier(cashier);
    		markets.add(m);
    		agents.add(m);
    		cook.addMarket(m);
    		m.startThread();
    	}
    }

    public void addPerson(String type, String name, String hack) {
    	if (type.equals("Customers")) {
		CustomerAgent c = new CustomerAgent(name);
    		CustomerGui g = new CustomerGui(c, gui);

    		switch(hack) {
    			case "willWait": {
    				c.setWillWait(true);
    				break;
    			}
    			case "willLeave": {
    				c.setWillWait(false);
    				break;
    			}
    			case "brokeCust": {
    				c.setMoney(0);
    				break;	
    			}
    			case "cheapCust": {
    				c.setMoney(cheapCost);
    				break;
    			}
    			case "niceCust": {
    				c.setMoney(cheapCost);
    				break;
    			}
    			case "flake": {
    				c.setMoney(0);
    				c.setFlake(true);
    				break;
    			}
    			default:
    				break;
    		}
    		
    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		agents.add(c);
    		c.startThread();
    	}
    	if(type.equals("Markets")) {
    		MarketAgent m = new MarketAgent(name);
    		m.setCashier(cashier);
    		if(hack.equals("emptyMarket")) {
    			m.setInventory(0);
    		}
    		if(hack.equals("lowMarket")) {
    			m.setInventory(1);
    		}
    		markets.add(m);
    		agents.add(m);
    		cook.addMarket(m);
    		m.startThread();
    	}
    }
    
    public void setCookInventory(int n) {
    	cook.setInventory(n);
    }
    
    public Vector<CustomerAgent> getCustomers() {
    	return customers;
    }
    
    public Vector<WaiterAgent> getWaiters() {
    	return waiters;
    }
    
    public Vector<MarketAgent> getMarkets() {
    	return markets;
    }
    
    public CookAgent getCook() {
    	return cook;
    }
    
    public CashierAgent getCashier() {
    	return cashier;
    }
    
    public void setEnabled(CustomerAgent c) {		//used to allow customer to be hungry again
    	customerPanel.setEnabled(c);
    }
    
    public void setEnabled(WaiterAgent w) {
    	waiterPanel.setEnabled(w);
    }
}
