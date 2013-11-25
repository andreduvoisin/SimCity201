package restaurant.restaurant_jerryweb.gui;

import restaurant.restaurant_jerryweb.CustomerRole;
import restaurant.restaurant_jerryweb.HostRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class WaiterListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private List<JCheckBox> chbxList = new ArrayList<JCheckBox>();
    private JButton addPersonB = new JButton("Add");
    private JButton addWaiter = new JButton("Add");
    private JCheckBox breakChbx;
    
    private JTextField jtfName = new JTextField(10);

    static final int rows = 1;
    static final int cols = 2;
    static final int hSpacing = 20;
	static final int vSpacing = 10;
    
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public WaiterListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        JPanel waiterAdd = new JPanel();
        waiterAdd.setLayout(new GridLayout( rows, cols)); 
        
        

        
        addWaiter.addActionListener(this);
        addPersonB.addActionListener(this);
        
        
        waiterAdd.add(jtfName);
        waiterAdd.add(addPersonB);
        add(waiterAdd);
        
        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    	
        }
    //
      
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	if(jtfName.getText().length() != 0){
        		
        		addPerson(jtfName.getText());
        		jtfName.setText(null);
        		restPanel.showInfo(type, jtfName.getText());
        		 
        	}
        }
        
        else {
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
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
        	JPanel waiterList = new JPanel();
        	waiterList.setLayout(new GridLayout(1,2));
        	
        	JButton button = new JButton(name);
            button.setBackground(Color.white);
            breakChbx = new JCheckBox();
        	chbxList.add(breakChbx);
            
            
            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(3,3);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            waiterList.add(button);

            breakChbx.addActionListener(this);
            breakChbx.setText("Break");
            waiterList.add(breakChbx);
            breakChbx.setVisible(true);
            breakChbx.setEnabled(true);
            for(int i = 0; i < chbxList.size(); i++){
            	if(chbxList.get(i).isSelected()){
            	 restPanel.RpBreakChbx(i);
            	}
            }
            
            view.add(waiterList);
            restPanel.addPerson(type, name);//puts waiter on list
            validate();
        }
    }
}
