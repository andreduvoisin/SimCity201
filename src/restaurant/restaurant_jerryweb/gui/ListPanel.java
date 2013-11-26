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
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private List<JCheckBox> chbxList = new ArrayList<JCheckBox>();
    private JButton addPersonB = new JButton("Add");
    private JButton addWaiter = new JButton("Add");
    
    private JTextField jtfName = new JTextField(10);
    private JCheckBox preHungrychbx;
    private JCheckBox hungrychbx; 
    static final int rows = 1;
    static final int cols = 3;
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
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        JPanel custAdd = new JPanel();
        custAdd.setLayout(new GridLayout( rows, cols)); 
        
        preHungrychbx = new JCheckBox();        
        preHungrychbx.setVisible(true);

        addWaiter.addActionListener(this);
        addPersonB.addActionListener(this);
        
        
        custAdd.add(jtfName);
        custAdd.add(addPersonB);
        //custAdd.add(preHungrychbx);
        add(custAdd);
        
        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    	
        }
    public JCheckBox getChbx (){
    	return hungrychbx;
    }

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

        		preHungrychbx.setSelected(false);
        	}
        }
        
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        	for(int i = 0; i <chbxList.size(); i++){
        		JCheckBox tempBox = chbxList.get(i);
        		if (e.getSource() == tempBox){
        			if(tempBox.getText() == restPanel.getCustomer(i).getName()){
        				restPanel.getCustomer(i).getGui().setHungry();
        				//restPanel.getCustomer(i).gotHungry();
        				chbxList.get(i).setEnabled(false);
        			}
        		}
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
        	JPanel customerList = new JPanel();
        	customerList.setLayout(new GridLayout(1,2));
        	
        	JButton button = new JButton(name);
        	hungrychbx = new JCheckBox();
        	chbxList.add(hungrychbx);
           /* button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(5,5);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);*/
        	//customerList.add(button);
            hungrychbx.addActionListener(this);
            hungrychbx.setText(name);
            customerList.add(hungrychbx);
            hungrychbx.setVisible(true);
            hungrychbx.setEnabled(true);

            if(hungrychbx.isSelected()){
            	restPanel.RPorginalChbx();//.setSelected(true);
            	//hungrychbx.setSelected(true);
            }

            view.add(customerList);
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }

}
