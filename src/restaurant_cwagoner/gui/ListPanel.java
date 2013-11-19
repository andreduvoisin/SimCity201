package restaurant_cwagoner.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of RestaurantPanel
 * Holds the scroll panes for customers and waiters
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel implements ActionListener {
	
	// For customer panel
    private JTextField personName = new JTextField("");
    private JCheckBox initiallyHungry = new JCheckBox();
    private JButton addPersonButton = new JButton("Add");
    public JScrollPane scrollPane = new JScrollPane(
    										JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    										JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    List<JButton> personList = new ArrayList<JButton>();
    List<JCheckBox> checkList = new ArrayList<JCheckBox>();
    List<JButton> askList = new ArrayList<JButton>();
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the GUI
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String panelType) {
        restPanel = rp;
        type = panelType;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre><u>" + type + "</u><br></pre></html>"));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        Dimension personNameDim = new Dimension(100, 30);
        personName.setPreferredSize(personNameDim);
        mainPanel.add(personName);
        
        if (type.equals("Customers")) {
	        initiallyHungry.setText("Hungry?");
	        initiallyHungry.setVisible(true);
	        initiallyHungry.addActionListener(this);
	        mainPanel.add(initiallyHungry);
        }
        // Else it's a waiter panel
        
        addPersonButton.addActionListener(this);
        mainPanel.add(addPersonButton);

        add(mainPanel);
        
        Dimension viewDim = new Dimension(100, 100);
        
        view.setMinimumSize(viewDim);
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        
        scrollPane.setPreferredSize(viewDim);
        scrollPane.setViewportView(view);
        add(scrollPane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
    	// Added person with "Add" button
        if (e.getSource().equals(addPersonButton)) {
	    	addPerson();
        }
        else {
        	// Clicked "Hungry?" checkbox, or...
        	int index = 0;
        	for (JCheckBox cb : checkList) {
        		if (e.getSource().equals(cb)) {
        			cb.setSelected(true);
        			cb.setEnabled(false);
        			restPanel.showInfo(type, index);
        			restPanel.makeHungry(index);
        			return;
        		}
        		index++;
        	}
        	
        	index = 0;
        	// Clicked person's name button, or...
        	for (JButton name : personList){
                if (e.getSource().equals(name)) {
                    restPanel.showInfo(type, index);
                    return;
                }
                index++;
            }
        	
        	index = 0;
        	// Clicked "Ask" button (waiter)
        	for (JButton ask : askList) {
        		if (e.getSource().equals(ask)) {
        			restPanel.askForBreak(index);
        			ask.setEnabled(false);
        			return;
        		}
        		index++;
        	}
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the customer scroll pane, and tells the restaurant panel
     * to add a new person (customer or waiter)
     *
     * @param name Name of the new customer or waiter
     */
    public void addPerson() {
        if (personName.getText().trim().equals("")) {
        	personName.setBackground(Color.PINK);
        }
        else {
	        personName.setBackground(Color.WHITE);
	        JButton button = new JButton(personName.getText());
	        button.setBackground(Color.WHITE);
	
	        Dimension paneSize = scrollPane.getSize();
	        Dimension buttonSize;
	        if (type.equals("Customers")) {
	        	buttonSize = new Dimension(paneSize.width / 2, paneSize.height / 6);
	        }
	        else { // Narrower name button
	        	buttonSize = new Dimension(paneSize.width / 3, paneSize.height / 6);
	        }
	        button.setPreferredSize(buttonSize);
	        button.setMinimumSize(buttonSize);
	        button.setMaximumSize(buttonSize);
	        button.addActionListener(this);
	        personList.add(button);
	        view.add(button);
	        
	        JPanel personRow = new JPanel();
	        Dimension rowSize = new Dimension(paneSize.width, paneSize.height / 6);
	        personRow.setPreferredSize(rowSize);
	        personRow.setMinimumSize(rowSize);
	        personRow.setMaximumSize(rowSize);
	        personRow.setLayout(new GridLayout(1, 0, 10, 0));
	        
	        personRow.add(button);
	        JCheckBox checkBox = new JCheckBox(); // Goes next to person's name
	        if (type.equals("Customers")) {
		        checkBox.setText("Hungry?");
		        checkBox.setSelected(initiallyHungry.isSelected());
		        checkBox.setEnabled(! initiallyHungry.isSelected());
	        }
	        else { // A waiter
	        	checkBox.setText("Break");
	        	checkBox.setEnabled(false);
	        	
	        	JButton breakButton = new JButton("Ask");
	        	breakButton.addActionListener(this);
	        	personRow.add(breakButton);
	        	askList.add(breakButton);
	        }

	        checkBox.addActionListener(this);
			checkList.add(checkBox);
			personRow.add(checkBox);
	        
	        view.add(personRow);
	
	        restPanel.addPerson(type, personName.getText(), initiallyHungry.isSelected());
	        restPanel.showInfo(type, personList.size() - 1);
        }
        
        // Reset fields
        personName.setText("");
        if (type.equals("Customers")) {
			initiallyHungry.setSelected(false);
        }
        validate();
    }
    
    public int numPeople() {
    	return personList.size();
    }
    
    public String getNameOf(int i) {
    	return personList.get(i).getText();
    }
    
    public void enableCust(int i) {
		checkList.get(i).setEnabled(true);
		checkList.get(i).setSelected(false);
    }
    
    public void setWaiterBreak(boolean allowed, int index) {
		checkList.get(index).setSelected(allowed);
		askList.get(index).setEnabled(! allowed);
    }
}
