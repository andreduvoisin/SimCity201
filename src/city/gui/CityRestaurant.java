package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Location;

public class CityRestaurant extends CityComponent {
	private String restaurantName = "";
	private int RESTAURANTSIZE = 80; 
	
	public CityRestaurant(int x, int y) {
		super(x, y, Color.red, "Unnamed Restaurant");
		rectangle = new Rectangle(x, y, RESTAURANTSIZE, RESTAURANTSIZE);
	}
	
	public CityRestaurant(int x, int y, String ID) {
		super(x, y, Color.red, ID);
		rectangle = new Rectangle(x, y, RESTAURANTSIZE, RESTAURANTSIZE);
		restaurantName = ID;
	}

	public CityRestaurant(Location location, String ID) {
		super(location.mX, location.mY, Color.red, ID);
		restaurantName = ID;
	}
	
	public void updatePosition() {
		
	}

	
	public void paint(Graphics g) {
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(color);
			g.fill3DRect(x, y, RESTAURANTSIZE, RESTAURANTSIZE, true);
			g.setColor(Color.BLACK);
			g.drawString(restaurantName,x + 5 , y + 15);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}

	
//	public boolean contains(int x, int y) {
//		if (x >= this.x && x <= this.x+20)
//			if (y >= this.y && y <= this.y+20)
//				return true;
//		return false;
//	}

}
