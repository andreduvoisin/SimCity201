package restaurant_tranac.gui;

import restaurant_tranac.*;
import base.Agent;
import restaurant_tranac.roles.RestaurantCashierRole_at;
import restaurant_tranac.roles.RestaurantCookRole_at;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ScenarioPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> scenarios = new ArrayList<JButton>();
    private ListPanel customerPanel;
    private ListPanel waiterPanel;
    private ListPanel marketPanel;
    private final int bWidth = 190;
    private final int bHeight = 25;
    private final int lowInventory = 1;
    
    private RestaurantCookRole_at cook;
    private RestaurantCashierRole_at cashier;
    
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ScenarioPanel(RestaurantPanel rp, ListPanel cPanel, ListPanel wPanel, ListPanel mPanel) {
        restPanel = rp;
        customerPanel = cPanel;
        waiterPanel = wPanel;
        marketPanel = mPanel;
        cook = restPanel.getCook();
        cashier = restPanel.getCashier();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));   

        scenarios.add(new JButton("2 Waiters, 4 Customers"));
        scenarios.add(new JButton("Low Cook Inventory"));
        scenarios.add(new JButton("Empty Cook Inventory"));
        scenarios.add(new JButton("Empty Markets, One"));
        scenarios.add(new JButton("Empty Markets, All"));
        scenarios.add(new JButton("Low Market Inventory"));
        scenarios.add(new JButton("Nice Broke Customer"));
        scenarios.add(new JButton("Cheap Customer"));
        scenarios.add(new JButton("Nice Cheap Customer"));
        scenarios.add(new JButton("Flaky Cheap Customer"));
        scenarios.add(new JButton("Full Restaurant, Wait"));
        scenarios.add(new JButton("Full Restaurant, Leaves"));
        scenarios.add(new JButton("Home Positions"));
        scenarios.add(new JButton("Bankrupt Restaurant"));
        
        for(JButton b : scenarios) {
        	b.setPreferredSize(new Dimension((int)(bWidth), bHeight));
        	b.setMaximumSize(new Dimension((int)(bWidth), bHeight));
        	b.setMinimumSize(new Dimension((int)(bWidth), bHeight));
        	b.addActionListener(this);
        	view.add(b);
        }
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        view.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        Dimension d = new Dimension(200,430);			//MAGIC NUMBER
        pane.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        pane.setPreferredSize(d);
        pane.setMaximumSize(d);
        pane.setMinimumSize(d);
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
    	for(JButton s : scenarios) {
    		if(e.getSource() == s)
    			runScenario(s.getText());
    		s.setEnabled(false);
    	}
    }

    public void runScenario(String s) {
    	switch(s) {
    	case "2 Waiters, 4 Customers": {
    		for(int i=1;i<=4;i++) {
    			customerPanel.addPerson("C"+i);
    		}
    		for(int i=1;i<=2;i++) {
    			waiterPanel.addPerson("W"+i);
    		}
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Low Cook Inventory": {
    		cook.setInventory(lowInventory);
			customerPanel.addPerson("C1");
			waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Empty Cook Inventory": {
    		cook.setInventory(0);
			customerPanel.addPerson("C1");
			waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Empty Markets, One": {
    		cook.setInventory(0);
			customerPanel.addPerson("C1");
			waiterPanel.addPerson("W1");
			marketPanel.addPerson("M1", "emptyMarket");
    		for(int i=2;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Empty Markets, All": {
    		cook.setInventory(0);
			customerPanel.addPerson("C1");
			waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i, "emptyMarket");
    		}
    		break;
    	}
    	case "Low Market Inventory": {
    		cook.setInventory(0);
    		customerPanel.addPerson("C1");
    		waiterPanel.addPerson("W1");
			marketPanel.addPerson("M1", "lowMarket");
    		for(int i=2;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Nice Broke Customer": {
			customerPanel.addPerson("C1", "brokeCust");				//will not eat, will leave
			waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Cheap Customer": {
    		customerPanel.addPerson("C1", "cheapCust");
    		waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Nice Cheap Customer": {
    		cook.setInventory("Salad", 0);
			customerPanel.addPerson("C1", "niceCust");				//will not eat, will leave
			waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Flaky Cheap Customer": {
			customerPanel.addPerson("C1", "flake");					//will still eat and leave
			waiterPanel.addPerson("W1");
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Full Restaurant, Wait": {
    		for(int i=1;i<=5;i++) {
    			customerPanel.addPerson("C"+i, "willWait");			//customer will wait
    		}
    		for(int i=1;i<=2;i++) {
    			waiterPanel.addPerson("W"+i);
    		}
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Full Restaurant, Leaves": {
    		for(int i=1;i<=5;i++) {
    			customerPanel.addPerson("C"+i, "willLeave");		//customer will leave
    		}
    		for(int i=1;i<=2;i++) {
    			waiterPanel.addPerson("W"+i);
    		}
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		break;
    	}
    	case "Home Positions": {
    		for(int i=1;i<=20;i++) {
    			customerPanel.addPerson("C"+i, "willWait");			//customer will wait
    		}
    		for(int i=1;i<=20;i++) {
    			waiterPanel.addPerson("W"+i);
    		}
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    	}
    	case "Bankrupt Restaurant": {
    		for(int i=1;i<=4;i++) {
    			customerPanel.addPerson("C"+i);
    		}
    		for(int i=1;i<=2;i++) {
    			waiterPanel.addPerson("W"+i);
    		}
    		for(int i=1;i<=3;i++) {
    			marketPanel.addPerson("M"+i);
    		}
    		cook.setInventory(0);
    		cashier.setMoney(0);
    		break;
    	}
    	default: {
    		break;
    	}	
    	}
    }
}
