package restaurant.restaurant_duvoisin.gui;

import restaurant.restaurant_duvoisin.CustomerAgent;
import restaurant.restaurant_duvoisin.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
/*
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
*/

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    /*
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    */
    private ArrayList<JCheckBox> listCB = new ArrayList<JCheckBox>();
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    Boolean waitHere[] = new Boolean[17];
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    Boolean idleHere[] = new Boolean[12];
    private ArrayList<JButton> listBU = new ArrayList<JButton>();
    
    static final int WINDOWX = 825;
    static final int WINDOWY = 438;
    static final int RESTPANEL_X = 355;
    static final int RESTPANEL_Y = 400;
    static final int ANIMPANEL_X = 450;
    static final int ANIMPANEL_Y = 370;
    
    private JButton pauseButton = new JButton("Pause");
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	/*
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(WINDOWX+100, 50 , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
    	*/
    	setBounds(50, 50, WINDOWX, WINDOWY);
    	
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        /*
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new BorderLayout(5, 5));
        
        pauseButton.addActionListener(this);
        picPanel.add(pauseButton, BorderLayout.SOUTH);
        
        try {
	        BufferedImage myPicture;
	        myPicture = ImageIO.read(new File("andreduvoisin.jpeg"));
			//myPicture = ImageIO.read(new File("C:\\Users\\Andre\\Desktop\\College\\Maestros\\andreduvoisin.jpeg"));
	        JLabel picLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance(84, 112, Image.SCALE_SMOOTH)));
	        picPanel.add(picLabel, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        add(picPanel);
        */
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
        animationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        animGroup.add(animationPanel, BorderLayout.CENTER);
        pauseButton.addActionListener(this);
        animGroup.add(pauseButton, BorderLayout.SOUTH);
        add(animGroup);
        // Now, setup the info panel
        /*
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel, BorderLayout.NORTH);
        */
        
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
    public void updateCustomerPanel(Object person, ArrayList<JCheckBox> myCB, Vector<CustomerAgent> cust) {
        //stateCB.setVisible(true);
        //currentPerson = person;
    	listCB = myCB;
    	listCB.get(listCB.size() - 1).addActionListener(this);	// ***** All that's being done.
    	customers = cust;
        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
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
    
    public void updateWaiterPanel(Object person, ArrayList<JButton> myBU, Vector<WaiterAgent> waiter) {
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
    
    public void setCustomerEnabled(CustomerAgent c) {
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
    
    public void setWaiterEnabled(WaiterAgent w) {
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
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
