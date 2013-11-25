package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;


import base.PersonAgent;

import base.ContactList;
import base.Location;
import base.interfaces.Person;

@SuppressWarnings("serial")
public class CityPanel extends SimCityPanel implements MouseMotionListener {
	
	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	SimCityGui simcitygui;

	public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	public List<CityHousing> masterHouseList = Collections.synchronizedList(new ArrayList<CityHousing>());
	
	// A*
	public static Semaphore[][] grid = new Semaphore[CITY_WIDTH / 5][CITY_HEIGHT / 5];
	

	public CityPanel(SimCityGui city) {
		//Setup
		super(city);
		masterPersonList.add(new PersonAgent(PersonAgent.EnumJobType.NONE, 20, "bob"));
		
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		background = new Color(128, 64, 0);
		this.addStatic(new CityRestaurant(75, 75, "R_Maggiyan"));
		this.addStatic(new CityRestaurant(80, 400, "Restaurant 2"));
		this.addStatic(new CityBank(400, 75, "Green Guts Bank"));
		//this.addStatic(new CityHousing(400, 400, "House 1"));
		this.addStatic(new CityMarket(75, 200, "Sears!"));
		CityPerson cp = new CityPerson(40, 40, "Bob");
		masterPersonList.get(0).setComponent(cp);
		
		this.addMoving(cp);

		simcitygui = city;
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);

		// A* Setup
		try {
			for(int i = 0; i < (CITY_WIDTH/5); i++) {
				for(int j = 0; j < (CITY_HEIGHT/5); j++) {
					grid[i][j] = new Semaphore(1, true);
				}
			}
			// Center (100,100 to 500,500)
			for(int i = 100/5; i < 500/5; i++) {
				for(int j = 100/5; j < 500/5; j++) {
					grid[i][j].acquire();
				}
			}
			// Houses
			for (int iHouse = 0 ; iHouse < 80; iHouse++){ //80 Houses
				int xCord, yCord = 0;
				if (iHouse / 20 == 0) {					//North
					xCord = 100 + 20 * (iHouse % 20);
					yCord = 0;
				} else if (iHouse / 20 == 2) {			//South
					xCord = 100 + 20 * (iHouse % 20);
					yCord = 580;
				} else if (iHouse / 20 == 3) {			//West
					xCord = 0;
					yCord = 100 + 20 * (iHouse % 20);
				} else {								//East
					xCord = 580;
					yCord = 100 + 20 * (iHouse % 20);
				}
				for(int i = 0; i < 4; i++)
					for(int j = 0; j < 4; j++)
						grid[xCord / 5 + i][yCord / 5 + j].acquire();
			}
		} catch (Exception e) {
			System.out.println("Exception During A* Setup: " + e);
		}

		//Add Background and city block
		background = new Color(100,100,100);
		this.addStatic(new CityBlock(100,100,400,400, new Color(30,30,30)));

		// Add bus
		transportation.TransportationBusDispatch tbd = new transportation.TransportationBusDispatch();
		transportation.TransportationBusInstance tbi = new transportation.TransportationBusInstance(tbd, 4);
		tbd.startThread();

		this.addMoving(tbi.getCityBus());

		//Add Roads
		this.addStatic(new CityRoad(35, RoadDirection.VERTICAL));
		this.addStatic(new CityRoad(515, RoadDirection.VERTICAL));
		this.addStatic(new CityRoad(35, RoadDirection.HORIZONTAL));
		this.addStatic(new CityRoad(515, RoadDirection.HORIZONTAL));
		
		//Add static buildings
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(0), "R_aduvoisin"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(1), "R_cwagoner"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(2), "R_jerrywebb"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(3), "R_maggiyang"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(4), "R_davidmca"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(5), "R_smileham"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(6), "R_tranac"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(7), "R_xurex"));
		this.addStatic(new CityBank(ContactList.cBANK_LOCATION, "Gringotts Bank"));
		this.addStatic(new CityMarket(ContactList.cMARKET_LOCATION, "Costco"));
		this.addStatic(new CityMarket(ContactList.cCARDEALERSHIP_LOCATION, "Car Dealership"));
		
		//Create Houses		
		for (int iHouseCount = 0; iHouseCount< 80; iHouseCount++) {
			Location houseLocation = ContactList.cHOUSE_LOCATIONS.get(iHouseCount);
			CityHousing newHouse = new CityHousing(simcitygui, houseLocation.mX, houseLocation.mY, iHouseCount, 50.00);
			simcitygui.cityview.addView(newHouse.mPanel, "House " + iHouseCount);
			this.addStatic(newHouse);
			masterHouseList.add(newHouse);
		}
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		for (CityComponent c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				//city.info.setText(c.ID);
				city.cityview.setView(c.ID);
			}
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {

	}
}