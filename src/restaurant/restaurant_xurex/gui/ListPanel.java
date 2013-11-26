package restaurant.restaurant_xurex.gui;

import restaurant.restaurant_xurex.agents.CustomerAgent;
import restaurant.restaurant_xurex.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    //private JButton addPersonB = new JButton("Add");

    private RexRestaurantPanel restPanel;
    private String type;
    private JTextField input; //customer name entry

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RexRestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        //JPanel adder = new JPanel(); //Holds Add button and input text field
        //adder.setLayout(new BoxLayout(adder, BoxLayout.X_AXIS));
        //addPersonB.addActionListener(this);
        //adder.add(addPersonB);
        
        String s = "-NAME-";
        Dimension inputSize = new Dimension (500,25);
        input = new JTextField(s, 2);
        input.setHorizontalAlignment(JTextField.CENTER);
    	input.setPreferredSize(inputSize);
    	input.setMaximumSize(inputSize);
    	input.addActionListener(
    			new ActionListener(){
    				public void actionPerformed(ActionEvent e){
    					if(!input.getText().equals("")){
    						addPerson(input.getText());
    					}
    				}
    			});
    	input.addFocusListener(
    			new FocusListener(){
    				public void focusGained(FocusEvent e){
    					input.setText("");
    				}
    				public void focusLost(FocusEvent e){
    					input.setText("-NAME-");
    				}
    			});
    	//adder.add(input);
    	
    	//add(adder);
    	add(input);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            addPerson(input.getText());
        }
        else */if (type=="Customers"){
            for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);
                if(e.getSource() == temp){
                	//restPanel.showInfo(type,  temp.getText());
                	CustomerAgent c = restPanel.getCustomer(i);
                	c.getGui().setHungry();
                	list.get(i).setEnabled(false);
                }
            }
        }
        else if (type=="Waiters"){
        	for (int i = 0; i < list.size(); i++){
        		JButton temp = list.get(i);
        		if(e.getSource() == temp){
    				Waiter w = restPanel.getWaiter(i);
        			if(temp.getText().equals("Back To Work")){
        				w.getGui().backToWork();
        				list.get(i).setText(w.getName());
        			}
        			else{
        				//restPanel.showInfo(type, temp.getText());
        				w.getGui().setBreak();
        				temp.setText("Back To Work");
        				temp.setName(w.getName());
        				list.get(i).setEnabled(false);
        			}
        		}
        	}
        }
       
    }
    
	// Isn't the second for loop more beautiful?
    /*for (int i = 0; i < list.size(); i++) {
        JButton temp = list.get(i);
        if(e.getSource() == temp){
        	//restPanel.showInfo(type,  temp.getText());
        	CustomerAgent c = restPanel.getCustomer(i);
        	c.getGui().setHungry();
        	list.get(i).setEnabled(false); // prevents user from doubling up on hungry state
        	list.get(i).setSelected(true);
        }
    }*/
	/*for (JButton temp:list){
        if (e.getSource() == temp){
            restPanel.showInfo(type, temp.getText());
        	//adding code from restaurant gui to automatically make hungry
            CustomerAgent c = (CustomerAgent) temp;
        	temp.getGui().setHungry();
        }*/

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (!name.equals("-NAME-")) {
        	
        	input.setText(null); //clears text field for new name
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension listSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 14));
            Dimension buttonSize = new Dimension(paneSize.width - 20,
            		(int) (paneSize.height / 14));
            
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);

            JPanel List = new JPanel();
            List.setPreferredSize(listSize);
            List.setMaximumSize(listSize);
            List.setMinimumSize(listSize);
            List.setLayout(new BoxLayout(List,BoxLayout.X_AXIS));
            List.add(button);
            
            list.add(button);
            view.add(List);
            restPanel.addPerson(type, name);//puts customer on 'customers' vector or 'waiters' vector
            //restPanel.showInfo(type, name); //puts customer on info panel display
            validate();
        }
    }
    
    /**
     * setCustomerEnabled in ListPanel to control list
     * will be used via RestaurantPanel via RestaurantGui via CustomerGui
     * @param c Customer passed by CustomerGui that is done eating
     */
    public void setCustomerEnabled (CustomerAgent c){
    	for (int i = 0; i < list.size(); i++) {
    		if(c.getName() == list.get(i).getText()){
    			list.get(i).setEnabled(true);
    		}
    	}
    }
    
    /**
     * setWaiterEnabled in ListPanel to control list
     * will be used via RestaurantPanel via RestaurantGui via CustomerGui
     * @param w Waiter passed by WaiterGui that is done with break
     */
    public void setWaiterEnabled (Waiter w){
    	for (int i = 0; i < list.size(); i++) {
    		if(w.getName().equals(list.get(i).getName())){
    			list.get(i).setEnabled(true);
    		}
    	}
    }
    public void setWaiterEnabled (Waiter w, String name){
    	for (int i = 0; i < list.size(); i++) {
    		if(w.getName().equals(list.get(i).getName())){
    			list.get(i).setText(name);
    			list.get(i).setEnabled(true);
    		}
    	}
    }
}
