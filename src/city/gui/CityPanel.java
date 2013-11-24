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
	
	public CityPanel(SimCityGui city) {
		super(city);
		simcitygui = city;
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		background = new Color(128, 64, 0);
		this.addStatic(new CityRestaurant(75, 75, "R_Maggiyan"));
		this.addStatic(new CityRestaurant(80, 400, "Restaurant 2"));
		this.addStatic(new CityBank(400, 75, "Green Guts Bank"));
		
		this.addStatic(new CityMarket(75, 200, "Sears!"));
		this.addMoving(new CityPerson(40, 40, "Bob"));
		
		for (int i = 30; i < 1000; i += 500) {
			this.addStatic(new CityRoad(i, RoadDirection.HORIZONTAL));
			this.addStatic(new CityRoad(i, RoadDirection.VERTICAL));
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
		if (addingObject) {
			for (CityComponent c: statics) {
				if (c.equals(temp))
					continue;
				if (c.rectangle.intersects(temp.rectangle))
					return;
			}
			addingObject = false;
			city.cityview.addView(new CityCard(city), temp.ID);
			temp = null;
		}
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
		if (addingObject)
			return;
		addingObject = true;
		switch (c) {
		case RESTAURANT: temp = new CityRestaurant(-100, -100, "Restaurant " + (statics.size()-19)); break;
		case ROAD: temp = new CityRoad(-100, RoadDirection.HORIZONTAL); break; //NOTE: DON'T MAKE NEW ROADS
		case BANK: temp = new CityBank(-100, -100, "Bank " + (statics.size()-19)); break;
		default: return;
		}
		addStatic(temp);
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
		if (addingObject) {
			temp.setPosition(arg0.getPoint());
		}
	}
}