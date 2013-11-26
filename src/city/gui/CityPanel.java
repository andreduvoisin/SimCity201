package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import transportation.TransportationBusDispatch;
import market.gui.MarketDeliveryTruckGui;
import market.roles.MarketDeliveryTruckRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;

@SuppressWarnings("serial")
public class CityPanel extends SimCityPanel implements MouseMotionListener {
	static CityPanel instance;

	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	SimCityGui simcitygui;

	TransportationBusDispatch busDispatch;
	
	public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	public List<CityHousing> masterHouseList = Collections.synchronizedList(new ArrayList<CityHousing>());
	
	// A*
	public static final int ASC = 5;
	public static final int NG = 4;
	public static Semaphore[][] grid = new Semaphore[CITY_WIDTH / ASC][CITY_HEIGHT / ASC];
	public static Semaphore[][] gridNW = new Semaphore[60][60];
	public static Semaphore[][] gridNE = new Semaphore[60][60];
	public static Semaphore[][] gridSW = new Semaphore[60][60];
	public static Semaphore[][] gridSE = new Semaphore[60][60];
	
	public CityPanel(SimCityGui city) {
		//Setup
		super(city);
		instance = this;		
		simcitygui = city;
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);

		// A* Setup
		try {
			for(int i = 0; i < (CITY_WIDTH/ASC); i++) {
				for(int j = 0; j < (CITY_HEIGHT/ASC); j++) {
					grid[i][j] = new Semaphore(1, true);
				}
			}
			
			// Center (100,100 to 500,500)
			for(int i = 100/ASC; i < 500/ASC; i++) {
				for(int j = 100/ASC; j < 500/ASC; j++) {
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
						grid[(xCord / ASC) + i][(yCord / ASC) + j].acquire();
			}
			// Roads (temp...)
			for(int i = 35/ASC; i < 85/ASC; i++)
				for(int j = 0; j < 600/ASC; j++) {
					grid[i][j].tryAcquire();
					grid[j][i].tryAcquire();
				}
			for(int i = 515/ASC; i < 565/ASC; i++)
				for(int j = 0; j < 600/ASC; j++) {
					grid[i][j].tryAcquire();
					grid[j][i].tryAcquire();
				}
			for(int i = 0; i < 600/ASC; i++)
				grid[i][7].release();
			for(int j = 0; j < 600/ASC; j++)
				grid[85/ASC][j].release();
			for(int i = 0; i < 600/ASC; i++)
				grid[i][17].release();
			for(int j = 0; j < 600/ASC; j++)
				grid[35/ASC][j].release();
			
			// Apply to 4 grids.
			for(int i = 0; i < 60; i++)
				for(int j = 0; j < 60; j++) {
					gridNW[i][j] = grid[i][j];
					gridNE[i][j] = grid[i + 60][j];
					gridSW[i][j] = grid[i][j + 60];
					gridSE[i][j] = grid[i + 60][j + 60];
				}
		} catch (Exception e) {
			System.out.println("Exception During A* Setup: " + e);
		}

		//Add Background and city block
		background = new Color(100,100,100);
		this.addStatic(new CityBlock(100,100,400,400, new Color(30,30,30)));

		// Add bus
/*		busDispatch = new TransportationBusDispatch(ContactList.cBUS_STOPS);
		this.addMoving(busDispatch.getBusGui());
		busDispatch.startThread();
*/

		//Add Roads
		this.addStatic(new CityRoad(35, RoadDirection.VERTICAL));
		this.addStatic(new CityRoad(515, RoadDirection.VERTICAL));
		this.addStatic(new CityRoad(35, RoadDirection.HORIZONTAL));
		this.addStatic(new CityRoad(515, RoadDirection.HORIZONTAL));
		
		//Add static buildings
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(0), "R_aduvoisin"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(1), "R_cwagoner"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(2), "R_jerryweb"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(3), "R_Maggiyan"));
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

	public static CityPanel getInstance() {
		return instance;
	}
	
	public void addDeliveryTruck(MarketDeliveryTruckGui g) {
		addMoving(g);
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