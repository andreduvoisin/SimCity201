package restaurant.restaurant_jerryweb.gui;

import restaurant.restaurant_jerryweb.CustomerRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    //private JTextField addCustName = new JTextField(10);
    private JButton pause = new JButton("pause");
    //private JButton pause = new JButton("Request Break");
    
    static final int rows = 1;
    static final int cols = 2;
    static final int hSpacing = 30;
    static final int vSpacing = 0;
    static final int xIndexing = 50;
    static final int yIndexing = 50;
    
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 350;

        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, yIndexing , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(false);
    	add(animationPanel); 
    	
    	setBounds(xIndexing, yIndexing, WINDOWX+250, WINDOWY+300);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(WINDOWX+150, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
       
        
        pause.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//restPanel.pauseAgents();
			}
        	
       });
        
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        //restPanel.getLpChbx().addActionListener(this);
        
        
        
        
        infoPanel.setLayout(new GridLayout(rows, cols, hSpacing, vSpacing));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click to pause.</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(pause);
        add(infoPanel);
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
            CustomerRole customer = (CustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 

            stateCB.setSelected( restPanel.getLpChbx().isSelected() );
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(! restPanel.getLpChbx().isSelected() );
            
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
    	//if( restPanel.getListPanel() ){
            if (currentPerson instanceof CustomerRole) {
                CustomerRole c = (CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
    }
    

    
    public void originalChbx() {

            if (currentPerson instanceof CustomerRole) {
                CustomerRole c = (CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
    
    
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerRole c) {
        if (currentPerson instanceof CustomerRole) {
            CustomerRole cust = (CustomerRole) currentPerson;
            if (c.equals(cust)) {
            	restPanel.getLpChbx().setEnabled(true);
            	restPanel.getLpChbx().setSelected(false);

            }
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
