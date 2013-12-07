package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import market.gui.MarketDeliveryTruckGui;
import transportation.TransportationBusDispatch;
import base.reference.ContactList;

@SuppressWarnings("serial")
public class CityPanel extends SimCityPanel implements MouseMotionListener {
	static CityPanel instance;

	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	SimCityGui simcitygui;

	TransportationBusDispatch busDispatch;
	
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
		this.addStatic(new CityBlock(100,100,400,400, new Color(30,30,30)));

		//CHASE: Add bus

		busDispatch = new TransportationBusDispatch();
		this.addMoving(busDispatch.getBusGui());
		busDispatch.startThread();


		//Add Roads
		this.addStatic(new CityRoad(35, RoadDirection.VERTICAL));
		this.addStatic(new CityRoad(515, RoadDirection.VERTICAL));
		this.addStatic(new CityRoad(35, RoadDirection.HORIZONTAL));
		this.addStatic(new CityRoad(515, RoadDirection.HORIZONTAL));
		
//		Add static buildings
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(0), "R_aduvoisin"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(1), "R_cwagoner"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(2), "R_jerryweb"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(3), "R_Maggiyan"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(4), "R_davidmca"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(5), "R_smileham"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(6), "R_tranac"));
		this.addStatic(new CityRestaurant(ContactList.cRESTAURANT_LOCATIONS.get(7), "R_xurex"));
		this.addStatic(new CityBank(ContactList.cBANK1_LOCATION, "Gringotts Bank"));
		this.addStatic(new CityBank(ContactList.cBANK2_LOCATION, "Piggy Bank"));
		this.addStatic(new CityMarket(ContactList.cMARKET1_LOCATION, "Costco"));
		this.addStatic(new CityMarket(ContactList.cMARKET2_LOCATION, "Sams Club"));
			
		//Create Timer Display
		this.addStatic(new TimeGui(540, 560));
		
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