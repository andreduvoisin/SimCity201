package restaurant.restaurant_cwagoner.gui;


import restaurant.restaurant_cwagoner.roles.CustomerRole;
import restaurant.restaurant_cwagoner.roles.WaiterRole;


//import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.BufferedImage;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends JFrame implements ActionListener {

	private int numTables = 4;

	JPanel animationFrame = new JPanel();
	AnimationPanel animationPanel = new AnimationPanel();
    private RestaurantPanel restPanel = new RestaurantPanel(this, numTables);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel = new JPanel();
    private JCheckBox stateCB = new JCheckBox();

    private Object currentPerson;	// Holds the agent that the info is about
    
    private ArrayList<Dimension> tableLocations = new ArrayList<Dimension>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	super();

    	int screenWidth = getToolkit().getScreenSize().width - 40,
    		screenHeight = getToolkit().getScreenSize().height - 80;

        tableLocations.add(new Dimension(screenWidth / 10,	screenHeight / 8));
        tableLocations.add(new Dimension(screenWidth / 5,	screenHeight / 8));
        tableLocations.add(new Dimension(screenWidth / 10,	screenHeight / 4));
        tableLocations.add(new Dimension(screenWidth / 5,	screenHeight / 4));
        
        for (Dimension d : tableLocations) {
        	animationPanel.addTable(d);
        }

        setBounds(20, 20, screenWidth, screenHeight);
        
    	setLayout(new GridLayout(1, 2));

        // Setup the info panel
        infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(stateCB);
        
        // Restaurant animation and information
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 1));
    	rightPanel.add(animationPanel);
    	rightPanel.add(infoPanel);
    	

    	add(restPanel);
    	add(rightPanel);
    }
    
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
    	
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerRole) {
            CustomerRole c = (CustomerRole) person;
            stateCB.setText("Hungry?");
            stateCB.setSelected(c.getGui().isHungry());
            stateCB.setEnabled(! c.getGui().isHungry());
        }
        else { // It's a waiter
        	// Hide checkbox for now
        	stateCB.setVisible(false);
        }
        
        infoPanel.validate();
    }
    
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(stateCB)) {
            if (currentPerson instanceof CustomerRole) {
                CustomerRole c = (CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
    }

    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerRole c) {        
        // Handle checkbox in Info panel
        stateCB.setEnabled(true);
        stateCB.setSelected(false);
        
        // Handle checkbox in Customers panel
        String name = c.getName();
        for (int i = 0; i < restPanel.numCustomers(); i++) {
          	if (name == restPanel.getCustomerName(i)) {
           		restPanel.enableCustomer(i);
           		break;
           	}
        }
    }
    
    /**
     * Allows GUIs to find their locations
     * @param tableNum The number of the table requested
     * @return The coordinates of that table
     */
    public Dimension getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }
    
    public void waiterOnBreak(boolean allowed, WaiterRole w) {
    	restPanel.waiterBreak(allowed, w);
    }
    
    
    /**
     * Main routine to get GUI started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("CSCI 201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
