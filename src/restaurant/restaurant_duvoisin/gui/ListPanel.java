package restaurant.restaurant_duvoisin.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel implements ActionListener {
	
	public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private JButton addPersonB = new JButton("Add");
    private JTextField inputName = new JTextField(20);
    private JPanel addPeople = new JPanel();
    private JFrame frame = new JFrame();
    private JPanel customerUI = new JPanel();
    
    private JPanel addWaiters = new JPanel();
    private JTextField inputName2 = new JTextField(20);
    private JButton addWaiterB = new JButton("Add");
    private JPanel waiterUI = new JPanel();
    private JPanel view2 = new JPanel();
    public JScrollPane pane2 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    private AndreRestaurantPanel restPanel;
    private String type;
    
    private ArrayList<JCheckBox> listCB = new ArrayList<JCheckBox>();
    private ArrayList<JButton> listBU = new ArrayList<JButton>();

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(AndreRestaurantPanel rp) {
        restPanel = rp;

        //setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        setLayout(new GridLayout(2, 1, 5, 5));
        
        customerUI.setLayout(new BorderLayout(5, 5));
        
        addPeople.setLayout(new BorderLayout());
        addPeople.add(new JLabel("<html><pre><u>Customers</u><br></pre></html>"), BorderLayout.NORTH);
        addPeople.add(inputName, BorderLayout.CENTER);
        addPersonB.addActionListener(this);
        addPeople.add(addPersonB, BorderLayout.EAST);
        addPeople.add(new JLabel("Name                             Hungry?"), BorderLayout.SOUTH);
        customerUI.add(addPeople, BorderLayout.NORTH);
        /*
        myPeople.setLayout(new BoxLayout((Container) myPeople, BoxLayout.Y_AXIS));
        add(myPeople, BorderLayout.CENTER);
        */
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        customerUI.add(pane, BorderLayout.CENTER);
        
        waiterUI.setLayout(new BorderLayout(5, 5));
        
        addWaiters.setLayout(new BorderLayout());
        addWaiters.add(new JLabel("<html><pre><u>Waiters</u><br></pre></html>"), BorderLayout.NORTH);
        addWaiters.add(inputName2, BorderLayout.CENTER);
        addWaiterB.addActionListener(this);
        addWaiters.add(addWaiterB, BorderLayout.EAST);
        addWaiters.add(new JLabel("Name"), BorderLayout.SOUTH);
        waiterUI.add(addWaiters, BorderLayout.NORTH);
        
        view2.setLayout(new BoxLayout((Container) view2, BoxLayout.Y_AXIS));
        pane2.setViewportView(view2);
        waiterUI.add(pane2, BorderLayout.CENTER);
        
        add(waiterUI);
        add(customerUI);

    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	String temp = inputName.getText();
        	if(temp.equals(""))
        		JOptionPane.showMessageDialog(frame, "Must have a customer name in text field.", "ERROR", JOptionPane.ERROR_MESSAGE);
        	else {
        		type = "Customer";
        		addPerson(temp, type);
        		inputName.setText("");
        	}
        } else if (e.getSource() == addWaiterB) {
        	String temp = inputName2.getText();
        	if(temp.equals(""))
        		JOptionPane.showMessageDialog(frame, "Must have a waiter name in text field.", "ERROR", JOptionPane.ERROR_MESSAGE);
        	else {
        		type = "Waiter";
        		addPerson(temp, type);
        		inputName2.setText("");
        	}
        }
        /*
        else {
        	// Isn't the second for loop more beautiful?
            for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        }
        */
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name, String type) {
    	/*
        JButton button = new JButton(name);
        button.setBackground(Color.white);
        Dimension paneSize = pane.getSize();
        Dimension buttonSize = new Dimension(paneSize.width - 20,
                (int) (paneSize.height / 7));
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.addActionListener(this);
        list.add(button);
        view.add(button);
        */
    	if(type.equals("Customer")) {
	    	Dimension paneSize = pane.getSize();
	        Dimension panelSize = new Dimension(paneSize.width - 20,
	                (int) (paneSize.height / 7));
	    	JPanel temp = new JPanel();
	    	temp.setPreferredSize(panelSize);
	    	temp.setMinimumSize(panelSize);
	    	temp.setMaximumSize(panelSize);
	    	temp.setLayout(new BorderLayout());
	    	temp.add(new JLabel(name), BorderLayout.WEST);
	    	JCheckBox tempCB = new JCheckBox();
	    	listCB.add(tempCB);
	    	temp.add(tempCB, BorderLayout.EAST);
	    	view.add(temp);
	        //restPanel.addPerson(type, name);//puts customer on list
	        restPanel.showCustomerInfo(type, name, listCB);//puts hungry button on panel
	        //list.get(list.size() - 1).doClick();	//automatically activates hungry button
	        validate();
    	} else if(type.equals("Waiter")) {
    		Dimension paneSize = pane2.getSize();
	        Dimension panelSize = new Dimension(paneSize.width - 20,
	                (int) (paneSize.height / 7));
	    	JPanel temp = new JPanel();
	    	temp.setPreferredSize(panelSize);
	    	temp.setMinimumSize(panelSize);
	    	temp.setMaximumSize(panelSize);
	    	temp.setLayout(new BorderLayout());
	    	temp.add(new JLabel(name), BorderLayout.WEST);
	    	JButton tempBU = new JButton("Break");
	    	listBU.add(tempBU);
	    	temp.add(tempBU, BorderLayout.EAST);
	    	view2.add(temp);
	        //restPanel.addPerson(type, name);
	        restPanel.showWaiterInfo(type, name, listBU);
	        validate();
    	}
    }
}
