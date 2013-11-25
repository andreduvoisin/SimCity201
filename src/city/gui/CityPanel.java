package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.ContactList;
import base.Location;
import base.interfaces.Person;

public class CityPanel extends SimCityPanel implements MouseMotionListener {
	
	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	SimCityGui simcitygui;
	
	public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	public List<CityHousing> masterHouseList = Collections.synchronizedList(new ArrayList<CityHousing>());
	
	public CityPanel(SimCityGui city) {
		//Setup
		super(city);
		simcitygui = city;
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);

		//Add Background and city block
		background = new Color(100,100,100);
		this.addStatic(new CityBlock(100,100,400,400, new Color(30,30,30)));

		// Add bus
		transportation.TransportationBusDispatch tbd = new transportation.TransportationBusDispatch();
		transportation.TransportationBusInstance tbi = new transportation.TransportationBusInstance(tbd, 4);
		tbd.addBus(tbi);
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