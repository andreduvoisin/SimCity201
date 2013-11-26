package restaurant.restaurant_maggiyan.gui;

import restaurant.restaurant_maggiyan.roles.MaggiyanCustomerRole;
import restaurant.restaurant_maggiyan.roles.MaggiyanHostRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class MaggiyanListPanel extends JPanel implements ActionListener {
	//Components for scroll pane for waiter and customers
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    public JScrollPane waiterPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private JPanel wView = new JPanel(); 
    private JPanel customerPanel = new JPanel();
    private JPanel waiterPanel = new JPanel(); 

    private List<JPanel> list = new ArrayList<JPanel>();
    private List<JPanel> waiterList = new ArrayList<JPanel>(); 
    
    //Add Customer, Add Waiter and Pause Buttons
    private JButton addPersonB = new JButton("Add Customer");
    private JButton pauseButton = new JButton("Pause");
    private JButton addWaiter = new JButton("Add Waiter");

    //Text boxes for inputting names of customers and waiters 
    private JTextField nameBox = new JTextField();  
    private JTextField waiterNameBox = new JTextField(); 
    
    //Instance of RestaurantPanel used to create new customers and waiters
    private MaggiyanRestaurantPanel restPanel;
    private String type;
    
    //To Check if paused button has been pressed 
    private boolean pressed = false; 
    
    //Break buttons
    private List<JButton> breakButtonList = new ArrayList<JButton>(); 
    
    //Customer check boxes
    private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>(); 
    private List<JCheckBox> waiterCBList = new ArrayList<JCheckBox>();
    
    //Values used for dimensions of Customer and Waiter panels 
    private static final int MAXDIM_X = 400; 
    private static final int MAXDIM_Y = 30; 
    
    //Getter in order for listener to check which boxes have been checked off 
    public List<JCheckBox> getCheckBoxList(){
    	return checkBoxList; 
    }
    public List<JCheckBox> getWaiterCBList(){
    	return waiterCBList;
    }
    
    private int customerCounter = 1; 
    private int waiterCounter = 1; 
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public MaggiyanListPanel(MaggiyanRestaurantPanel rp, String type) {
        restPanel = rp;
        
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.X_AXIS));
       
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.setMaximumSize(new Dimension(MAXDIM_X - 200, MAXDIM_Y*9));
        waiterPanel.setLayout(new BoxLayout(waiterPanel, BoxLayout.Y_AXIS));
        waiterPanel.setMaximumSize(new Dimension(MAXDIM_X - 200, MAXDIM_Y*9));
        customerPanel.setBackground(Color.MAGENTA);
        
        customerPanel.add(new JLabel("<html><pre> <u>" + "Customers and Waiters" + "</u><br></pre></html>"));
        
        pauseButton.addActionListener(this);
        pauseButton.setAlignmentY(Component.LEFT_ALIGNMENT);
        waiterPanel.add(pauseButton);
        
        //Add Customer Button 
        addPersonB.addActionListener(this);
        customerPanel.add(addPersonB);
        
        //Add Waiter Button
        addWaiter.setAlignmentY(Component.CENTER_ALIGNMENT);
        addWaiter.addActionListener(this); 
        waiterPanel.add(addWaiter); 
        nameBox.setMaximumSize(new Dimension(MAXDIM_X, MAXDIM_Y));  
        waiterNameBox.setMaximumSize(new Dimension(MAXDIM_X, MAXDIM_Y));  
        waiterNameBox.setAlignmentY(Component.LEFT_ALIGNMENT);
        customerPanel.add(nameBox); 
        waiterPanel.add(waiterNameBox); 
        
  
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        view.setMaximumSize(new Dimension(MAXDIM_X, MAXDIM_Y*10));
        pane.setViewportView(view);
        pane.setMaximumSize(new Dimension(MAXDIM_X, MAXDIM_Y*10));
        customerPanel.add(pane);
        
        wView.setLayout(new BoxLayout((Container) wView, BoxLayout.Y_AXIS)); 
        waiterPane.setViewportView(wView);
        waiterPane.setMaximumSize(new Dimension(MAXDIM_X, MAXDIM_Y*10)); 
        waiterPane.setAlignmentY(Component.LEFT_ALIGNMENT);
        waiterPanel.add(waiterPane); 
        
        
        add(customerPanel); 
        add(waiterPanel); 
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	String custName = nameBox.getText();
            //addPerson(custName);
        }
        if(e.getSource() == addWaiter){
        	String waiterName = waiterNameBox.getText(); 
        	//addWaiter(waiterName); 
        }
        else {
        	//Checks for hungry customers
        	for(int i = 0; i<checkBoxList.size(); i++){
        		JCheckBox temp = checkBoxList.get(i); 
        		//if the check box is checked
        		if(e.getSource() == temp){
        			//if the name of the checkbox's corresponding customer matches that of a customer agent then set the customer gui to hungry
        			if(temp.getText() == restPanel.getCust().get(i).getName()){
            			int index = i; 
                		restPanel.getCust().get(index).getGui().setHungry(customerCounter); 
                		if(customerCounter == 10){
                			customerCounter = 0; 
                		}
                		else{
                			customerCounter++; 
                		}
                		temp.setEnabled(false);
                	}
        		}
        		
        	}
//        	for(int i =0; i< breakButtonList.size(); i++){
//        		JButton temp = breakButtonList.get(i);
//        		if(e.getSource() == temp){
//        			if(temp.getName() == "Break"){
//        				System.out.println("Button pressed");
//        				int index = i; 
//                		restPanel.getWaiter().get(index).getGui().setBreak(); 
//                		temp.setName("Waiting for Response");
//        			}
//        		}
//        	}
        	//Checks for waiters asking to go on break
        	for(int i = 0; i<waiterCBList.size(); i++){
        		JCheckBox temp = waiterCBList.get(i); 
        		//if the check box is checked
        		if(e.getSource() == temp){
        			//if the name of the checkbox's corresponding customer matches that of a customer agent then set the customer gui to hungry
        			if(temp.getText() == restPanel.getWaiter().get(i).getName()){
            			int index = i; 
                		restPanel.getWaiter().get(index).getGui().setBreak(); 
                		temp.setEnabled(false);
                	}
        		}
        		
        	}
        
        }
    }
