package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityRestaurant extends CityComponent {

	public CityRestaurant(int x, int y) {
		super(x, y, Color.red, "Restaurant 1");
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	public CityRestaurant(int x, int y, String ID) {
		super(x, y, Color.red, ID);
		rectangle = new Rectangle(x, y, 20, 20);
	}

	public void updatePosition() {
		
	}

	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, 20, 20);
		g.fill3DRect(x, y, 20, 20, true);
	}

	
//	public boolean contains(int x, int y) {
//		if (x >= this.x && x <= this.x+20)
//			if (y >= this.y && y <= this.y+20)
//				return true;
//		return false;
//	}

}
