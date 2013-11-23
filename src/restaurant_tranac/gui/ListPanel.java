package restaurant_tranac.gui;

import base.Agent;
import restaurant_tranac.roles.RestaurantCustomerRole_at;
import restaurant_tranac.roles.MarketAgent;
import restaurant_tranac.roles.RestaurantWaiterRole_at;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JCheckBox> list = new ArrayList<JCheckBox>();
    
    private JPanel addPersonPanel = new JPanel();
    private JButton addPersonB = new JButton("Add");
    private JTextField personName = new JTextField();
    
    private final int pNameWidth = 120;
    private final int pNameHeight = 25;
    private final int paneX = 180;
    private final int paneY = 380;
    
    private Vector<RestaurantCustomerRole_at> customers;
    private Vector<RestaurantWaiterRole_at> waiters;
    private Vector<MarketAgent> markets;
    private RestaurantSidePanel_at restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantSidePanel_at rp, String type) {
        restPanel = rp;
        this.type = type;
        customers = rp.getCustomers();
        waiters = rp.getWaiters();
        markets = rp.getMarkets();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addPersonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 5));
        add(addPersonPanel);
        addPersonPanel.add(personName);
        personName.setPreferredSize(new Dimension((int)(pNameWidth), pNameHeight));
        personName.addActionListener(this);
        addPersonB.addActionListener(this);
        addPersonPanel.add(addPersonB);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        view.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        Dimension d = new Dimension(paneX, paneY);
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
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
        if (e.getSource() == addPersonB) {
        	addPerson(personName.getText());
        }
        if (e.getSource() == personName) {
        	addPerson(personName.getText());
        }
        else {
        	for (JCheckBox temp:list){
                if (e.getSource() == temp)
                {
                	if(type == "Customers") {
                	for(RestaurantCustomerRole_at i:customers){
                		if(temp.getText() == i.getName()) {
                			i.getGui().setHungry();
                			temp.setEnabled(false);
                		}
                	}
                	}
                	else if(type == "Waiters") {
                    	for(RestaurantWaiterRole_at i:waiters){
                    		if(temp.getText() == i.getName()) {
                    			i.getGui().setWantToGoOnBreak();
                    			temp.setEnabled(false);
                    		}
                    	}
                	}
                }
            }
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
        	JCheckBox newPerson = new JCheckBox();
        	newPerson.setText(name);
        	newPerson.addActionListener(this);
        	Dimension paneSize = pane.getSize();
        	Dimension nameSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 7));
        	newPerson.setPreferredSize(nameSize);
        	list.add(newPerson);							//adds new person the the lists
        	view.add(newPerson);
        	restPanel.addPerson(type, name);				//creates the person
            validate();
        }
    }
    
    public void addPerson(String name, String hack) {
        if (name != null) {
        	JCheckBox newPerson = new JCheckBox();
        	newPerson.setText(name);
        	newPerson.addActionListener(this);
        	Dimension paneSize = pane.getSize();
        	Dimension nameSize = new Dimension(paneSize.width - 20, (int) (paneSize.height / 7));
        	newPerson.setPreferredSize(nameSize);
        	list.add(newPerson);							//adds new person the the lists
        	view.add(newPerson);
        	restPanel.addPerson(type, name, hack);				//creates the person
            validate();
        }
    }
    
    public void setEnabled(RestaurantCustomerRole_at c) {		//used to set customer hungry enable once they leave the restaurant
    	for(JCheckBox i : list) {
    		if(i.getText() == c.getName()) {
    			i.setEnabled(true);
    			i.setSelected(false);
    		}
    	}
    }
    
    public void setEnabled(RestaurantWaiterRole_at w) {		//used to set customer hungry enable once they leave the restaurant
    	for(JCheckBox i : list) {
    		if(i.getText() == w.getName()) {
    			i.setEnabled(true);
    			i.setSelected(false);
    		}
    	}
    }
}
