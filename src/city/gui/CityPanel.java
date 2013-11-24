package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.interfaces.Person;

public class CityPanel extends SimCityPanel implements MouseMotionListener {
	
	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	SimCityGui simcitygui;
	
	public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	public List<CityHousing> masterHouseList = Collections.synchronizedList(new ArrayList<CityHousing>());
	
	public CityPanel(SimCityGui city) {
		super(city);
		simcitygui = city;
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		background = new Color(128, 64, 0);
		this.addStatic(new CityRestaurant(120, 120, "R_Maggiyan"));
		this.addStatic(new CityRestaurant(80, 400, "Restaurant 2"));
		this.addStatic(new CityBank(400, 75, "Green Guts Bank"));
		
		this.addStatic(new CityMarket(75, 200, "Sears!"));
		
		for (int i = 30; i < 1000; i += 500) {
			this.addStatic(new CityRoad(i, RoadDirection.HORIZONTAL));
			this.addStatic(new CityRoad(i, RoadDirection.VERTICAL));
		}
		
		/*
		 * Housing creation
		 */
				
		for (int iHouseCount = 0; iHouseCount< 80; iHouseCount++) {
			int xCord, yCord = 0;
			if (iHouseCount % 20 == 0) {
				xCord = 50 + (20 * iHouseCount % 20);
				yCord = 0;
			} else if (iHouseCount % 20 == 2) {
				xCord = 50 + (20 * iHouseCount % 20);
				yCord = 480;
			} else if (iHouseCount % 20 == 3) {
				xCord = 0;
				yCord = 50 + 20 * (iHouseCount % 20);
			} else {
				xCord = 480;
				yCord = 50 + 20 * (iHouseCount % 20);
			}
			CityHousing newHouse = new CityHousing(simcitygui, xCord, yCord, "House " + iHouseCount, 50.00);
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
//		if (addingObject) {
//			for (CityComponent c: statics) {
//				if (c.equals(temp))
//					continue;
//				if (c.rectangle.intersects(temp.rectangle))
//					return;
//			}
//			addingObject = false;
//			city.view.addView(new CityCard(city), temp.ID);
//			temp = null;
//		}
		
		//Here is the click method
		for (CityComponent c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				//city.info.setText(c.ID);
				city.cityview.setView(c.ID);
			}
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void addObject(CityComponents c) {
//		if (addingObject)
//			return;
//		addingObject = true;
//		switch (c) {
//		case RESTAURANT: temp = new CityRestaurant(-100, -100, "Restaurant " + (statics.size()-19)); break;
//		case ROAD: temp = new CityRoad(-100, RoadDirection.HORIZONTAL); break; //NOTE: DON'T MAKE NEW ROADS
//		case BANK: temp = new CityBank(-100, -100, "Bank " + (statics.size()-19)); break;
//		default: return;
//		}
//		addStatic(temp);
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
//		if (addingObject) {
//			temp.setPosition(arg0.getPoint());
//		}
	}
}