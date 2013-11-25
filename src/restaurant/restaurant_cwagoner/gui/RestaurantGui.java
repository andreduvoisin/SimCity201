package restaurant.restaurant_cwagoner.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends JFrame {

	private int numTables = 4;

	JPanel animationFrame = new JPanel();
	AnimationPanel animationPanel = new AnimationPanel();
    private RestaurantPanel restPanel = new RestaurantPanel(this, numTables);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    
    private ArrayList<Dimension> tableLocations = new ArrayList<Dimension>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	super();

    	int screenWidth = getToolkit().getScreenSize().width - 40,
    		screenHeight = getToolkit().getScreenSize().height - 80;

        tableLocations.add(new Dimension(screenWidth / 10,	screenHeight / 8));
        tableLocations.add(new Dimension(screenWidth / 5,	screenHeight / 8));
        tableLocations.add(new Dimension(screenWidth / 10,	screenHeight / 4));
        tableLocations.add(new Dimension(screenWidth / 5,	screenHeight / 4));
        
        for (Dimension d : tableLocations) {
        	animationPanel.addTable(d);
        }
        
        // Restaurant animation and information
    	this.add(animationPanel);
    }
    
    /**
     * Allows GUIs to find their locations
     * @param tableNum The number of the table requested
     * @return The coordinates of that table
     */
    public Dimension getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }
    
    /**
     * Main routine to get GUI started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("CSCI 201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