//
//    /**
//     * If the Add Customer button is pressed, this function creates
//     * a spot for it in the scroll pane, and tells the restaurant panel
//     * to add a new person.
//     *
//     * @param name name of new person
//     */
//    public void addPerson(String name) {
//        if (name != null) {
//        	JPanel custPanel = new JPanel(); 
//        	custPanel.setMaximumSize(new Dimension (MAXDIM_X,MAXDIM_Y));
//        	JCheckBox hungryBox = new JCheckBox(name); 
//            custPanel.setBackground(Color.white);
//        	custPanel.add(hungryBox); 
//        	checkBoxList.add(hungryBox);
//        	hungryBox.addActionListener(this);
//        	
//        	
//            Dimension paneSize = pane.getSize();
//      
//            Dimension buttonSize = new Dimension(paneSize.width - 30, MAXDIM_Y);
//            custPanel.setPreferredSize(buttonSize);
//            custPanel.setMinimumSize(buttonSize);
//            custPanel.setMaximumSize(buttonSize);
//            list.add(custPanel);
//            view.add(custPanel);
//            restPanel.addPerson(type, name);//puts customer on list
//            validate();
//        }
//    }
//    
//    public void addWaiter(String name) {
//        if (name != null) {
//        	JPanel waiterPanel = new JPanel(); 
//        	waiterPanel.setMaximumSize(new Dimension (MAXDIM_X,MAXDIM_Y));
//        	JCheckBox onBreakBox = new JCheckBox(name); 
//        	JLabel breakLabel = new JLabel("Break?");
////        	JButton breakButton = new JButton("Break"); 
//        	waiterPanel.setBackground(Color.white);
//        	waiterPanel.add(breakLabel); 
////        	waiterPanel.add(breakButton);
////        	breakButtonList.add(breakButton); 
////        	breakButton.addActionListener(this);
//        	waiterPanel.add(onBreakBox); 
//        	waiterCBList.add(onBreakBox);
//        	onBreakBox.addActionListener(this);
//        	
//        	
//            Dimension paneSize = pane.getSize();
//      
//            Dimension buttonSize = new Dimension(paneSize.width - 30, MAXDIM_Y);
//            waiterPanel.setPreferredSize(buttonSize);
//            waiterPanel.setMinimumSize(buttonSize);
//            waiterPanel.setMaximumSize(buttonSize);
//            waiterList.add(waiterPanel);
//            wView.add(waiterPanel);
//            restPanel.addPerson("Waiter", name);
//            restPanel.getWaiterGui(name).atWork(waiterCounter);
//            waiterCounter++; 
//            validate();
//        }
//    }
    
    
//    public void addWaiter(String name){
//    	if (name != null){
//    		JPanel wPanel = new JPanel();
//    		wPanel.setMaximumSize(new Dimension (MAXDIM_X,MAXDIM_Y));
//    		wPanel.setBackground(Color.white);
//    		
//    		JLabel waiter = new JLabel(name); 
//    		wPanel.add(waiter); 
//    		
//    		Dimension paneSize = pane.getSize();
//    	      
//            Dimension buttonSize = new Dimension(paneSize.width - 30, MAXDIM_Y);
//            wPanel.setPreferredSize(buttonSize);
//            wPanel.setMinimumSize(buttonSize);
//            wPanel.setMaximumSize(buttonSize);
//            waiterList.add(wPanel);
//            wView.add(wPanel);
//            restPanel.addPerson("Waiter", name);
//            restPanel.getWaiterGui(name).atWork(); 
//            
//            //restPanel.getHost().isWaiter = true; 
//    		
//    		validate(); 
//    	}
//    }
}
