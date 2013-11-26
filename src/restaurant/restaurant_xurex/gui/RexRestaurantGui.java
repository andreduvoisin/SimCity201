package restaurant.restaurant_xurex.gui;

//import restaurant.restaurant_xurex.agents.CustomerAgent;
//import restaurant.restaurant_xurex.interfaces.Waiter;

//import javax.swing.*;

import city.gui.CityCard;
import city.gui.SimCityGui;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RexRestaurantGui extends CityCard implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    //private RestaurantPanel restPanel = new RestaurantPanel(this);
    
	//private JLabel label = new JLabel();
    //private JLabel inventoryLabel = new JLabel();
    
    /* infoPanel holds information about the clicked customer, if there is one
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel*/
    
    /* myPanel displays a graphic and some text in the left and infoPanel in the right*/
    //private JPanel myPanel;
    //private JLabel myLabel;	  //part of myPanel
    //private ImageIcon myFace; //part of myPanel
    

    //private Object currentPerson;/* Holds the agent that the info is about.Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RexRestaurantGui(SimCityGui city) {
    	super(city);
        int WINDOWX = 500;
        int WINDOWY = 500;
        /*
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
    	*/
        
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new GridLayout (1,2));
        
        Dimension animationDim = new Dimension(500, 500);
		animationPanel.setPreferredSize(animationDim);
		animationPanel.setMinimumSize(animationDim);
		animationPanel.setMaximumSize(animationDim);
		add(animationPanel);
    	/*
        Dimension restDim = new Dimension(550, (int) (WINDOWY));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        */
       // add(animationPanel);
        /*
        InitInventoryLabel();
        Dimension inventoryDim = new Dimension(200,350);
        inventoryLabel.setPreferredSize(inventoryDim);
        inventoryLabel.setMinimumSize(inventoryDim);
        inventoryLabel.setMaximumSize(inventoryDim);

        add(inventoryLabel);
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

        infoPanel.setLayout(new GridLayout(1,2,30,0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click to add customers</i></pre></html>");
        
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);*/
        //add(infoPanel);
        
        //Creating new Label with personal information
        /*
        myFace = new ImageIcon("C:\\Users\\Rex\\Downloads\\myPic.jpg");
        myLabel = new JLabel("Rex Xu", myFace, JLabel.CENTER);
        Dimension faceDim = new Dimension(87, (int)( WINDOWY * .25));
        myLabel.setPreferredSize(faceDim);
        myLabel.setMinimumSize(faceDim);
        myLabel.setMaximumSize(faceDim);
        myLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        //Adding myLabel to myPanel
        myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.X_AXIS));
        myPanel.add(myLabel);
       // myPanel.add(infoPanel);
        add(myPanel);*/
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    /*
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }*/
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }*/
    }
    /*
    public void updateInventory(){
    	label.setText(
                "<html><h3><u>Inventory</u></h3><table>"
                +"<tr><td>Steak:</td><td>" 		+ restPanel.getCook().getQuantity("Steak")
                +"<tr><td>Chicken:</td><td>" 	+ restPanel.getCook().getQuantity("Chicken")
                +"<tr><td>Salad:</td><td>" 		+ restPanel.getCook().getQuantity("Salad")
                +"<tr><td>Pizza:</td><td>" 		+ restPanel.getCook().getQuantity("Pizza")
                +"</td></tr></table><h3><u>Markets</u></h3><table>"
                +"<tr><td>M1:</td><td>"
                +restPanel.getMarket(1).getQuantity("Steak")+" : "
                +restPanel.getMarket(1).getQuantity("Chicken")+" : "
                +restPanel.getMarket(1).getQuantity("Salad")+" : "
                +restPanel.getMarket(1).getQuantity("Pizza")
                +"<tr><td>M2:</td><td>"
                +restPanel.getMarket(2).getQuantity("Steak")+" : "
                +restPanel.getMarket(2).getQuantity("Chicken")+" : "
                +restPanel.getMarket(2).getQuantity("Salad")+" : "
                +restPanel.getMarket(2).getQuantity("Pizza")
                +"<tr><td>M3:</td><td>"
                +restPanel.getMarket(3).getQuantity("Steak")+" : "
                +restPanel.getMarket(3).getQuantity("Chicken")+" : "
                +restPanel.getMarket(3).getQuantity("Salad")+" : "
                +restPanel.getMarket(3).getQuantity("Pizza")
                +"</td></tr></table><br></html>");
    }
    /*
    private void InitInventoryLabel(){
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        inventoryLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Inventory</u></h3><table>"
                +"<tr><td>Steak:</td><td>" 		+ restPanel.getCook().getQuantity("Steak")
                +"<tr><td>Chicken:</td><td>" 	+ restPanel.getCook().getQuantity("Chicken")
                +"<tr><td>Salad:</td><td>" 		+ restPanel.getCook().getQuantity("Salad")
                +"<tr><td>Pizza:</td><td>" 		+ restPanel.getCook().getQuantity("Pizza")
                +"</td></tr></table><h3><u>Markets</u></h3><table>"
                +"<tr><td>M1:</td><td>"
                +restPanel.getMarket(1).getQuantity("Steak")+" : "
                +restPanel.getMarket(1).getQuantity("Chicken")+" : "
                +restPanel.getMarket(1).getQuantity("Salad")+" : "
                +restPanel.getMarket(1).getQuantity("Pizza")
                +"<tr><td>M2:</td><td>"
                +restPanel.getMarket(2).getQuantity("Steak")+" : "
                +restPanel.getMarket(2).getQuantity("Chicken")+" : "
                +restPanel.getMarket(2).getQuantity("Salad")+" : "
                +restPanel.getMarket(2).getQuantity("Pizza")
                +"<tr><td>M3:</td><td>"
                +restPanel.getMarket(3).getQuantity("Steak")+" : "
                +restPanel.getMarket(3).getQuantity("Chicken")+" : "
                +restPanel.getMarket(3).getQuantity("Salad")+" : "
                +restPanel.getMarket(3).getQuantity("Pizza")
                +"</td></tr></table><br></html>");

        inventoryLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        inventoryLabel.add(label, BorderLayout.CENTER);
        inventoryLabel.add(new JLabel("               "), BorderLayout.EAST);
        inventoryLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
    */
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    /*
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
        //Extra Code added to enable ListC CheckBoxes
        restPanel.getCustomerPanel().setCustomerEnabled(c);
    }
    
    public void setWaiterEnabled(Waiter w){
    	restPanel.getWaiterPanel().setWaiterEnabled(w);
    }
    
    public void setWaiterEnabled(Waiter w, String name){
    	restPanel.getWaiterPanel().setWaiterEnabled(w, name);
    }
    */
    /**
     * Main routine to get gui started
     */
    /*
    public static void main(String[] args) {
        RexRestaurantGui gui = new RexRestaurantGui();
        gui.setTitle("Rex's Rad Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	*/
}
