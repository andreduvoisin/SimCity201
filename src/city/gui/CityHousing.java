package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

public class CityHousing extends CityComponent {

	public CityHousing(int x, int y) {
		super(x, y, Color.blue, "House 1");
		rectangle = new Rectangle(x, y, 20, 20);
	}

	public CityHousing(int x, int y, String I) {
		super(x, y, Color.blue, I);
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
	
		g.setColor(color);
		g.fillRect(x, y, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("H",x + 7 , y + 17);
	}	

}
