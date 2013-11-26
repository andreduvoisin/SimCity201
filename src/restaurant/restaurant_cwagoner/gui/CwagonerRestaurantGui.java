package restaurant.restaurant_cwagoner.gui;


import java.awt.*;
import java.util.ArrayList;

import city.gui.CityCard;
import city.gui.SimCityGui;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class CwagonerRestaurantGui extends CityCard {
	private int numTables = 4;

	CwagonerAnimationPanel animationPanel;
    CwagonerRestaurantPanel restPanel = new CwagonerRestaurantPanel(this, numTables);
    ArrayList<Dimension> tableLocations = new ArrayList<Dimension>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CwagonerRestaurantGui(SimCityGui city) {
    	super(city);

    	animationPanel = new CwagonerAnimationPanel(CARD_WIDTH, CARD_HEIGHT);

        tableLocations.add(new Dimension(50,80));
        tableLocations.add(new Dimension(100,80));
        tableLocations.add(new Dimension(50,160));
        tableLocations.add(new Dimension(100,160));
        
        for (Dimension d : tableLocations) {
        	animationPanel.addTable(d);
        }

        add(animationPanel);
    	add(restPanel);
    }
    
    /**
     * Allows GUIs to find their locations
     * @param tableNum The number of the table requested
     * @return Dimension object with the coordinates of that table
     */
    public Dimension getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }
}
