package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Location;

public class CityMarket extends CityComponent{
	private String marketName = "";
	
	private static final int sMARKET_SIZE = 80;
	
	public CityMarket(int x, int y) {
		super(x, y, Color.blue, "Market 1");
		rectangle = new Rectangle(x, y, sMARKET_SIZE, sMARKET_SIZE);
	}
	
	public CityMarket(int x, int y, String ID) {
		super(x, y, Color.blue, ID);
		rectangle = new Rectangle(x, y, sMARKET_SIZE, sMARKET_SIZE);
		marketName = ID;
	}
	
	public CityMarket(Location location, String ID) {
		super(location.mX, location.mY, Color.blue, ID);
		rectangle = new Rectangle(x, y, sMARKET_SIZE, sMARKET_SIZE);
		marketName = ID;
	}

	@Override
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fill3DRect(x, y, sMARKET_SIZE, sMARKET_SIZE, true);
		g.setColor(Color.WHITE);
		g.drawString(marketName, x + 10 , y + 50);
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

}
