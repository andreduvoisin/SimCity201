package restaurant.restaurant_cwagoner.gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import base.Location;
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
    ArrayList<Location> tableLocations = new ArrayList<Location>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public CwagonerRestaurantGui(SimCityGui city) {
    	super(city);

    	setVisible(true);

    	animationPanel = new CwagonerAnimationPanel(CARD_WIDTH, CARD_HEIGHT);

        tableLocations.add(new Location(100, 100));
        tableLocations.add(new Location(300, 100));
        tableLocations.add(new Location(100, 200));
        tableLocations.add(new Location(300, 200));
        
        for (Location iL : tableLocations) {
        	animationPanel.addTable(iL);
        }

        this.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        this.setLayout(new GridLayout(2, 1));

        add(animationPanel);
    	add(restPanel);
    }
    
    /**
     * Allows GUIs to find their locations
     * @param tableNum The number of the table requested
     * @return Dimension object with the coordinates of that table
     */
    public Location getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }
}
