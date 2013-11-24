package restaurant.restaurant_smileham.gui;

import restaurant.restaurant_smileham.agents.CustomerAgent;

import javax.swing.*;

import city.gui.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends CityCard implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)*/
	
	//Constants
    public static final int cWINDOWX = 1000;
    public static final int cWINDOWY = 700;
	
	private AnimationPanel mAnimationPanel;
	private RestaurantPanel mRestPanel;
	private JPanel mInfoPanel; //Holds info about clicked customer, if there is one
	    private JLabel mInfoLabel;
	    private JCheckBox mStateCB;
//	    private Object mSelectedPerson;/* Holds the agent that the info is about. Seems like a hack ???*/
	private JPanel mIDPanel;
		private JLabel mIDLabel;
		private ImageIcon mIDIcon;
    

    //CONSTRUCTOR
    public RestaurantGui(SimCityGui city, Color background) {
    	super(city, background);
        //Alter JFrame RestaurantGUI
        setBounds(50, 50, cWINDOWX, cWINDOWY);
        setLayout(new GridLayout(1,2));
        		//BoxLayout((Container) getContentPane(), BoxLayout.Y_AXIS));
	    
        //Animation Panel
        mAnimationPanel = new AnimationPanel();
        	mAnimationPanel.setBorder(BorderFactory.createTitledBorder("Animation Panel"));
        
      	//restPanel
        Dimension restDim = new Dimension(cWINDOWX, (int) (cWINDOWY * .6));
	    mRestPanel = new RestaurantPanel(this);
	    	mRestPanel.setBorder(BorderFactory.createTitledBorder("Restaurant Panel"));
	        mRestPanel.setPreferredSize(restDim);
	        mRestPanel.setMinimumSize(restDim);
	        mRestPanel.setMaximumSize(restDim);
	        /* restPanel holds 2 panels
		     * 1) the staff listing, menu, and lists of current customers all constructed
		     *    in RestaurantPanel()
		     * 2) the infoPanel about the clicked Customer (created just below)
	    	 */
    	
    	
    	//infoPanel
	    Dimension infoDim = new Dimension(cWINDOWX / 4, (int) (cWINDOWY * .25));
        mInfoPanel = new JPanel();
	        mInfoPanel.setPreferredSize(infoDim);
	        mInfoPanel.setMinimumSize(infoDim);
	        mInfoPanel.setMaximumSize(infoDim);
	        mInfoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
	        mInfoPanel.setLayout(new GridLayout(2, 1));
	        
	        mInfoLabel = new JLabel(); 
		        mInfoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
		        mInfoPanel.add(mInfoLabel);
	        mStateCB = new JCheckBox();
		        mStateCB.setVisible(false);
		        mStateCB.addActionListener(this);
		        mInfoPanel.add(mStateCB);
        
	      //idPanel
	        mIDPanel = new JPanel(new FlowLayout());
		    	mIDLabel = new JLabel();
		    		mIDIcon = new ImageIcon("images/uscBanner.jpg");
		    	mIDLabel.setIcon(mIDIcon);
//		    	mIDLabel.setText("<html>\nAuthor: Shane Mileham</html>");
		    mIDPanel.add(mIDLabel);
		    mIDPanel.setVisible(true);
			    
	        
        
    	//PANEL ORDER
		add(mAnimationPanel);
        add(mRestPanel);
//        add(mInfoPanel);
//        add(mIDPanel);
    }
    
    
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        mStateCB.setVisible(true);
//        mSelectedPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            mStateCB.setText("Hungry?");
            //Should checkmark be there? 
            mStateCB.setSelected(customer.isHungry());
            //Is customer hungry? Hack. Should ask customerGui
            mStateCB.setEnabled(!customer.isHungry());
            //Hack. Should ask customerGui
            mInfoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        mInfoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == mStateCB) {
//            if (mSelectedPerson instanceof CustomerAgent) {
//                CustomerAgent customer = (CustomerAgent) mSelectedPerson;
//                customer.msgGotHungry();
//                mStateCB.setEnabled(false);
//            }
//        }
    }
    
//    /**
//     * Message sent from a customer gui to enable that customer's
//     * "I'm hungry" checkbox.
//     *
//     * @param c reference to the customer
//     */
//    public void setCustomerEnabled(CustomerAgent c) {
//        if (mSelectedPerson instanceof CustomerAgent) {
//            CustomerAgent cust = (CustomerAgent) mSelectedPerson;
//            if (c.equals(cust)) {
//                mStateCB.setEnabled(true);
//                mStateCB.setSelected(false);
//            }
//        }
//    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        //RestaurantGui gui = new RestaurantGui();
        //gui.setTitle("CSCI201 Restaurant");
        //gui.setVisible(true);
        //gui.setResizable(false);
        //gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public RestaurantPanel getRestaurantPanel(){
    	return mRestPanel;
    }
    
    public AnimationPanel getAnimationPanel(){
    	return mAnimationPanel;
    }
    
    public void popup(String message){
    	JOptionPane.showMessageDialog(mRestPanel, message);
    }
    
    private boolean exit = false;
    public void popupExit(String message){
    	if (exit) return;
    	JOptionPane.showMessageDialog(mRestPanel, message);
    	setVisible(false);
    	//dispose();
    	exit = true;
    }
    
    public void pause(){
    	mRestPanel.getAgentPanel().pauseSimulation();
    }
}
