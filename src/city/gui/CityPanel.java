package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import market.gui.MarketDeliveryTruckGui;
import transportation.TransportationBus;
import base.ContactList;

@SuppressWarnings("serial")
public class CityPanel extends SimCityPanel implements MouseMotionListener {
	static CityPanel instance;

	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	SimCityGui simcitygui;

	TransportationBus busDispatch;
	
	public CityPanel(SimCityGui city) {
		//Setup
		super(city);
		instance = this;		
		simcitygui = city;
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		
		// Houses - DONE IN CITYVIEW
		
		//Add Background and city block
		background = new Color(100,100,100);
		this.addStatic(new CityBlock(100,100,180,180, new Color(30,30,30)));
		this.addStatic(new CityBlock(320,100,180,180, new Color(30,30,30)));
		this.addStatic(new CityBlock(100,320,180,180, new Color(30,30,30)));
		this.addStatic(new CityBlock(320,320,180,180, new Color(30,30,30)));

		//Add Bus
		busDispatch = new TransportationBus();
		this.addMoving(busDispatch.getBusGui());
		busDispatch.startThread();
		
		//Add Roads
		this.addStatic(new CityRoad(35, RoadDirection.VERTICAL, 50, 600));
		this.addStatic(new CityRoad(515, RoadDirection.VERTICAL, 50, 600));
		this.addStatic(new CityRoad(35, RoadDirection.HORIZONTAL, 50, 600));
		this.addStatic(new CityRoad(515, RoadDirection.HORIZONTAL, 50, 600));
		this.addStatic(new CityRoad(280, RoadDirection.VERTICAL, 40, 480));
		this.addStatic(new CityRoad(280, RoadDirection.HORIZONTAL, 40, 480));
		
		//Add Intersections
		
//		//North-Center
		this.addIntersection(new CityIntersection(ContactList.cINTERSECTIONBLOCK0));
		//Center
		this.addIntersection(new CityIntersection(ContactList.cINTERSECTIONBLOCK1));
		//East-Center
		this.addIntersection(new CityIntersection(ContactList.cINTERSECTIONBLOCK2));
		//West-Center
		this.addIntersection(new CityIntersection(ContactList.cINTERSECTIONBLOCK3));
		//South-Central #InDaHood
		this.addIntersection(new CityIntersection(ContactList.cINTERSECTIONBLOCK4));
		
		//Add static buildings
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(0), "r_duvoisin"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(1), "r_cwagoner"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(2), "r_jerryweb"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(3), "r_maggiyan"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(4), "r_davidmca"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(5), "r_smileham"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(6), "r_tranac"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(7), "r_xurex"));
		this.addStatic(new CityBank(ContactList.cBANK1_LOCATION, "Gringotts"));
		this.addStatic(new CityBank(ContactList.cBANK2_LOCATION, "Piggy Bank"));
		this.addStatic(new CityMarket(ContactList.cMARKET1_LOCATION, "Honeydukes"));
		this.addStatic(new CityMarket(ContactList.cMARKET2_LOCATION, "Ollivanders"));
			
		//Create Timer Display
		this.addStatic(new TimeGui(520, 575));
		
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