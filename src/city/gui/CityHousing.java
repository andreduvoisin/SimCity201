package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

public class CityHousing extends CityComponent {
	private String houseName = "";
	
	public CityHousing(int x, int y) {
		super(x, y, Color.blue, "House 1");
		rectangle = new Rectangle(x, y, 50, 50);
	}

	public CityHousing(int x, int y, String ID) {
		super(x, y, Color.blue, ID);
		rectangle = new Rectangle(x, y, 50, 50);
		houseName = ID;
	}
	
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
	
		g.setColor(color);
		g.fillRect(x, y, 50, 50);
		g.setColor(Color.WHITE);
		g.drawString(houseName,x + 7 , y + 17);
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
