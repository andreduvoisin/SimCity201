package reference.simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;

public class CityPanel extends SimCityPanel implements MouseMotionListener {

	public static final int CITY_WIDTH = 600, CITY_HEIGHT = 600;
	boolean addingObject = false;
	CityComponent temp;
	
	String name = "City Panel";
	
	public CityPanel(SimCityGui city) {
		super(city);
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		background = new Color(128, 64, 0);
		this.addStatic(new CityRestaurant(30, 30));
		this.addStatic(new CityRestaurant(60, 30, "Restaurant 2"));
		for (int i = 10; i < 1000; i += 100) {
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
			//make sure we aren't overlapping anything
			for (CityComponent c: statics) {
				if (c.equals(temp))
					continue;
				if (c.rectangle.intersects(temp.rectangle)) {
					AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, this.name, "Can't add building, location obstructed!");
					return;
				}
			}
			AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Building successfully added");
			addingObject = false;
			city.view.addView(new CityCard(city, Color.pink), temp.ID);
			temp = null;
		}
		for (CityComponent c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				//city.info.setText(c.ID);
				city.view.setView(c.ID);
				AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Building Selected: " + c.ID);
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
