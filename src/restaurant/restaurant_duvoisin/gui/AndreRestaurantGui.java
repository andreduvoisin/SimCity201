package restaurant.restaurant_duvoisin.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import city.gui.CityCard;
import city.gui.SimCityGui;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class AndreRestaurantGui extends CityCard implements ActionListener {
	public AndreAnimationPanel animationPanel = new AndreAnimationPanel();
	
    public AndreRestaurantPanel restPanel;
   
    private ArrayList<JCheckBox> listCB = new ArrayList<JCheckBox>();
    private Vector<AndreCustomerRole> customers = new Vector<AndreCustomerRole>();
    Boolean waitHere[] = new Boolean[17];
    private Vector<Waiter> waiters = new Vector<Waiter>();
    Boolean idleHere[] = new Boolean[12];
    private ArrayList<JButton> listBU = new ArrayList<JButton>();
    
    static final int WINDOWX = 825;
    static final int WINDOWY = 438;
    static final int RESTPANEL_X = 0;
    static final int RESTPANEL_Y = 0;
    static final int ANIMPANEL_X = 500;
    static final int ANIMPANEL_Y = 500;
    
    private JButton pauseButton = new JButton("Pause");
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public AndreRestaurantGui(SimCityGui gui) {
    	super(gui);
    	restPanel = new AndreRestaurantPanel(this);
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        Dimension restDim = new Dimension(RESTPANEL_X, RESTPANEL_Y);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        JPanel animGroup = new JPanel();
        animGroup.setLayout(new BorderLayout(5, 5));
        Dimension animDim = new Dimension(ANIMPANEL_X, ANIMPANEL_Y);
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        animGroup.add(animationPanel, BorderLayout.CENTER);
        pauseButton.addActionListener(this);
        animGroup.add(pauseButton, BorderLayout.SOUTH);
        add(animGroup);
        
        for(int i = 0; i < waitHere.length; i++)
        	waitHere[i] = false;
        for(int i = 0; i < idleHere.length; i++)
        	idleHere[i] = false;
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateCustomerPanel(Object person, ArrayList<JCheckBox> myCB, Vector<AndreCustomerRole> cust) {
        //stateCB.setVisible(true);
        //currentPerson = person;
    	listCB = myCB;
    	listCB.get(listCB.size() - 1).addActionListener(this);	// ***** All that's being done.
    	customers = cust;
        if (person instanceof AndreCustomerRole) {
            AndreCustomerRole customer = (AndreCustomerRole) person;
            //stateCB.setText("Hungry?");
            for(int i = 0; i < customers.size(); i++)
            	if(customers.get(i).equals(customer)) {
            		//Should checkmark be there? 
                    listCB.get(i).setSelected(customer.getGui().isHungry());
                    //Is customer hungry? Hack. Should ask customerGui
                    listCB.get(i).setEnabled(!customer.getGui().isHungry());
            	}
          
          // Hack. Should ask customerGui
            /*
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
            */
        }
        //infoPanel.validate();
    }
    
    public void updateWaiterPanel(Object person, ArrayList<JButton> myBU, Vector<Waiter> waiter) {
    	listBU = myBU;
    	listBU.get(listBU.size() - 1).addActionListener(this);
    	waiters = waiter;
    	/*
        if (person instanceof WaiterAgent) {
        	WaiterAgent w = (WaiterAgent) person;
            for(int i = 0; i < customers.size(); i++)
            	if(customers.get(i).equals(w)) {
            		//What do?
            	}
        }
        */
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            for(int i = 0; i < listCB.size(); i++)
            	if(listCB.get(i) == e.getSource()) {
                    listCB.get(i).setEnabled(false);
                    customers.get(i).getGui().setHungry();
            	}
        } else if (e.getSource() == pauseButton) {
        	if(pauseButton.getText().equals("Pause")) {
        		pauseButton.setText("Resume");
        		restPanel.pauseAgents();
        		animationPanel.pauseAnimations();
        	} else if(pauseButton.getText().equals("Resume")) {
        		pauseButton.setText("Pause");
        		restPanel.resumeAgents();
        		animationPanel.resumeAnimations();
        	}
        } else if (e.getSource() instanceof JButton) {
        	for(int i = 0; i < listBU.size(); i++)
            	if(listBU.get(i) == e.getSource()) {
            		listBU.get(i).setEnabled(false);
            		waiters.get(i).msgRequestBreak();
            	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    
    public void setCustomerEnabled(AndreCustomerRole c) {
        //if (currentPerson instanceof CustomerAgent) {
            //CustomerAgent cust = (CustomerAgent) currentPerson;
            //if (c.equals(cust)) {
            	for(int i = 0; i < customers.size(); i++)
                	if(customers.get(i).equals(c)) {
                        listCB.get(i).setEnabled(true);
                        listCB.get(i).setSelected(false);
                	}
            //}
        //}
    }
    
    public void setWaiterEnabled(Waiter w) {
		for(int i = 0; i < waiters.size(); i++)
	    	if(waiters.get(i).equals(w)) {
	            listBU.get(i).setEnabled(true);
	            listBU.get(i).setSelected(false);
	    	}
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
    	/*
        AndreRestaurantGui gui = new AndreRestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        */
    }
}
