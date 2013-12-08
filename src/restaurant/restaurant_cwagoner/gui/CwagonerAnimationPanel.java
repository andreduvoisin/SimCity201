package restaurant.restaurant_cwagoner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import restaurant.restaurant_cwagoner.interfaces.CwagonerWaiter;
import restaurant.restaurant_cwagoner.roles.CwagonerCashierRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCookRole;
import restaurant.restaurant_cwagoner.roles.CwagonerCustomerRole;
import restaurant.restaurant_cwagoner.roles.CwagonerHostRole;
import restaurant.restaurant_cwagoner.roles.CwagonerSharedWaiterRole;
import restaurant.restaurant_cwagoner.roles.CwagonerWaiterRole;
import base.Location;
import base.Time;
import base.interfaces.Role;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class CwagonerAnimationPanel extends CityCard implements ActionListener {

	int tableSize = 50, numTables = 4;
    List<CwagonerGui> guis = Collections.synchronizedList(new ArrayList<CwagonerGui>());
    ArrayList<Location> tableLocations = new ArrayList<Location>();

    public CwagonerHostRole host;
    public CwagonerCashierRole cashier;
    public static CwagonerCookRole cook;
    private List<CwagonerCustomerRole> Customers = new ArrayList<CwagonerCustomerRole>();
    private List<CwagonerWaiter> Waiters = new ArrayList<CwagonerWaiter>();

    public CwagonerAnimationPanel(SimCityGui g) {
    	super(g);
    	this.setVisible(true);

    	this.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

    	Timer timer = new Timer(Time.cSYSCLK/15, this);
    	timer.addActionListener(this);
    	timer.start();

    	initializeTables();
    }
    
    private void initializeTables() {
        tableLocations.add(new Location(100, 100));
        tableLocations.add(new Location(300, 100));
        tableLocations.add(new Location(100, 200));
        tableLocations.add(new Location(300, 200));
    }

    public Location getTableLocation(int tableNum) {
    	return tableLocations.get(tableNum);
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  // Will have paint() called
		
		synchronized(guis) {
			for (CwagonerGui gui : guis) {
	            gui.updatePosition();
	        }
		}
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Clear the screen by painting a rectangle the size of the panel
        g2.setColor(getBackground());
        g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        // Tables
        g2.setColor(Color.ORANGE);
        
        for (Location iL : tableLocations) {
        	g2.fillRect(iL.mX, iL.mY, tableSize, tableSize);
        }

        // Cook areas

    	// Stove
		g.drawImage(CwagonerCookGui.stoveImg, CwagonerCookGui.cookingPos.mX, CwagonerCookGui.cookingPos.mY, null);
		// Plating area
		g.drawImage(CwagonerCookGui.tableImg, CwagonerCookGui.platingPos.mX, CwagonerCookGui.platingPos.mY, null);
    	// Fridge
		g.drawImage(CwagonerCookGui.fridgeImg, CwagonerCookGui.fridgePos.mX, CwagonerCookGui.fridgePos.mY, null);

		synchronized(guis) {
	        for (CwagonerGui gui : guis) {
	            gui.draw(g2);
	        }
        }
    }

    public void addGui(CwagonerGui gui) {
    	synchronized(guis) {
    		guis.add(gui);
    	}
    }

    public void addPerson(Role subRole) {
    	if (subRole instanceof CwagonerHostRole) {
    		host = (CwagonerHostRole)subRole;
    		host.setNumTables(numTables);
    	}
    	else if (subRole instanceof CwagonerCashierRole) {
    		cashier = (CwagonerCashierRole)subRole;
    	}
    	else if (subRole instanceof CwagonerCookRole) {
    		cook = (CwagonerCookRole)subRole;
    	}
    	else if (subRole instanceof CwagonerCustomerRole) {	
    		Customers.add((CwagonerCustomerRole) subRole);
    	}
    	else if (subRole instanceof CwagonerSharedWaiterRole) {
    		Waiters.add((CwagonerSharedWaiterRole)subRole);
    	}
    	else if (subRole instanceof CwagonerWaiterRole) {	
    		Waiters.add((CwagonerWaiterRole) subRole);
    	}
    }
}
